package com.africanbongo.whipit.model.offline;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.africanbongo.whipit.model.interfaces.Recipe;

/*
Recipe that is saved offline in a room database
 */

@Entity(tableName = "offline_recipes")
public class OfflineRecipe implements Recipe {
    @PrimaryKey
    public int id;
    @ColumnInfo
    public int apiId;

    @ColumnInfo
    public String title;

    @ColumnInfo
    public String imageURI;

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

    public String getImageURI() {
        return imageURI;
    }

    @Override
    public int getApiId() {
        return apiId;
    }
}
