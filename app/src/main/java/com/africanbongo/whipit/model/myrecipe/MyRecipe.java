package com.africanbongo.whipit.model.myrecipe;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
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

    public String getImageURL() {
        return imageURL;
    }

    @Override
    public int getApiId() {
        return apiId;
    }
}
