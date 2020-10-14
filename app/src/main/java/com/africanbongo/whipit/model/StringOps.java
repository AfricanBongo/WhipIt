package com.africanbongo.whipit.model;

import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.africanbongo.whipit.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
Singleton Class used to obtain Strings and perform operations on String related objects
 */
public class StringOps {
    private Map<Integer, String> recipeGroupStrings;
    private int recipeGroupIndex = 0;
    private static StringOps instance;

    // Use View as argument in constructor to get list of string in resources files
    @RequiresApi(api = Build.VERSION_CODES.N)
    private StringOps(View view) {
        recipeGroupStrings = new HashMap<>();

        List<String> recipes = new ArrayList<>(
                Arrays.asList(view.getResources().getStringArray(R.array.recipe_groups))
        );

        recipes.forEach(s -> recipeGroupStrings.put(
                recipes.indexOf(s), s));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static StringOps getInstance(View view) {
        if (instance == null) {
            instance = new StringOps(view);
        }
        return instance;
    }

    // Obtain the name of one recipe group
    public synchronized String getRecipeGroup() {
        // Get recipe group string from the list of strings
        String recipeGroup = recipeGroupStrings.get(recipeGroupIndex);

        // Increment the index to get next string when the method is called
        recipeGroupIndex++;

        // If the index is equal or more than the size of the list, reset it
        if (recipeGroupIndex >= recipeGroupStrings.size()) {
            recipeGroupIndex = 0;
        }

        return titleString(recipeGroup);
    }

    public int getRecipeGroupSize() {
        return recipeGroupStrings.size();
    }

    // Return a string with the first letter capitalized
    public String titleString(String string) {
        return string
                .substring(0, 1)
                .toUpperCase()
                .concat(string.substring(1));
    }
}
