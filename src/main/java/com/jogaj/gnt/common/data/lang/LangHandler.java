package com.jogaj.gnt.common.data.lang;

import com.jogaj.gnt.GNT;
import com.tterrag.registrate.providers.RegistrateLangProvider;

public class LangHandler extends com.gregtechceu.gtceu.data.lang.LangHandler {

    public static void init(RegistrateLangProvider provider) {
        GNT.LOGGER.info("generating lang");

        replace(provider, "gnt.multiblock.reactor.heat", "Heat %sK / %sK");
        replace(provider, "gnt.multiblock.reactor.powergen", "Power generation %s EU");
        replace(provider, "gnt.multiblock.reactor.rods", "Rods are %s in");
        replace(provider, "gnt.multiblock.reactor.rods.tooltip", "Insert control rods deeper to slow down fission");
        replace(provider, "gnt.multiblock.reactor.rods_modify", "Change depth");
        replace(provider, "gnt.multiblock.reactor.scram", "[SCRAM]");

        replace(provider, "gnt.multiblock.pattern.error.moderator", "§cAll moderators must be the same§r");

        replace(provider, "gnt.block.moderator.tooltip_extended_info", "§7Hold SHIFT to show Moderator Bonus Info");
        replace(provider, "gnt.block.moderator.tooltip_conversion",
                "Converts %s%% of fast neutrons into thermal neutrons");
        replace(provider, "gnt.block.moderator.tooltip_max_temp", "Maximum safe temperature %s");

        replace(provider, "gnt.recipe.neutrons", "%s Thermal neutrons, %s Fast neutrons");

        // replace(provider, "block.gnt.water_moderator_block", "Light Water Moderator Block");
    }
}
