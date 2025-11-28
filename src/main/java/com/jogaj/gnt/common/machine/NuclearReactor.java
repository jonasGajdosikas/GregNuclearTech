package com.jogaj.gnt.common.machine;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.capability.IEnergyContainer;
import com.gregtechceu.gtceu.api.capability.ITurbineMachine;
import com.gregtechceu.gtceu.api.capability.recipe.EURecipeCapability;
import com.gregtechceu.gtceu.api.capability.recipe.IO;
import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.fancy.FancyMachineUIWidget;
import com.gregtechceu.gtceu.api.gui.fancy.IFancyUIProvider;
import com.gregtechceu.gtceu.api.machine.IMachineBlockEntity;
//import com.gregtechceu.gtceu.api.machine.MetaMachine;
import com.gregtechceu.gtceu.api.machine.TickableSubscription;
import com.gregtechceu.gtceu.api.machine.feature.IExplosionMachine;
import com.gregtechceu.gtceu.api.machine.feature.IFancyUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.ITieredMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IDisplayUIMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMaintenanceMachine;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IMultiPart;
import com.gregtechceu.gtceu.api.machine.feature.multiblock.IRotorHolderMachine;
import com.gregtechceu.gtceu.api.machine.multiblock.WorkableMultiblockMachine;
import com.gregtechceu.gtceu.api.misc.EnergyContainerList;
import com.gregtechceu.gtceu.api.recipe.GTRecipe;
//import com.gregtechceu.gtceu.api.recipe.modifier.ModifierFunction;
//import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.gui.modular.ModularUI;
import com.lowdragmc.lowdraglib.gui.util.ClickData;
import com.lowdragmc.lowdraglib.gui.widget.*;
import com.lowdragmc.lowdraglib.syncdata.annotation.DescSynced;
import com.lowdragmc.lowdraglib.syncdata.annotation.Persisted;
import com.lowdragmc.lowdraglib.syncdata.field.ManagedFieldHolder;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

import com.jogaj.gnt.GNT;
import com.jogaj.gnt.api.block.IModeratorType;
import com.jogaj.gnt.common.block.ModeratorBlock;
import com.jogaj.gnt.config.GNTConfig;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import static com.jogaj.gnt.GNT.LOGGER;

