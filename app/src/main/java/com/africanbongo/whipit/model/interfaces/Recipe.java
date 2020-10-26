package com.africanbongo.whipit.model.interfaces;

import android.content.Intent;

import androidx.annotation.NonNull;

/*
Recipe interface for getting info about a recipe
 */
public interface Recipe {
    String getTitle();
    String getSourceURL();
    String getSummary();
    String getSteps();
    String getIngredients();
    String getImageURL();
    int getServings();
    int getApiId();

    // Used when sharing the recipe
    default String shareRecipe() {
        return "Hey," +
                " I want you to check out this recipe, \"" + getTitle() +
                "\"" + " at:\n" + getSourceURL();
    }

    // Used passing around objects
    // Intended to carry download button to maintain state of its icons
    @NonNull
    Object getTag();

    @NonNull
    void setTag(@NonNull Object object);
}
