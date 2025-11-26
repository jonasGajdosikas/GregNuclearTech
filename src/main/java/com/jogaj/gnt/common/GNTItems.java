package com.jogaj.gnt.common;

import com.jogaj.gnt.GNT;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.world.item.Item;

import static com.jogaj.gnt.common.registry.GNTRegistration.REGISTRATE;

public class GNTItems {
    static {
        REGISTRATE.creativeModeTab(() -> GNT.GNT_CREATIVE_TAB);
    }
    public static void init(){

    }

    public static final ItemEntry<Item> THERMAL_NEUTRON = REGISTRATE
            .item("thermal_neutron", Item::new)
            .lang("Thermal Neutron")
            .register();
    public static final ItemEntry<Item> FAST_NEUTRON = REGISTRATE
            .item("fast_neutron", Item::new)
            .lang("Fast Neutron")
            .register();
}
