package com.jogaj.gnt.client;

import com.gregtechceu.gtceu.client.renderer.machine.DynamicRenderManager;
import com.gregtechceu.gtceu.common.CommonProxy;

import com.jogaj.gnt.GNT;
import com.jogaj.gnt.client.renderer.machine.impl.ReactorPartRenderer;

public class ClientProxy extends CommonProxy {

    public ClientProxy() {
        super();
        init();
    }

    public static void init() {
        initializeDynamicRenderers();
    }

    private static void initializeDynamicRenderers() {
        DynamicRenderManager.register(GNT.resourceLocation("reactor_turbine"), ReactorPartRenderer.TYPE);
    }
}
