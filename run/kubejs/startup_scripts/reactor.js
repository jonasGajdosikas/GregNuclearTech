//let $GTMachineUtils = Java.loadClass("com.gregtechceu.gtceu.common.data.machines.GTMachineUtils")
let $GNTMachineUtils = Java.loadClass("com.jogaj.gnt.common.data.machines.MachineUtils")

GTCEuStartupEvents.registry('gtceu:machine', event => {

/*

    $GNTMachineUtils.registerFissionReactor(
        "small_fission_reactor", "Small Fission Reactor", GTValues.LuV,
        definition => FactoryBlockPattern.start()
                    .aisle('###CCCCC', '###CMMMC', '###CMMMC', '###CMMMC', '###CCCCC')
                    .aisle('TTTWCCCC', 'TDTPPMMM', 'TTTMMMMM', '###MMMMM', '###CCCCC')
                    .aisle('TTTCCCCC', 'TGTMMRMM', 'TTTMMRMM', '###MMRMM', '###CCCCC')
                    .aisle('TTTWCCCC', 'TDTPPMMM', 'TTTMMMMM', '###MMMMM', '###CCCCC')
                    .aisle('###CC@CC', '###CMMMC', '###CMMMC', '###CMMMC', '###CCCCC')
                    .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                    .where("C", Predicates.blocks(GNTBlocks.RADIATION_PROOF_CASING.get()).setMinGlobalLimited(50)
                        .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                        .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                    .where("T", Predicates.blocks(GTBlocks.CASING_TITANIUM_TURBINE.get()))
                    .where("W", Predicates.blocks(GNTBlocks.RADIATION_PROOF_CASING.get())
                        .or(Predicates.blocks(GTMachines.RESERVOIR_HATCH).setExactLimit(1)))
                    .where("G", Predicates.blocks(GTBlocks.CASING_TITANIUM_GEARBOX.get()))
                    .where("D", Predicates.abilities(PartAbility.OUTPUT_ENERGY).setExactLimit(1)
                        .or(GNTPredicates.rotorHolderPredicate()))
                    .where("M", GNTPredicates.moderators())
                    .where("R", Predicates.blocks(GTBlocks.HERMETIC_CASING_IV.get()))
                    .where("P", Predicates.blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                    .where("#", Predicates.any())
                    .build()
    )


/*
    event.create('small_fission_reactor', 'multiblock')
        .machine((holder) => new Reactor(holder, 7))
        .rotationState(RotationState.NON_Y_AXIS)
        .recipeType(GNTRecipeTypes.FISSION_RECIPES)
        .appearanceBlock(GNTBlocks.RADIATION_PROOF_CASING)
        .partAppearance((controller, part, side) =>
            ((part instanceof EnergyHatchPartMachine)
                ? GTBlocks.CASING_TITANIUM_TURBINE
                : GNTBlocks.RADIATION_PROOF_CASING).get().defaultBlockState()
        )
        .pattern(definition => FactoryBlockPattern.start()
            .aisle('###CCCCC', '###CMMMC', '###CMMMC', '###CMMMC', '###CCCCC')
            .aisle('TTTWCCCC', 'TDTPPMMM', 'TTTMMMMM', '###MMMMM', '###CCCCC')
            .aisle('TTTCCCCC', 'TGTMMRMM', 'TTTMMRMM', '###MMRMM', '###CCCCC')
            .aisle('TTTWCCCC', 'TDTPPMMM', 'TTTMMMMM', '###MMMMM', '###CCCCC')
            .aisle('###CC@CC', '###CMMMC', '###CMMMC', '###CMMMC', '###CCCCC')
            .where("@", Predicates.controller(Predicates.blocks(definition.get())))
            .where("C", Predicates.blocks(GNTBlocks.RADIATION_PROOF_CASING.get()).setMinGlobalLimited(50)
                .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
            .where("T", Predicates.blocks(GTBlocks.CASING_TITANIUM_TURBINE.get()))
            .where("W", Predicates.blocks(GNTBlocks.RADIATION_PROOF_CASING.get())
                .or(Predicates.blocks(GTMachines.RESERVOIR_HATCH).setExactLimit(1)))
            .where("G", Predicates.blocks(GTBlocks.CASING_TITANIUM_GEARBOX.get()))
            .where("D", Predicates.abilities(PartAbility.OUTPUT_ENERGY).setExactLimit(1)
                .or(GNTPredicates.rotorHolderPredicate()))
            .where("M", GNTPredicates.moderators())
            .where("R", Predicates.blocks(GTBlocks.HERMETIC_CASING_IV.get()))
            .where("P", Predicates.blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
            .where("#", Predicates.any())
            .build()
        )
        .model(GTMachineModels.createWorkableCasingMachineModel(
            GNT.resourceLocation("block/casing/machine/radiation_proof"),
            GTCEu.id("block/multiblock/implosion_compressor"))
                //["andThen(java.util.function.Consumer)"](b => b.addDynamicRenderer(() => GNTDynamicRenderHelper.makeReactorTurbineRender(GNTBlocks.RADIATION_PROOF_CASING, GTBlocks.CASING_TITANIUM_TURBINE)))
        ) /**/

//    $GTMachineUtils.registerLargeTurbine(
//        "nuclear_large_turbine",
//        GTValues.LuV,
//        GTRecipeTypes.STEAM_TURBINE_FUELS,
//        GTBlocks.CASING_TITANIUM_TURBINE, GTBlocks.CASING_TITANIUM_GEARBOX,
//        GTCEu.id("block/casings/mechanic/machine_casing_turbine_titanium"),
//        GTCEu.id("block/multiblock/generator/large_steam_turbine"),
//        false
//    );

})