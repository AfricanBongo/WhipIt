package com.africanbongo.whipit.model.explorerecipe;

import android.content.Context;
import android.util.Log;

import com.africanbongo.whipit.model.interfaces.RecipeList;
import com.africanbongo.whipit.model.myrecipe.MyRecipeList;
import com.africanbongo.whipit.view.adapters.OnlineRecipeGroupAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/*
Credit for API use to spoonacular for use of their API: https://spoonacular.com/food-api
 */
public class ExploreRecipeList implements RecipeList {

    // Context used when retrieving RecipeRequestQueue
    private Context context;

    // Default image if not found
    public static final String IMAGE_NOT_FOUND = "https://bitsofco.de/content/images/2018/12/broken-1.png";

    // Stores the list of online recipes
    private List<ExploreRecipe> exploreRecipeList;

    // Adapter binded to the list
    private OnlineRecipeGroupAdapter boundAdapter;

    // Base URL used to grab the specific type of recipes from sp
    public static final String IMPLICIT_RECIPE_GROUPS_URL = "https://api.spoonacular.com/recipes/random?" +
            "apiKey=keyhere&number=50&tags=";

    // Explicit group for implicit recipe groups url
    private final String explicitRecipeGroupsURL;

    public ExploreRecipeList(String recipeGroup, OnlineRecipeGroupAdapter boundAdapter, Context context) {
        this.context = context;
        exploreRecipeList = new ArrayList<>();
        this.boundAdapter = boundAdapter;
        this.explicitRecipeGroupsURL = IMPLICIT_RECIPE_GROUPS_URL + recipeGroup.toLowerCase();
        loadRecipeInfo();
    }

    public void loadRecipeInfo() {

        // Request listener to unpack JSONObject and load recipes
        Response.Listener<JSONObject> listListener = (JSONObject response) -> {
            try {

                // Loop through array creating recipe objects
                JSONArray recipesArray = response.getJSONArray("recipes");

                for (int i = 0; i < recipesArray.length(); i++) {
                    JSONObject recipeObject = recipesArray.getJSONObject(i);

                    // Load strings not within other JSONArrays first
                    String recipeTitle = recipeObject.getString("title");
                    String imageURL = recipeObject.optString("image", IMAGE_NOT_FOUND);

                    String summary = recipeObject.getString("summary");
                    String sourceURL = recipeObject.getString("sourceUrl");

                    StringBuilder ingredientsBuilder = new StringBuilder();
                    StringBuilder instructionsBuilder = new StringBuilder();

                    // Grab ingredients and store as tab separated values string
                    if (recipeObject.optJSONArray("extendedIngredients") != null) {
                        JSONArray ingredientsArray = recipeObject.getJSONArray("extendedIngredients");

                        for (int j = 0; j < ingredientsArray.length(); j++) {
                            String ingredient = ingredientsArray.getJSONObject(j).getString("originalString");
                            ingredientsBuilder.append(ingredient + "\t");
                        }
                    } else {
                        ingredientsBuilder.append("Empty");
                    }

                    // Grab instructions and store as tab separated values string
                    JSONObject instructionObject = recipeObject.getJSONArray("analyzedInstructions")
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

                    // Api Id of the recipe
                    int apiId = recipeObject.getInt("id");
                    boolean saved;

                    // Check if the recipe is already saved offline in the app
                    if (MyRecipeList.getInstance(context).contains(apiId)) {
                        saved = true;
                    } else {
                        saved = false;
                    }

                    // Create new recipe object
                    ExploreRecipe newRecipe = new ExploreRecipe(
                            apiId, recipeTitle, imageURL, sourceURL, summary,
                            instructionsBuilder.toString(), ingredientsBuilder.toString(), saved
                            );

                    // Add recipe to list
                    exploreRecipeList.add(newRecipe);
                }

                // Update data set of bound adapter
                boundAdapter.stopLoadingAnimations();
            } catch (JSONException e) {
                Log.e("whipit", "Failed to load objects from internet", e);
            }
        };

        // Error listener for a Volley request
        Response.ErrorListener errorListener = error -> Log.e("whipit", "recipe list error", error);

        // Create new request for the recipes
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                explicitRecipeGroupsURL,
                null,
                listListener,
                errorListener);

        // Add to request queue in RecipeRequestQueue singleton class
        RecipeRequestQueue.getInstance(context).getRequestQueue().add(request);
    }

    public List<ExploreRecipe> getRecipeList() {
        return exploreRecipeList;
    }
}
