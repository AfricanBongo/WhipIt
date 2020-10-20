package com.africanbongo.whipit.model.myrecipe;

import android.nfc.Tag;

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

    @NonNull
    @Override
    public void setTag(Object object) {
        tag = object;
    }
}
