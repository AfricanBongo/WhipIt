package com.africanbongo.whipit.model.interfaces;

import android.os.Parcelable;
import android.view.View;

import androidx.annotation.NonNull;

/*
Recipe interface for getting info about a recipe
 */
public interface Recipe {
    String getTitle();
    String getSourceURL();
    String getSummary();
    String getSteps();
    String getIngredients();
    String getImageURL();
    int getApiId();

    // Used passing around objects
    // Intended to carry download button to maintain state of its icons
    @NonNull
    Object getTag();

    @NonNull
    void setTag(Object object);
}
