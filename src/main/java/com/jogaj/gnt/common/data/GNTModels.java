package com.jogaj.gnt.common.data;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.api.block.property.GTBlockStateProperties;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ModelFile;

import com.jogaj.gnt.GNT;
import com.jogaj.gnt.api.block.IModeratorType;
import com.jogaj.gnt.common.block.ModeratorBlock;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;

public class GNTModels {

    public static NonNullBiConsumer<DataGenContext<Block, ModeratorBlock>, RegistrateBlockstateProvider> createModeratorModel(
                                                                                                                              IModeratorType moderatorType) {
        return (ctx, prov) -> {
            String name = ctx.getName();
            ActiveBlock block = ctx.getEntry();
            ModelFile inactive = prov.models().cubeAll(name, moderatorType.getTexture());
            ModelFile active = prov.models()
                    .withExistingParent(name + "_active", GNT.resourceLocation("block/cube_2_layer/all"))
                    .texture("bot_all", moderatorType.getTexture())
                    .texture("top_all", moderatorType.getTexture().withSuffix("_bloom"));
            prov.getVariantBuilder(block)
                    .partialState().with(GTBlockStateProperties.ACTIVE, false).modelForState().modelFile(inactive)
                    .addModel()
                    .partialState().with(GTBlockStateProperties.ACTIVE, true).modelForState().modelFile(active)
                    .addModel();
        };
    }
}
