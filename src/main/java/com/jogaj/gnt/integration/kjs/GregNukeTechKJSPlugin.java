package com.jogaj.gnt.integration.kjs;

import com.jogaj.gnt.GNT;
import com.jogaj.gnt.api.pattern.Predicates;
import com.jogaj.gnt.client.renderer.machine.DynamicRenderHelper;
import com.jogaj.gnt.common.data.GNTBlocks;
import com.jogaj.gnt.common.data.GNTRecipeModifiers;
import com.jogaj.gnt.common.machine.NuclearReactor;
import com.jogaj.gnt.integration.kjs.builders.block.ModeratorBlockBuilder;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class GregNukeTechKJSPlugin extends KubeJSPlugin {

    public @Override void initStartup() {
        super.initStartup();
    }

    public @Override void init() {
        super.init();

        RegistryInfo.BLOCK.addType("gnt:moderator", ModeratorBlockBuilder.class, ModeratorBlockBuilder::new);
    }

    public @Override void registerClasses(ScriptType type, ClassFilter filter) {
        super.registerClasses(type, filter);

        // allow all gnt classes by importing them
        filter.allow("com.jogaj.gnt");
    }

    public @Override void registerEvents() {
        super.registerEvents();
    }

    public @Override void registerBindings(BindingsEvent event) {
        super.registerBindings(event);
        event.add("GNT", GNT.class);
        event.add("GNTPredicates", Predicates.class);
        event.add("GNTBlocks", GNTBlocks.class);
        event.add("GNTModifiers", GNTRecipeModifiers.class);
        event.add("GNTDynamicRenderHelper", DynamicRenderHelper.class);

        event.add("Reactor", NuclearReactor.class);
    }
}
