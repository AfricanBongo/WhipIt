package com.africanbongo.whipit.model.offline;

import android.content.Context;

import androidx.room.Room;

import com.africanbongo.whipit.model.interfaces.Recipe;
import com.africanbongo.whipit.model.interfaces.RecipeList;

import java.util.ArrayList;
import java.util.List;

/*
Singleton class used to load offline recipes from Room Database
And perform functions on elements of the database
 */
public class OfflineRecipeList implements RecipeList {
    private List<OfflineRecipe> offlineRecipeList;
    private OfflineRecipeDatabase recipeDatabase;
    private static OfflineRecipeList instance;

    private OfflineRecipeList(Context context) {
        recipeDatabase = Room
                .databaseBuilder(context, OfflineRecipeDatabase.class, "offline_recipes")
                .allowMainThreadQueries()
                .build();

        loadRecipeInfo();
    }
    @Override
    public List<OfflineRecipe> getRecipeList() {
        return offlineRecipeList;
    }

    @Override
    public void loadRecipeInfo() {
        offlineRecipeList = recipeDatabase.offlineRecipeDAO().getAllOfflineRecipes();
    }

    // Remove a recipe from the database and list
    public boolean deleteRecipe(int id) {
        recipeDatabase.offlineRecipeDAO().deleteRecipe(id);
        return removeRecipeFromList(id);
    }

    // Remove a recipe from the list
    private boolean removeRecipeFromList(int id) {
        OfflineRecipe recipe = (OfflineRecipe) getRecipe(id);
        return offlineRecipeList.remove(recipe);
    }

    // Get a recipe from the list
    public Recipe getRecipe(int id) {
        for (OfflineRecipe recipe : offlineRecipeList) {
            if (recipe.id == id) {
                return recipe;
            }
        }

        return null;
    }

    // Brings back
    public boolean contains(int id) {
        Recipe recipe = getRecipe(id);
        return (recipe != null);
    }

    public static OfflineRecipeList getInstance(Context context) {
        if (instance == null) {
            instance = new OfflineRecipeList(context);
        }
        return instance;
    }
}