public class NuclearReactor extends WorkableMultiblockMachine
                            implements IFancyUIMachine, ITieredMachine, IExplosionMachine, IDisplayUIMachine,
                            ITurbineMachine {

    protected static final ManagedFieldHolder MANAGED_FIELD_HOLDER = new ManagedFieldHolder(NuclearReactor.class,
            WorkableMultiblockMachine.MANAGED_FIELD_HOLDER);

    @Override
    public @NotNull ManagedFieldHolder getFieldHolder() {
        return MANAGED_FIELD_HOLDER;
    }

    public static final int MIN_DURABILITY_TO_WARN = 10;
    public static final int TICKS_PER_HEAT_UPDATE = 10;
    public static final int BOILING_TEMP = 100;
    public static final int C_TO_K_OFFSET = 274;

    @Getter
    @Persisted
    @DescSynced
    private int controlRods;
    @Getter
    @Persisted
    @DescSynced
    private double temp;

    private double getHeat() {
        return temp * moderatorType.getHeatCapacity();
    }

    private void addHeat(double heatToAdd) {
        temp += heatToAdd / moderatorType.getHeatCapacity();
    }

    private @Getter IModeratorType moderatorType = ModeratorBlock.ModeratorType.WATER;

    protected @Nullable TickableSubscription radiationHeatSubs;

    private double partialProgress;
    private EnergyContainerList outputHatches;
    private long netEnergyGeneratedLastSec;
    private @Getter long outputPerSec;

    private IMaintenanceMachine maintenance;
    private final long BASE_EU_OUTPUT;

    public NuclearReactor(IMachineBlockEntity holder, int tier,
                          Object... args) {
        super(holder, args);
        this.BASE_EU_OUTPUT = GTValues.V[tier] * 2;
        this.controlRods = 0;
        this.partialProgress = 0;
    }

    // region Rotor stuff

    private @Nullable IRotorHolderMachine getRotorHolder() {
        for (IMultiPart part : getParts()) {
            if (part instanceof IRotorHolderMachine rotorHolder) return rotorHolder;
        }
        return null;
    }

    protected double rotorBoost() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder == null || !rotorHolder.hasRotor()) return 0;
        int maxSpeed = rotorHolder.getMaxRotorHolderSpeed();
        int currentSpeed = rotorHolder.getRotorSpeed();
        if (currentSpeed >= maxSpeed) return 1;
        return Math.pow(1.0 * currentSpeed / maxSpeed, 2);
    }

    @Override
    public boolean hasRotor() {
        var rotorHolder = getRotorHolder();
        return rotorHolder != null && rotorHolder.hasRotor();
    }

    @Override
    public int getRotorSpeed() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor())
            return rotorHolder.getRotorSpeed();
        return 0;
    }

    @Override
    public int getMaxRotorHolderSpeed() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor())
            return rotorHolder.getMaxRotorHolderSpeed();
        return 0;
    }

    @Override
    public int getTotalEfficiency() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder != null && rotorHolder.hasRotor())
            return rotorHolder.getTotalEfficiency();
        return -1;
    }

    @Override
    public int getRotorDurabilityPercent() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder == null || !rotorHolder.hasRotor()) return -1;
        return rotorHolder.getRotorDurabilityPercent();
    }

    // endregion

    // region Recipe Logic
    // region Heat Management
    public @Override void onStructureFormed() {
        super.onStructureFormed();
        var type = getMultiblockState().getMatchContext().get("ModeratorType");
        if (type instanceof IModeratorType moderator) moderatorType = moderator;
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateWorkSubscription));
        }
        List<IEnergyContainer> outputs = new ArrayList<>();
        Long2ObjectMap<IO> ioMap = getMultiblockState().getMatchContext().getOrCreate("ioMap",
                Long2ObjectMaps::emptyMap);
        for (IMultiPart part : getParts()) {
            IO io = ioMap.getOrDefault(part.self().getPos().asLong(), IO.BOTH);
            if (io == IO.NONE) continue;
            if (part instanceof IMaintenanceMachine maintHatch) maintenance = maintHatch;
            var handlersList = part.getRecipeHandlers();
            for (var handlerList : handlersList) {
                if (!handlerList.isValid(io)) continue;

                var containers = handlerList.getCapability(EURecipeCapability.CAP).stream()
                        .filter(IEnergyContainer.class::isInstance)
                        .map(IEnergyContainer.class::cast)
                        .toList();

                if (handlerList.getHandlerIO().support(IO.OUT))
                    outputs.addAll(containers);
            }
        }
        // GNT.LOGGER.info("{} outputs on reactor",outputs.size());
        outputHatches = new EnergyContainerList(outputs);
    }

    public @Override void onStructureInvalid() {
        outputHatches = null;

        super.onStructureInvalid();
        if (getLevel() instanceof ServerLevel serverLevel) {
            serverLevel.getServer().tell(new TickTask(0, this::updateWorkSubscription));
        }
    }

    public @Override void onUnload() {
        if (radiationHeatSubs != null) {
            radiationHeatSubs.unsubscribe();
            radiationHeatSubs = null;
        }
        super.onUnload();
    }

    protected void updateWorkSubscription() {
        if (temp > 0) {
            radiationHeatSubs = subscribeServerTick(radiationHeatSubs, this::updateCurrentradiationHeat);
        } else if (radiationHeatSubs != null) {
            radiationHeatSubs.unsubscribe();
            radiationHeatSubs = null;
        }
    }

    protected void updateCurrentradiationHeat() {
        if (recipeLogic.isWorking()) {
            if (getOffsetTimer() % TICKS_PER_HEAT_UPDATE == 0) {
                var activeRecipe = recipeLogic.getLastRecipe();
                if (activeRecipe != null) {
                    int thermal = tryGetRecipeData(activeRecipe, "thermalNeutrons");
                    int fast = tryGetRecipeData(activeRecipe, "fastNeutrons");
                    int recipeHeat = (int) (thermal + moderatorType.getFastNeutronConversion() * fast);
                    addHeat(TICKS_PER_HEAT_UPDATE * ctrlRodMultiplier() * recipeHeat);

                    partialProgress += Math.pow(ctrlRodMultiplier(), 0.95);
                    //LOGGER.info(partialProgress);
                    if (partialProgress >= 1.0) partialProgress -= 1.0;
                    else recipeLogic.setProgress(recipeLogic.getProgress() - TICKS_PER_HEAT_UPDATE);
                }
            }
        }
        if (temp > 0) {
            // it reduces heat 9 ticks out of 10
            addHeat(-getHeatDissipation() * 10.0 / 9.0);
        }

        if (isFormed() && getOffsetTimer() % TICKS_PER_HEAT_UPDATE == 0) {
            if (temp > moderatorType.getMaxTemp()) {
                if (GNTConfig.INSTANCE.values.scramBeforeOverheat) {
                    scram();
                }
                int overheat = (int) temp - moderatorType.getMaxTemp() + maintenance.getNumMaintenanceProblems() * 20 -
                        GTValues.RNG.nextInt(100);
                //GNT.LOGGER.info("overheating by {}", overheat);
                if (overheat > 100) {
                    goVacNuke(overheat / 5f);
                }
                if (overheat > 0) {
                    byte problem = (byte) (maintenance.getMaintenanceProblems() &
                            (0xff ^ (0b1 << GTValues.RNG.nextInt(6))));
                    maintenance.setMaintenanceProblems(problem);
                }
            }
        }
        generateEnergyFromHeatTick();
        updateWorkSubscription();
    }

    @Override
    public boolean onWorking() {
        boolean value = super.onWorking();
        if (getTemp() < moderatorType.getMaxTemp()) {
            if (getTemp() < 1)
                addHeat(moderatorType.getHeatCapacity());
            updateWorkSubscription();
        }

        return value;
    }

    void goVacNuke(float power) {
        GNT.LOGGER.info("going vacnuke with {} power", power);
        doExplosion(power);
        var center = getPos().below().relative(getFrontFacing().getOpposite());
        if (GTValues.RNG.nextInt(100) > 80)
            doExplosion(center, power);
        for (Direction x : Direction.Plane.HORIZONTAL) {
            for (Direction y : Direction.Plane.VERTICAL) {
                if (GTValues.RNG.nextInt(100) > 80)
                    doExplosion(center.relative(x).relative(y), power);
            }
        }
    }

    private void scram() {
        this.setWorkingEnabled(false);
        maintenance.setMaintenanceProblems(IMaintenanceMachine.ALL_PROBLEMS);
        controlRods = 100;
        recipeLogic.setSuspendAfterFinish(true);
        recipeLogic.interruptRecipe();
    }

    int tryGetRecipeData(GTRecipe recipe, String key) {
        return recipe.data.contains(key) ? recipe.data.getInt(key) : 0;
    }

    protected double getHeatDissipation() {
        return getTemp() * .01 + 1;
    }

    private double ctrlRodMultiplier() {
        return 1.0 - (controlRods / 125.0);
    }

