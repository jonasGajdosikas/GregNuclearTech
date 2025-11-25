package com.jogaj.gnt.common.data;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.lowdragmc.lowdraglib.utils.LocalizationUtils;

import static com.gregtechceu.gtceu.common.data.GTRecipeTypes.*;
import static com.jogaj.gnt.api.gui.GuiTextures.PROGRESS_BAR_FISSION;
import static com.lowdragmc.lowdraglib.gui.texture.ProgressTexture.FillDirection.LEFT_TO_RIGHT;

public class GNTRecipeTypes {

    public static void init() {}

    public static final GTRecipeType FISSION_RECIPES = register("fission_reactor", MULTIBLOCK)
            .setMaxIOSize(2, 2, 1, 1)
            .setProgressBar(PROGRESS_BAR_FISSION, LEFT_TO_RIGHT)
            .setSlotOverlay(false, false, GuiTextures.ATOMIC_OVERLAY_1)
            .setSlotOverlay(false, true, GuiTextures.ATOMIC_OVERLAY_2)
            .setSlotOverlay(true, false, GuiTextures.ATOMIC_OVERLAY_1)
            .setSlotOverlay(true, true, GuiTextures.ATOMIC_OVERLAY_2)
            .addDataInfo(data -> {
                int thermal = data.getInt("thermalNeutrons");
                int fast = data.getInt("fastNeutrons");
                return LocalizationUtils.format("gnt.recipe.neutrons", FormattingUtil.formatNumbers(thermal),
                        FormattingUtil.formatNumbers(fast));
            })
            .setSound(GTSoundEntries.COOLING);
}
