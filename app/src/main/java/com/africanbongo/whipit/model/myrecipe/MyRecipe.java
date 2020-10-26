package com.africanbongo.whipit.model.myrecipe;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.africanbongo.whipit.model.interfaces.Recipe;

/*
Recipe that is saved offline in a room database
 */

@Entity(tableName = "my_recipes")
public class MyRecipe implements Recipe {
    @PrimaryKey
    public int id;
    @ColumnInfo
    public int apiId;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public String imageURL;

    @ColumnInfo
    public String sourceURL;

    @ColumnInfo
    public String summary;

    @ColumnInfo
    public String steps;

    @ColumnInfo
    public String ingredients;

    @ColumnInfo
    public int servings;

    @Ignore
    private Object tag = null;

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
    public int getServings() {
        return servings;
    }

    @Override
    public String getIngredients() {
        return ingredients;
    }

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public int getApiId() {
        return apiId;
    }

    @NonNull
    @Override
    public Object getTag() {
        return tag;
    }

    @Override
    public void setTag(@NonNull Object object) {
        tag = object;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
