package com.jogaj.gnt.common.data;

import com.gregtechceu.gtceu.common.data.GTCreativeModeTabs;
import com.jogaj.gnt.api.GNTAPI;
import com.jogaj.gnt.api.block.IModeratorType;
import com.jogaj.gnt.common.block.ModeratorBlock;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Blocks;

import static com.jogaj.gnt.GNT.REGISTRATE;

public class GNTBlocks {
    static {
        REGISTRATE.creativeModeTab(() -> GTCreativeModeTabs.DECORATION);
    }
    // Moderator Blocks
    public static final BlockEntry<ModeratorBlock> MODERATOR_WATER = createModeratorBlock(ModeratorBlock.ModeratorType.WATER);




    private static BlockEntry<ModeratorBlock> createModeratorBlock(IModeratorType moderatorType){
        var moderatorBlock = REGISTRATE
                .block("%s_moderator_block".formatted(moderatorType.getName()), p -> new ModeratorBlock(p, moderatorType))
                .initialProperties(() -> Blocks.GLASS)
                .properties(p -> p.isValidSpawn((state, level, pos, ent) -> false))
                .addLayer(() -> RenderType::cutoutMipped)
                .blockstate(GNTModels.createModeratorModel(moderatorType))
                .item(BlockItem::new)
                .build()
                .register();
        GNTAPI.MODERATORS.put(moderatorType, moderatorBlock);
        return moderatorBlock;
    }

    public static void init(){

    }
}
