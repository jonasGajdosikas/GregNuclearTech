package com.jogaj.gnt.client;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;
import com.gregtechceu.gtceu.common.CommonProxy;

import net.minecraftforge.eventbus.api.IEventBus;

import com.jogaj.gnt.GNT;
import com.jogaj.gnt.client.renderer.machine.impl.ReactorPartRenderer;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {}

    public static void init(IEventBus modBus) {
        modBus.register(ClientProxy.class);

        DynamicRenderManager.register(GNT.resourceLocation("reactor_turbine"), ReactorPartRenderer.TYPE);
    }
}
