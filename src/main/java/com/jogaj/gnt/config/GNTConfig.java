package com.jogaj.gnt.config;

import com.jogaj.gnt.GNT;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GNT.MOD_ID)
public class GNTConfig {

    public static GNTConfig INSTANCE;
    private static final Object LOCK = new Object();

    public static void init() {
        synchronized (LOCK){
            if(INSTANCE == null)
                INSTANCE = Configuration.registerConfig(GNTConfig.class, ConfigFormats.yaml()).getConfigInstance();
        }
    }

    @Configurable
    public FissionConfigs values = new FissionConfigs();

    public static class FissionConfigs {

        @Configurable.Comment("If the fission reactor should scram automatically when reaching critical heat")
        public @Configurable boolean scramBeforeOverheat = false;
    }
}
