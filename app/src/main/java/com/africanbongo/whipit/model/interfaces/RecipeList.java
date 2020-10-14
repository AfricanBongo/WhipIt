package com.africanbongo.whipit.model.interfaces;

import java.util.List;

// For holding recipes allows flexibility
public interface RecipeList {
    List<? extends Recipe> getRecipeList();
    void loadRecipeInfo();
}
