package com.jogaj.gnt.config;

import com.jogaj.gnt.GNT;
import dev.toma.configuration.Configuration;
import dev.toma.configuration.config.Config;
import dev.toma.configuration.config.ConfigHolder;
import dev.toma.configuration.config.Configurable;
import dev.toma.configuration.config.format.ConfigFormats;

@Config(id = GNT.MOD_ID)
public class GNTConfig {

    public static GNTConfig INSTANCE;
    public static ConfigHolder<GNTConfig> CONFIG_HOLDER;

    public static void init() {
        CONFIG_HOLDER = Configuration.registerConfig(GNTConfig.class, ConfigFormats.yaml());
    }

    @Configurable
    public ValueConfigs values = new ValueConfigs();

    public static class ValueConfigs {
        @Configurable.Comment("If the fission reactor should scram automatically when reaching critical heat")
        public @Configurable boolean scramBeforeOverheat = false;
    }
}
