package com.jogaj.gnt.client.renderer.machine;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRender;
import com.jogaj.gnt.client.renderer.machine.impl.ReactorPartRenderer;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class DynamicRenderHelper {
    public static DynamicRender<?, ?> makeReactorTurbineRender(Supplier<? extends Block> casing, Supplier<? extends Block> turbineCasing){
        return new ReactorPartRenderer(casing, turbineCasing);
    }
}
