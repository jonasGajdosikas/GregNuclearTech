package com.jogaj.gnt.api.block;

import lombok.Getter;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public class CustomModeratorType implements IModeratorType, StringRepresentable {
    private @Getter final String name;
    private @Getter final double fastNeutronConversion;
    private @Getter final int maxTemp;
    private @Getter final int heatCapacity;
    private @Getter final @NotNull ResourceLocation texture;

    public CustomModeratorType(String name, double fastNeutronConversion, int maxTemp, int heatCapacity, @NotNull ResourceLocation texture) {
        this.name = name;
        this.fastNeutronConversion = fastNeutronConversion;
        this.maxTemp = maxTemp;
        this.heatCapacity = heatCapacity;
        this.texture = texture;
    }

    public @Override @NotNull String getSerializedName() { return name; }
    public @Override @NotNull String toString() { return getName(); }
}
