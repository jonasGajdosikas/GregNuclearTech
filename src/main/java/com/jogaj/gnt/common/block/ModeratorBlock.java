package com.jogaj.gnt.common.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.jogaj.gnt.GNT;
import com.jogaj.gnt.api.block.IModeratorType;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
public class ModeratorBlock extends ActiveBlock {
    public IModeratorType moderatorType;
    public ModeratorBlock(Properties properties, IModeratorType moderatorType) {
        super(properties);
        this.moderatorType = moderatorType;
    }

    // Make the block render like glass
    // disable inner faces, make it transparent to sky light
    @Override @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getVisualShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @ParametersAreNonnullByDefault
    public @Override float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0F;
    }

    @ParametersAreNonnullByDefault
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

//    @Override
//    public boolean skipRendering(BlockState state, BlockState adjacentBlockState, Direction side) {
//        return adjacentBlockState.is(this) || super.skipRendering(state, adjacentBlockState, side);
//    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (GTUtil.isShiftDown()){
            int conversion = (int)(moderatorType.getFastNeutronConversion() * 100);
            int maxTemp = moderatorType.getMaxTemp();
            tooltip.add(Component.translatable("gnt.block.moderator.tooltip_conversion", conversion));
            tooltip.add(Component.translatable("gnt.block.moderator.tooltip_max_temp", maxTemp ));
        } else {
            tooltip.add(Component.translatable("gnt.block.moderator.tooltip_extended_info"));
        }
    }

    @Getter
    public enum ModeratorType implements StringRepresentable, IModeratorType{
        WATER("water", .1, 700, 100 , GNT.resourceLocation("block/casing/moderator/water"));

        private @Getter final String name;
        private @Getter final double fastNeutronConversion;
        private @Getter final int maxTemp;
        private @Getter final int heatCapacity;
        private @Getter final @NotNull ResourceLocation texture;

        ModeratorType(String name, double fastNeutronConversion, int maxTemp, int heatCapacity, @NotNull ResourceLocation texture) {
            this.name = name;
            this.fastNeutronConversion = fastNeutronConversion;
            this.maxTemp = maxTemp;
            this.heatCapacity = heatCapacity;
            this.texture = texture;
        }

        @Override
        public @NotNull String toString(){
            return getName();
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }
    }
}
