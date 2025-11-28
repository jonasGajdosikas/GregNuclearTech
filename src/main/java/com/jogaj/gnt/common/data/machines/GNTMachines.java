package com.jogaj.gnt.common.data.machines;

import com.gregtechceu.gtceu.api.GTValues;
import com.gregtechceu.gtceu.api.machine.MultiblockMachineDefinition;
import com.gregtechceu.gtceu.api.machine.multiblock.PartAbility;
import com.gregtechceu.gtceu.api.pattern.FactoryBlockPattern;
import com.gregtechceu.gtceu.api.pattern.Predicates;
import com.gregtechceu.gtceu.common.data.GTBlocks;
import com.gregtechceu.gtceu.common.data.GTMachines;
import com.jogaj.gnt.common.data.GNTBlocks;
import static com.jogaj.gnt.api.pattern.Predicates.*;

public class GNTMachines {
    public static final MultiblockMachineDefinition SMALL_REACTOR =
            MachineUtils.registerFissionReactor("small_fission_reactor", "Small Fission Reactor", GTValues.LuV,
                    GNTBlocks.RADIATION_PROOF_CASING, GTBlocks.CASING_TITANIUM_TURBINE,
                    definition -> FactoryBlockPattern.start()
                            .aisle("###CCCCC", "###CMMMC", "###CMMMC", "###CMMMC", "###CCCCC")
                            .aisle("TTTWCCCC", "TDTPPMMM", "TTTMMMMM", "###MMMMM", "###CCCCC")
                            .aisle("TTTCCCCC", "TGTMMRMM", "TTTMMRMM", "###MMRMM", "###CCCCC")
                            .aisle("TTTWCCCC", "TDTPPMMM", "TTTMMMMM", "###MMMMM", "###CCCCC")
                            .aisle("###CC@CC", "###CMMMC", "###CMMMC", "###CMMMC", "###CCCCC")
                            .where("@", Predicates.controller(Predicates.blocks(definition.get())))
                            .where("C", Predicates.blocks(GNTBlocks.RADIATION_PROOF_CASING.get()).setMinGlobalLimited(50)
                                    .or(Predicates.autoAbilities(definition.getRecipeTypes()))
                                    .or(Predicates.abilities(PartAbility.MAINTENANCE).setExactLimit(1)))
                            .where("T", Predicates.blocks(GTBlocks.CASING_TITANIUM_TURBINE.get()))
                            .where("W", Predicates.blocks(GNTBlocks.RADIATION_PROOF_CASING.get())
                                    .or(Predicates.blocks(GTMachines.RESERVOIR_HATCH.get()).setExactLimit(1)))
                            .where("G", Predicates.blocks(GTBlocks.CASING_TITANIUM_GEARBOX.get()))
                            .where("D", Predicates.abilities(PartAbility.OUTPUT_ENERGY).setExactLimit(1)
                                    .or(rotorHolderPredicate()))
                            .where("M", moderators())
                            .where("R", Predicates.blocks(GTBlocks.HERMETIC_CASING_IV.get()))
                            .where("P", Predicates.blocks(GTBlocks.CASING_TUNGSTENSTEEL_PIPE.get()))
                            .where("#", Predicates.any())
                            .build());
    public static void init() {}
}
