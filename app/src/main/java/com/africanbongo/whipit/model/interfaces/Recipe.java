package com.africanbongo.whipit.model.interfaces;

/*
Recipe interface for getting info about a recipe
 */
public interface Recipe {
    String getTitle();
    String getSourceURL();
    String getSummary();
    String getSteps();
    String getIngredients();
    int getApiId();
}