//    /**
//     * Recipe Modifier for <b>Nuclear Reactors</b> - can be used as a valid {@link RecipeModifier}
//     * <p>
//     * Duration is multiplied with {@code (1- controlRods/125)^-.95} if control rods are inserted more than 0
//     * </p>
//     *
//     * @param machine a {@link NuclearReactor}
//     * @param recipe  recipe
//     * @return A {@link ModifierFunction} for the given reactor and recipe
//     */
//    public static ModifierFunction ctrlRodModifier(@NotNull MetaMachine machine, @NotNull GTRecipe recipe) {
//        if (!(machine instanceof NuclearReactor reactor))
//            return RecipeModifier.nullWrongType(NuclearReactor.class, machine);
//        if (reactor.controlRods == 0) return ModifierFunction.IDENTITY;
//        return ModifierFunction.builder()
//                .durationMultiplier(Math.pow(reactor.ctrlRodMultiplier(), -0.95))
//                .build();
//    }

    // endregion

    // region Power Generation

    protected void generateEnergyFromHeatTick() {
        // GNT.LOGGER.info("hello from generateEnergyFromHeatTick");
        // noinspection DataFlowIssue
        if (getLevel().isClientSide) return;
        if (getOffsetTimer() % 20 == 0) {
            outputPerSec = netEnergyGeneratedLastSec;
            netEnergyGeneratedLastSec = 0;
        }

        if (isFormed()) {
            long energyCreated = generateEnergy(outputHatches.getEnergyCapacity() - outputHatches.getEnergyStored());
            outputHatches.changeEnergy(energyCreated);
            netEnergyGeneratedLastSec += energyCreated;

        }
    }

    enum CappedBy {
        TEMP,
        HEAT,
        OUTPUT
    }

    private double moderatorMult(){
        return Math.cbrt(moderatorType.getHeatCapacity());
    }

    @Override
    public long getCurrentProduction() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder == null) return 0;
        double holderEfficiency = rotorHolder.getTotalEfficiency() / 100.0;
        // don't make energy if there is no requirement or if there is no working rotor
        if (holderEfficiency <= 0) return 0;

        // making heat energy nonlinear to incentivize use as baseline power
        var maxTempEnergy = .169256 * Math.pow(getTemp() - BOILING_TEMP, 2);
        var availableHeatEnergy = maxTempEnergy * moderatorMult() * holderEfficiency * rotorBoost();

        return (long) Math.min(getOverclockVoltage(), availableHeatEnergy);
    }

    private CappedBy productionCap(){
        var rotorHolder = getRotorHolder();
        if (rotorHolder == null) return CappedBy.OUTPUT;
        double holderEfficiency = rotorHolder.getTotalEfficiency() / 100.0;
        // don't make energy if there is no requirement or if there is no working rotor
        if (holderEfficiency <= 0) return CappedBy.OUTPUT;

        var maxTempEnergy = .169256 * Math.pow(getTemp() - BOILING_TEMP, 2);
        var availableHeatEnergy = maxTempEnergy * moderatorMult() * holderEfficiency * rotorBoost();

        return (getOverclockVoltage() < availableHeatEnergy) ? CappedBy.OUTPUT : CappedBy.TEMP;
    }

    public @Override long getOverclockVoltage() {
        var rotorHolder = getRotorHolder();
        if (rotorHolder == null || !rotorHolder.hasRotor()) return 0;
        return BASE_EU_OUTPUT * rotorHolder.getTotalPower() / 100;
    }

    long generateEnergy(long tryGenerateAmount) {
        return Math.min(tryGenerateAmount, getCurrentProduction());
    }

    // endregion
    // endregion

    // region UI

    public void addDisplayText(@NotNull List<Component> textList) {
        IDisplayUIMachine.super.addDisplayText(textList);
        if (isFormed()) {
            NumberFormat formatter = new DecimalFormat("#0.0");
            textList.add(Component.translatable("gnt.multiblock.reactor.heat", formatter.format(temp + C_TO_K_OFFSET),
                    moderatorType.getMaxTemp() + C_TO_K_OFFSET));
            var powerText = Component.translatable("gnt.multiblock.reactor.powergen", getCurrentProduction())
                            .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    Component.literal(productionCap().toString()))));
            textList.add(powerText);

            var ctrlRodText = Component.translatable("gnt.multiblock.reactor.rods",
                    ChatFormatting.AQUA.toString() + getControlRods() + "%")
                    .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                            Component.translatable("gnt.multiblock.reactor.rods.tooltip"))));
            textList.add(ctrlRodText);

            var buttonText = Component.translatable("gnt.multiblock.reactor.rods_modify");
            buttonText.append(" ");
            buttonText.append(ComponentPanelWidget.withButton(Component.literal("[-]"), "sub"));
            buttonText.append(" ");
            buttonText.append(ComponentPanelWidget.withButton(Component.literal("[+]"), "add"));
            textList.add(buttonText);

            textList.add(ComponentPanelWidget.withButton(
                    Component.literal(ChatFormatting.RED.toString())
                            .append(Component.translatable("gnt.multiblock.reactor.scram")),
                    "scram"));

            var rotorHolder = getRotorHolder();
            if (rotorHolder != null && rotorHolder.getRotorEfficiency() > 0) {
                textList.add(Component.translatable("gtceu.multiblock.turbine.rotor_speed",
                        FormattingUtil.formatNumbers(rotorHolder.getRotorSpeed()),
                        FormattingUtil.formatNumbers(rotorHolder.getMaxRotorHolderSpeed())));
                textList.add(Component.translatable("gtceu.multiblock.turbine.efficiency",
                        rotorHolder.getTotalEfficiency()));

                int rotorDurability = rotorHolder.getRotorDurabilityPercent();
                var duraText = Component.translatable("gtceu.multiblock.turbine.rotor_durability", rotorDurability);
                textList.add((rotorDurability > MIN_DURABILITY_TO_WARN) ?
                        duraText :
                        duraText.setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
            }
        }
    }

    public void handleDisplayClick(String componentData, ClickData clickData) {
        if (!clickData.isRemote) {
            switch (componentData) {
                case "add":
                    this.controlRods = Mth.clamp(controlRods + 5, 0, 100);
                    break;
                case "sub":
                    this.controlRods = Mth.clamp(controlRods - 5, 0, 100);
                    break;
                case "scram":
                    scram();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Widget createUIWidget() {
        var group = new WidgetGroup(0, 0, 182 + 8, 117 + 8);
        // noinspection DataFlowIssue
        group.addWidget(new DraggableScrollableWidgetGroup(4, 4, 182, 117).setBackground(getScreenTexture())
                .addWidget(new LabelWidget(4, 5, self().getBlockState().getBlock().getDescriptionId()))
                .addWidget(new ComponentPanelWidget(4, 17, this::addDisplayText)
                        .textSupplier(this.getLevel().isClientSide ? null : this::addDisplayText)
                        .setMaxWidthLimit(200)
                        .clickHandler(this::handleDisplayClick)));
        group.setBackground(GuiTextures.BACKGROUND_INVERSE);
        return group;
    }

    @Override
    public ModularUI createUI(Player entityPlayer) {
        return new ModularUI(198, 208, this, entityPlayer).widget(new FancyMachineUIWidget(this, 198, 208));
    }

    @Override
    public List<IFancyUIProvider> getSubTabs() {
        return getParts().stream().filter(Objects::nonNull).map(IFancyUIProvider.class::cast).toList();
    }
    // endregion
}
