package com.jogaj.gnt.client.renderer.machine;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;

import net.minecraft.world.level.block.Block;

import com.jogaj.gnt.client.renderer.machine.impl.ReactorPartRenderer;

import java.util.function.Supplier;

public class DynamicRenderHelper {

    public static DynamicRender<?, ?> makeReactorTurbineRender(Supplier<? extends Block> casing,
                                                               Supplier<? extends Block> turbineCasing) {
        return new ReactorPartRenderer(casing, turbineCasing);
    }
}
