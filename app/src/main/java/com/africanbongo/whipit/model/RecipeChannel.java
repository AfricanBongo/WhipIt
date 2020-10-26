package com.africanbongo.whipit.model;

import androidx.annotation.NonNull;

import com.africanbongo.whipit.model.interfaces.Recipe;
import com.africanbongo.whipit.model.myrecipe.MyRecipe;

/*
A singleton class used to transfer recipes across activities and fragments
Used when opening the recipe in a detail activity and sending it back
 */

public class RecipeChannel {
    private Recipe currentRecipe;
    private static RecipeChannel recipeChannel;

    private RecipeChannel() {
        // Do nothing
    }

    public static RecipeChannel getRecipeChannel() {
        if (recipeChannel == null) {
            recipeChannel = new RecipeChannel();
        }
        return recipeChannel;
    }

    // Put a recipe into the channel
    public void putRecipe(@NonNull Recipe recipe) {
        currentRecipe = recipe;
    }

    // Get a recipe from the channel
    @NonNull
    public Recipe getCurrentRecipe() {
        return  currentRecipe;
    }
}
