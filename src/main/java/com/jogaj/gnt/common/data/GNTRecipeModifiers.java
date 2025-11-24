package com.jogaj.gnt.common.data;

import com.gregtechceu.gtceu.api.recipe.modifier.RecipeModifier;

import com.jogaj.gnt.common.machine.NuclearReactor;

public class GNTRecipeModifiers {

    public static RecipeModifier CONTROL_RODS = NuclearReactor::ctrlRodModifier;
}
