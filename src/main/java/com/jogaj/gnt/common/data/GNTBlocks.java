package com.jogaj.gnt.common.data;

import com.gregtechceu.gtceu.common.data.models.GTModels;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

import com.jogaj.gnt.GNT;
import com.jogaj.gnt.api.GNTAPI;
import com.jogaj.gnt.api.block.IModeratorType;
import com.jogaj.gnt.common.block.ModeratorBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import java.util.function.Supplier;

import static com.jogaj.gnt.common.registry.GNTRegistration.REGISTRATE;

public class GNTBlocks {

    static {
        REGISTRATE.creativeModeTab(() -> GNT.GNT_CREATIVE_TAB);
    }
    // Moderator Blocks
    public static final BlockEntry<ModeratorBlock> MODERATOR_WATER = createModeratorBlock(
            ModeratorBlock.ModeratorType.WATER);

    public static final BlockEntry<Block> RADIATION_PROOF_CASING = createCasingBlock("radiation_proof_casing",
            GNT.resourceLocation("block/casing/machine/radiation_proof"));

    @SuppressWarnings("SameParameterValue")
    private static BlockEntry<ModeratorBlock> createModeratorBlock(IModeratorType moderatorType) {
        var moderatorBlock = REGISTRATE
                .block("%s_moderator_block".formatted(moderatorType.getName()),
                        p -> new ModeratorBlock(p, moderatorType))
                .initialProperties(() -> Blocks.GLASS)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::translucent)
                .blockstate(GNTModels.createModeratorModel(moderatorType))
                .item(BlockItem::new)
                .build()
                .register();
        GNTAPI.MODERATORS.put(moderatorType, moderatorBlock);
        return moderatorBlock;
    }

    public static BlockEntry<Block> createCasingBlock(String name, ResourceLocation texture) {
        return createCasingBlock(name, Block::new, texture, () -> Blocks.IRON_BLOCK, () -> RenderType::cutoutMipped);
    }

    public static BlockEntry<Block> createCasingBlock(String name,
                                                      NonNullFunction<BlockBehaviour.Properties, Block> blockSupplier,
                                                      ResourceLocation texture,
                                                      NonNullSupplier<? extends Block> properties,
                                                      Supplier<Supplier<RenderType>> type) {
        return REGISTRATE.block(name, blockSupplier)
                .initialProperties(properties)
                .properties(p -> p.isValidSpawn(((state, level, pos, ent) -> false)))
                .addLayer(type)
                .exBlockstate(GTModels.cubeAllModel(texture))
                .item(BlockItem::new)
                .build()
                .register();
    }

    public static void init() {}
}
