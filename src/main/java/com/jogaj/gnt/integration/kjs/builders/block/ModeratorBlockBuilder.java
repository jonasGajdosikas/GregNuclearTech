package com.jogaj.gnt.integration.kjs.builders.block;

import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.jogaj.gnt.api.GNTAPI;
import com.jogaj.gnt.api.block.CustomModeratorType;
import com.jogaj.gnt.common.block.ModeratorBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true, fluent = true)
public class ModeratorBlockBuilder extends BlockBuilder {

    @Setter
    public transient int maxTemp, heatCapacity;
    @Setter
    public transient double fastNeutronConversion;
    @Setter
    public transient String texture = "minecraft:missingno";

    public ModeratorBlockBuilder(ResourceLocation id) {
        super(id);
        property(GTBlockStateProperties.ACTIVE);
        renderType("translucent");
        opaque(false);
        noValidSpawns(true);
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        bs.simpleVariant("active=false", newID("block/", "").toString());
        bs.simpleVariant("active=true", newID("block/", "_active").toString());
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent("minecraft:block/cube_all");
            m.texture("all", texture);
        });
        generator.blockModel(id.withSuffix("_active"), m -> {
            m.parent("gnt:block/cube_2_layer/all");
            m.texture("bot_all", texture);
            m.texture("top_all", texture + "_bloom");
        });
    }

    BlockBehaviour.Properties createModeratorProperties() {
        BlockBehaviour.Properties properties = createProperties();
        properties.noOcclusion();

        return properties;
    }

    @Override
    public Block createObject() {
        @SuppressWarnings("removal")
        CustomModeratorType moderatorType = new CustomModeratorType(this.id.getPath(), fastNeutronConversion,
                maxTemp, heatCapacity, new ResourceLocation(texture));
        ModeratorBlock result = new ModeratorBlock(createModeratorProperties(), moderatorType);
        GNTAPI.MODERATORS.put(moderatorType, () -> result);
        return result;
    }
}
