package com.jogaj.gnt.common.data.machines;

import com.gregtechceu.gtceu.GTCEu;
import com.gregtechceu.gtceu.api.data.RotationState;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.pattern.BlockPattern;
import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;
import com.gregtechceu.gtceu.common.data.models.GTMachineModels;
import com.gregtechceu.gtceu.common.machine.multiblock.part.EnergyHatchPartMachine;
import com.jogaj.gnt.GNT;
import com.jogaj.gnt.common.data.GNTRecipeTypes;
import com.jogaj.gnt.common.machine.NuclearReactor;
import com.jogaj.gnt.client.renderer.machine.DynamicRenderHelper;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.world.level.block.Block;

import static com.jogaj.gnt.common.registry.GNTRegistration.REGISTRATE;

import java.util.function.Function;

public class MachineUtils {
    public static MultiblockMachineDefinition registerFissionReactor(String name, String lang, int tier,
                                                                     BlockEntry<Block> casing, BlockEntry<Block> turbineCasing,
                                                                     Function<MultiblockMachineDefinition, BlockPattern> patternFunction) {
        return registerFissionReactor(REGISTRATE, name, lang, tier, casing, turbineCasing, patternFunction);

    }

    public static MultiblockMachineDefinition registerFissionReactor(
            GTRegistrate registrate, String name, String lang, int tier,
            BlockEntry<Block> casing, BlockEntry<Block> turbineCasing,
            Function<MultiblockMachineDefinition, BlockPattern> patternFunction
            ){
        return registrate
                .multiblock(name, holder -> new NuclearReactor(holder, tier))
                .langValue(lang)
                .allowExtendedFacing(false)
                .rotationState(RotationState.NON_Y_AXIS)
                .recipeType(GNTRecipeTypes.FISSION_RECIPES)
                .appearanceBlock(casing)
                .partAppearance(
                        (controller, part, side) ->
                    ((part instanceof EnergyHatchPartMachine)
                            ? turbineCasing
                            : casing).get().defaultBlockState()
                )
                .pattern(patternFunction)
                .model(GTMachineModels.createWorkableCasingMachineModel(
                                GNT.resourceLocation("block/casing/machine/radiation_proof"),
                                GTCEu.id("block/multiblock/implosion_compressor"))
                        .andThen(b -> b.addDynamicRenderer(() ->
                                DynamicRenderHelper.makeReactorTurbineRender(
                                        casing, turbineCasing)))
                )
                .register();
    }
}
