package com.africanbongo.whipit.model.searchrecipe;

import androidx.annotation.NonNull;

import com.africanbongo.whipit.model.interfaces.Recipe;

/*
Object used when loading recipes from the search tab
 */
public class SearchRecipe implements Recipe {

    private String title;
    private String sourceURL;
    private String imageURL;
    private String summary;
    private String steps;
    private String ingredients;
    private int servings;
    private int apiId;


    public SearchRecipe(String title, String imageURL) {
        this.title = title;
        this.imageURL = imageURL;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getSourceURL() {
        return sourceURL;
    }

    @Override
    public String getSummary() {
        return summary;
    }

    @Override
    public String getSteps() {
        return steps;
    }

    @Override
    public String getIngredients() {
        return ingredients;
    }

    @Override
    public String getImageURL() {
        return imageURL;
    }

    @Override
    public int getServings() {
        return servings;
    }

    @Override
    public int getApiId() {
        return apiId;
    }

    @NonNull
    @Override
    public Object getTag() {
        return null;
    }

    @NonNull
    @Override
    public void setTag(@NonNull Object object) {

    }
}
