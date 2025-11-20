package com.jogaj.gnt.integration.kjs.recipe;

import com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema;

import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import lombok.experimental.Accessors;

import static com.gregtechceu.gtceu.integration.kjs.recipe.GTRecipeSchema.*;

public class GNTRecipeSchema {

    @SuppressWarnings({ "unused", "UnusedReturnValue" })
    @Accessors(chain = true, fluent = true)
    class GNTRecipeJs extends GTRecipeSchema.GTRecipeJS {

        public GTRecipeSchema.GTRecipeJS thermalNeutrons(int n) {
            this.addData("thermalNeutrons", n);
            return this;
        }

        public GTRecipeSchema.GTRecipeJS fastNeutrons(int n) {
            this.addData("fastNeutrons", n);
            return this;
        }
    }

    RecipeSchema SCHEMA = new RecipeSchema(GNTRecipeJs.class, GNTRecipeJs::new, DURATION, DATA, CONDITIONS,
            ALL_INPUTS, ALL_TICK_INPUTS, ALL_OUTPUTS, ALL_TICK_OUTPUTS,
            INPUT_CHANCE_LOGICS, OUTPUT_CHANCE_LOGICS, TICK_INPUT_CHANCE_LOGICS, TICK_OUTPUT_CHANCE_LOGICS,
            CATEGORY)
            .constructor(
                    (recipe, schemaType, keys, from) -> recipe.id(from.getValue(recipe, ID)), ID)
            .constructor(
                    DURATION, CONDITIONS, ALL_INPUTS, ALL_OUTPUTS, ALL_TICK_INPUTS, ALL_TICK_OUTPUTS);
}
