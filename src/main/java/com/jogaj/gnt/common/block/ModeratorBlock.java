package com.jogaj.gnt.common.block;

import com.gregtechceu.gtceu.api.block.ActiveBlock;
import com.gregtechceu.gtceu.utils.GTUtil;
import com.jogaj.gnt.GNT;
import com.jogaj.gnt.api.block.IModeratorType;
import lombok.Getter;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ModeratorBlock extends ActiveBlock {
    public IModeratorType moderatorType;
    public ModeratorBlock(Properties properties, IModeratorType moderatorType) {
        super(properties);
        this.moderatorType = moderatorType;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void appendHoverText(ItemStack stack, @Nullable BlockGetter level, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, level, tooltip, flag);
        if (GTUtil.isShiftDown()){
            int conversion = (int)(moderatorType.getFastNeutronConversion() * 100);
            int maxTemp = moderatorType.getMaxTemp();
            tooltip.add(Component.translatable("block.gnt.moderator.tooltip_conversion", conversion));
            tooltip.add(Component.translatable("block.gnt.moderator.tooltip_max_temp", maxTemp));
        } else {
            tooltip.add(Component.translatable("block.gnt.moderator.tooltip_extended_info"));
        }
    }

    @Getter
    public enum ModeratorType implements StringRepresentable, IModeratorType{
        WATER("water", .1, 700, GNT.resourceLocation("block/casing/moderator/water"));

        private final String name;
        private final double fastNeutronConversion;
        private final int maxTemp;
        private final @NotNull ResourceLocation texture;

        ModeratorType(String name, double fastNeutronConversion, int maxTemp, @NotNull ResourceLocation texture) {
            this.name = name;
            this.fastNeutronConversion = fastNeutronConversion;
            this.maxTemp = maxTemp;
            this.texture = texture;
        }

        @Override
        public double getFastNeutronConversion() {
            return fastNeutronConversion;
        }

        @Override
        public int getMaxTemp() {
            return maxTemp;
        }

        @Override
        public @NotNull ResourceLocation getTexture() {
            return texture;
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
