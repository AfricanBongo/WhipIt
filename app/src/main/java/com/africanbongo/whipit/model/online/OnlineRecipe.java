package com.africanbongo.whipit.model.online;

import android.graphics.Bitmap;

import com.africanbongo.whipit.model.interfaces.Recipe;

/*
Recipe that has its information gathered from the spoonacular API (Credits to them for their API)
 */

public class OnlineRecipe implements Recipe {

    public boolean saved;

    private int id;
    private String title;
    private String imageURL;
    private String sourceURL;
    private String summary;
    private String steps;
    private String ingredients;

    public OnlineRecipe(int id, String title, String imageURL, String sourceURL, String summary, String steps, String ingredients) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.sourceURL = sourceURL;
        this.summary = summary;
        this.steps = steps;
        this.ingredients = ingredients;
    }

    @Override
    public int getApiId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getSourceURL() {
        return sourceURL;
    }

    public String getSummary() {
        return summary;
    }

    public String getSteps() {
        return steps;
    }

    public String getIngredients() {
        return ingredients;
    }
}
