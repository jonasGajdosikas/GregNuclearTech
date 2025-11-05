package com.jogaj.gnt.common.data.lang;

import com.jogaj.gnt.GNT;
import com.tterrag.registrate.providers.RegistrateLangProvider;

public class LangHandler extends com.gregtechceu.gtceu.data.lang.LangHandler {
    public static void init(RegistrateLangProvider provider){
        GNT.LOGGER.info("generating lang");

        provider.add("gnt.multiblock.reactor.heat", "Heat %sK / %sK");
        provider.add("gnt.multiblock.reactor.powergen", "Potential power generation %s EU");
        provider.add("gnt.multiblock.reactor.rods", "Rods are %s in");
        provider.add("gnt.multiblock.reactor.rods.tooltip", "Insert control rods deeper to slow down the fission");
        provider.add("gnt.multiblock.reactor.rods_modify", "Change control rode height");
        provider.add("gnt.multiblock.reactor.scram", "[SCRAM]");



        provider.add("gnt.block.moderator.tooltip_extended_info", "§7Hold SHIFT to show Moderator Bonus Info");
        provider.add("gnt.block.moderator.tooltip_conversion", "Converts %s%% of fast neutrons into thermal neutrons");
        provider.add("gnt.block.moderator.tooltip_max_temp", "Maximum safe temperature %s");


        provider.add("block.gnt.water_moderator_block", "Water Moderator Block");
    }
}
