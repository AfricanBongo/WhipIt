package com.africanbongo.whipit.model.searchrecipe;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.africanbongo.whipit.model.SpoonacularAPI;
import com.africanbongo.whipit.model.explorerecipe.RecipeRequestQueue;
import com.africanbongo.whipit.model.interfaces.Recipe;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    // Used for loading recipes from the internet
    private static Context context;


    public SearchRecipe(int apiId, String title, String imageURL, Context context) {
        this.apiId = apiId;
        this.title = title;
        this.imageURL = imageURL;

        if (context == null) {
            this.context = context;
        }
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


    // Only executes when the recipe information wasn't loaded
    public void loadRecipeInfo() {

        // Response listener to retrieve recipe info
        Response.Listener<JSONObject> fetchRecipeInfo = (JSONObject response) -> {

            try {
                // Load strings not within other JSONArrays first

                synchronized (this) {
                    summary = response.getString("summary");
                    sourceURL = response.getString("sourceUrl");

                    StringBuilder ingredientsBuilder = new StringBuilder();
                    StringBuilder instructionsBuilder = new StringBuilder();

                    // Grab ingredients and store as tab separated values string
                    if (response.optJSONArray("extendedIngredients") != null) {
                        JSONArray ingredientsArray = response.getJSONArray("extendedIngredients");

                        for (int j = 0; j < ingredientsArray.length(); j++) {
                            String ingredient = ingredientsArray.getJSONObject(j).getString("originalString");
                            ingredientsBuilder.append(ingredient + "\t");
                        }
                    } else {
                        ingredientsBuilder.append("Empty");
                    }

                    // Grab instructions and store as tab separated values string
                    JSONObject instructionObject = response.getJSONArray("analyzedInstructions")
                            .optJSONObject(0);
                    if (instructionObject != null) {
                        if (instructionObject.optJSONArray("steps") != null) {
                            JSONArray instructionsArray = instructionObject.getJSONArray("steps");

                            for (int j = 0; j < instructionsArray.length(); j++) {
                                String instruction = instructionsArray.getJSONObject(j).getString("step");
                                instructionsBuilder.append(instruction + "\t");
                            }
                        }
                    } else {
                        instructionsBuilder.append("Empty");
                    }

                    // Load out ingredients and instructions
                    ingredients = ingredientsBuilder.toString();
                    steps = instructionsBuilder.toString();

                    servings = response.getInt("servings");
                }

            } catch (JSONException e) {
                Log.e("whipit", "Error loading search recipe info", e);
            }
        };

        // Error listener for a Volley request
        Response.ErrorListener errorListener = error -> Log.e("whipit", "recipe list error", error);

        // Create new request for the recipes
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                SpoonacularAPI.GET_RECIPE_INFO_ID_START + apiId +
                        SpoonacularAPI.GET_RECIPE_INFO_ID_END,
                null,
                fetchRecipeInfo,
                errorListener
        );

        // Add to request queue in RecipeRequestQueue singleton class
        RecipeRequestQueue
                .getInstance(context)
                .getRequestQueue()
                .add(request);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
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

    public void setApiId(int apiId) {
        this.apiId = apiId;
    }
}
