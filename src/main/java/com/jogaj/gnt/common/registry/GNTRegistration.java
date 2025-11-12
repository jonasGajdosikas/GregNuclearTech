package com.jogaj.gnt.common.registry;

import com.gregtechceu.gtceu.api.registry.registrate.GTRegistrate;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;

import com.jogaj.gnt.GNT;

public class GNTRegistration {

    public static final GTRegistrate REGISTRATE = GTRegistrate.create(GNT.MOD_ID);
    static {
        REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
    }
}
