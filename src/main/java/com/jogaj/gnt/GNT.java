package com.jogaj.gnt;

import com.gregtechceu.gtceu.api.GTCEuAPI;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.MaterialRegistryEvent;
import com.gregtechceu.gtceu.api.data.chemical.material.event.PostMaterialEvent;
import com.gregtechceu.gtceu.api.machine.MachineDefinition;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.api.sound.SoundEntry;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.jogaj.gnt.common.GNTItems;
import com.jogaj.gnt.common.data.GNTBlocks;
import com.jogaj.gnt.common.data.machines.GNTMachines;
import com.lowdragmc.lowdraglib.Platform;

import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import com.jogaj.gnt.client.ClientProxy;
import com.jogaj.gnt.common.data.GNTDatagen;
import com.jogaj.gnt.common.data.GNTRecipeTypes;
import com.jogaj.gnt.common.registry.GNTRegistration;
import static com.jogaj.gnt.common.registry.GNTRegistration.REGISTRATE;
import com.jogaj.gnt.config.GNTConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(GNT.MOD_ID)
@SuppressWarnings("removal")
public class GNT {

    public static final String MOD_ID = "gnt", NAME = "GregNukeTech";
    public static final Logger LOGGER = LogManager.getLogger();

    public static RegistryEntry<CreativeModeTab> GNT_CREATIVE_TAB = REGISTRATE
            .defaultCreativeTab(MOD_ID,
                    builder -> builder
                            .displayItems(new GTCreativeModeTabs.RegistrateDisplayItemsGenerator(MOD_ID, REGISTRATE))
                            .title(REGISTRATE.addLang("itemGroup", GNT.resourceLocation("creative_tab"), "Greg Nuclear Technology"))
                            .icon(GNTBlocks.MODERATOR_WATER::asStack)
                            .build())
            .register();

    public GNT() {
        GNT.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);

        modEventBus.addListener(this::addMaterialRegistries);
        modEventBus.addListener(this::addMaterials);
        modEventBus.addListener(this::modifyMaterials);

        modEventBus.addGenericListener(GTRecipeType.class, this::registerRecipeTypes);
        modEventBus.addGenericListener(MachineDefinition.class, this::registerMachines);
        modEventBus.addGenericListener(SoundEntry.class, this::registerSounds);

        // Most other events are fired on Forge's bus.
        // If we want to use annotations to register event listeners,
        // we need to register our object like this!
        MinecraftForge.EVENT_BUS.register(this);

        GNTRegistration.REGISTRATE.registerRegistrate();

        if (Platform.isClient()) {
            ClientProxy.init(modEventBus);
        }
    }

    public static void init() {
        LOGGER.info("running gnt init");
        GNTConfig.init();
        GNTDatagen.init();
        GNTItems.init();
        //GNTMachines.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {});
    }

    private void clientSetup(final FMLClientSetupEvent event) {}

    /**
     * Create a ResourceLocation in the format "gnt:path"
     *
     * @param path the resourceLocation
     * @return ResourceLocation with the namespace of your mod
     */
    public static ResourceLocation resourceLocation(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    /**
     * Create a material manager for your mod using GT's API.
     * You MUST have this if you have custom materials.
     * Remember to register them not to GT's namespace, but your own.
     * 
     * @param event event
     */
    private void addMaterialRegistries(MaterialRegistryEvent event) {
        GTCEuAPI.materialManager.createRegistry(GNT.MOD_ID);
    }

    /**
     * You will also need this for registering custom materials
     * Call init() from your Material class(es) here
     * 
     * @param event event
     */
    private void addMaterials(MaterialEvent event) {
        // CustomMaterials.init();
    }

    /**
     * (Optional) Used to modify pre-existing materials from GregTech
     * 
     * @param event event
     */
    private void modifyMaterials(PostMaterialEvent event) {
        // CustomMaterials.modify();
    }

    /**
     * Used to register your own new RecipeTypes.
     * Call init() from your RecipeType class(es) here
     * 
     * @param event event
     */
    private void registerRecipeTypes(GTCEuAPI.RegisterEvent<ResourceLocation, GTRecipeType> event) {
        GNTRecipeTypes.init();
    }

    /**
     * Used to register your own new machines.
     * Call init() from your Machine class(es) here
     * 
     * @param event event
     */
    private void registerMachines(GTCEuAPI.RegisterEvent<ResourceLocation, MachineDefinition> event) {
        GNTMachines.init();
    }

    /**
     * Used to register your own new sounds
     * Call init from your Sound class(es) here
     * 
     * @param event event
     */
    public void registerSounds(GTCEuAPI.RegisterEvent<ResourceLocation, SoundEntry> event) {
        // CustomSounds.init();
    }
}
