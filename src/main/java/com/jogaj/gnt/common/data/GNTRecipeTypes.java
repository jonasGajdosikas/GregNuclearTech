package com.jogaj.gnt.common.data;

import com.gregtechceu.gtceu.api.gui.GuiTextures;
import com.gregtechceu.gtceu.api.gui.widget.SlotWidget;
import com.gregtechceu.gtceu.api.recipe.GTRecipeType;
import com.gregtechceu.gtceu.common.data.GTSoundEntries;
import com.gregtechceu.gtceu.integration.xei.entry.item.ItemHolderSetList;
import com.gregtechceu.gtceu.integration.xei.handlers.item.CycleItemStackHandler;
import com.gregtechceu.gtceu.utils.FormattingUtil;

import com.jogaj.gnt.common.GNTItems;
import com.lowdragmc.lowdraglib.utils.LocalizationUtils;
import net.minecraft.world.item.ItemStack;

import java.util.Collections;
import java.util.List;

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
//            .addDataInfo(data -> {
//                int thermal = data.getInt("thermalNeutrons");
//                int fast = data.getInt("fastNeutrons");
//                return LocalizationUtils.format("gnt.recipe.neutrons", FormattingUtil.formatNumbers(thermal),
//                        FormattingUtil.formatNumbers(fast));
//            })
            .setUiBuilder((recipe, widgetGroup) -> {
                int thermal = recipe.data.getInt("thermalNeutrons");
                int fast = recipe.data.getInt("fastNeutrons");
                var thermalStack = collection(new ItemStack(GNTItems.THERMAL_NEUTRON, thermal));
                var fastStack = collection(new ItemStack(GNTItems.FAST_NEUTRON, fast));
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(thermalStack), 0,
                        widgetGroup.getSize().width - 65, widgetGroup.getSize().height - 30,
                        false, false));
                widgetGroup.addWidget(new SlotWidget(new CycleItemStackHandler(fastStack), 0,
                        widgetGroup.getSize().width - 45, widgetGroup.getSize().height - 30,
                        false, false));
            })
            .setSound(GTSoundEntries.COOLING);


    private static List<List<ItemStack>> collection(ItemStack stack){
        return Collections.singletonList(Collections.singletonList(stack));
    }
}
