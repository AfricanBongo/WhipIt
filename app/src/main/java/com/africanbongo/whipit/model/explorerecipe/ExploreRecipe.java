package com.africanbongo.whipit.model.explorerecipe;

import androidx.annotation.NonNull;

import com.africanbongo.whipit.model.interfaces.Recipe;

/*
Recipe that has its information gathered from the spoonacular API (Credits to them for their API)
 */

public class ExploreRecipe implements Recipe {

    private boolean saved;

    private int apiId;
    private int servings;
    private String title;
    private String imageURL;
    private String sourceURL;
    private String summary;
    private String steps;
    private String ingredients;

    private Object tag = null;

    public ExploreRecipe(int apiId, String title, String imageURL, String sourceURL,
                         String summary, String steps, String ingredients, boolean saved, int servings) {
        this.apiId = apiId;
        this.title = title;
        this.imageURL = imageURL;
        this.sourceURL = sourceURL;
        this.summary = summary;
        this.steps = steps;
        this.ingredients = ingredients;
        this.saved = saved;
        this.servings = servings;
    }

    // Implements methods inherited from the interface
    @Override
    public int getApiId() {
        return apiId;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public String getImageURL() {
        return imageURL;
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
    public int getServings() {
        return servings;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    @NonNull
    @Override
    public Object getTag() {
        return tag;
    }

    @NonNull
    @Override
    public void setTag(@NonNull Object object) {
        tag = object;
    }
}
