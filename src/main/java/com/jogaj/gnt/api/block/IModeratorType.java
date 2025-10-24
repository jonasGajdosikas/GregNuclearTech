package com.jogaj.gnt.api.block;

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface IModeratorType {
    /**
    * @return The unique name of the moderator block
    * */
    @NotNull
    String getName();

    /**
     * @return The factor of fast to thermal neutron conversion
     * */
    double getFastNeutronConversion();

    /**
     * @return The max temperature the moderator can safely reach
     */
    int getMaxTemp();

    /**
     * @return The {@link ResourceLocation} with the base texture of the moderator block
     */
    ResourceLocation getTexture();
}
