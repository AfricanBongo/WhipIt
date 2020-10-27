package com.africanbongo.whipit.model.searchrecipe;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.africanbongo.whipit.controller.adapters.SearchRecipeAdapter;
import com.africanbongo.whipit.model.SpoonacularAPI;
import com.africanbongo.whipit.model.explorerecipe.RecipeRequestQueue;
import com.africanbongo.whipit.model.interfaces.Recipe;
import com.africanbongo.whipit.model.interfaces.RecipeList;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/*
Singleton Class
Holds information and processes revolving around the SearchRecipe object
 */
public class SearchRecipeList implements RecipeList {

    // Instance
    private static SearchRecipeList instance;

    private SearchRecipeAdapter searchRecipeAdapter;

    // Stores the recipes retrieved from the API
    private List<SearchRecipe> currentSearches;

    // Stores the string for which recipe view holders to load
    private String recipeResultString = null;

    // Stores the auto complete strings
    private List<String> autocompleteStrings;
    private Context context;

    // Error listener for a Volley request
    Response.ErrorListener errorListener;


    private final String autocompleteQuery = SpoonacularAPI.AUTOCOMPLETE_START +
            SpoonacularAPI.FIRST_KEY + SpoonacularAPI.AUTOCOMPLETE_END;

    private SearchRecipeList(Context context) {
        this.context = context;
        autocompleteStrings = new ArrayList<>();
    }

    @Override
    @NonNull
    public List<? extends Recipe> getRecipeList() {
        if (currentSearches == null) {
            currentSearches = new ArrayList<>();
        }
        return currentSearches;
    }

    // Get autocomplete strings from the api
    public void loadAutoCompleteStrings(String queryString) {
        String fullAutoCompleteQuery = autocompleteQuery + queryString.trim();

        // Request listener to unpack JSONObject and get autocomplete strings
        Response.Listener<JSONArray> autocompleteListener = (JSONArray response) -> {

            try {

                // Clear the list in order not to show old searches
                autocompleteStrings.clear();

                synchronized (this) {
                    String[] fetchedStrings = new String[response.length()];

                    // Loop through list getting strings
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject autocompleteObject = response.getJSONObject(i);
                        String autocompleteString = autocompleteObject.getString("title");

                        // Add to string array
                        autocompleteStrings.add(autocompleteString);
                    }

                    notifyAll();
                }
            } catch (JSONException e) {
                Log.e("whipit", "Failed to get autocomplete strings", e);
            }

        };

        // Error listener for a Volley request
        Response.ErrorListener errorListener = error -> Log.e("whipit", "autocomplete list error", error);

        // Create new request for the recipes
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET,
                fullAutoCompleteQuery,
                null,
                autocompleteListener,
                errorListener);

        // Add to request queue in RecipeRequestQueue singleton class
        RecipeRequestQueue.getInstance(context).getRequestQueue().add(request);
    }


    // Get recipes as result of user's search
    public void getRecipesFor(String nameQuery, SearchRecipeAdapter adapter) {
        recipeResultString = nameQuery.trim() + "&number=15";

        searchRecipeAdapter = adapter;
        // Load recipes from the spoonacular API
        loadRecipeInfo();
    }

    // Load recipes as results of the search
    @Override
    public void loadRecipeInfo() {

        if (recipeResultString != null && searchRecipeAdapter != null) {
            currentSearches = new ArrayList<>();
            synchronized (currentSearches) {
                // Request listener to fetch recipes from the API
                Response.Listener<JSONObject> fetchSearchRecipes = (JSONObject response) -> {

                    try {

                        // Parse the results
                        JSONArray results = response.getJSONArray("results");

                        // Loop through JSONArray creating the SearchRecipe objects
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject result = results.getJSONObject(i);

                            int apiId = result.getInt("id");
                            String imageURL = result.getString("image");
                            String title = result.getString("title");

                            // Create new SearchRecipe object
                            SearchRecipe searchRecipe =
                                    new SearchRecipe(apiId, title, imageURL, context);

                            currentSearches.add(searchRecipe);
                        }

                        searchRecipeAdapter.notifyDataSetChanged();

                        // Load the remaining information for the recipes
                        loadForEachSearchRecipe(currentSearches);

                    } catch (JSONException e) {
                        Log.e("whipit", "Error loading search recipes", e);
                    }
                };

                // Error listener for a Volley request
                errorListener =
                        error -> Log.e("whipit", "recipe list error", error);

                // Create new request for the recipes
                JsonObjectRequest request = new JsonObjectRequest(
                        Request.Method.GET,
                        SpoonacularAPI.GET_RECIPE_INFO_NAME + recipeResultString,
                        null,
                        fetchSearchRecipes,
                        errorListener
                );

                // Add request to request queue
                RecipeRequestQueue
                        .getInstance(context)
                        .getRequestQueue()
                        .add(request);
            }

        }


    }

    // Load information for each search recipe
    private void loadForEachSearchRecipe(@NonNull List<SearchRecipe> recipes) {

        if (!recipes.isEmpty()) {

            StringBuilder ids = new StringBuilder();

            // First loop getting each recipe's api Id
            // It's needed for the request url
            for (SearchRecipe recipe : recipes) {

                // Add comma at the end of the string
                // As long as it's not the last recipe id
                if (recipes.indexOf(recipe) != (recipes.size() - 1)) {
                    ids.append(recipe.getApiId() + ",");
                } else {
                    ids.append(recipe.getApiId());
                }
            }

            Response.Listener<JSONArray> forEachSearchRecipe = (JSONArray response) -> {

                try {

                    for (int i = 0; i < response.length(); i++) {
                        JSONObject recipeObject = response.getJSONObject(i);
                        SearchRecipe searchRecipe = recipes.get(i);

                        searchRecipe.setSummary(recipeObject.getString("summary"));
                        searchRecipe.setSourceURL(recipeObject.getString("sourceUrl"));

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

                        // Load out ingredients and instructions
                        searchRecipe.setIngredients(ingredientsBuilder.toString());
                        searchRecipe.setSteps(instructionsBuilder.toString());

                        searchRecipe.setServings(recipeObject.getInt("servings"));
                    }
                } catch (JSONException e) {
                    Log.e("whipit", "Failed to load search recipes information", e);
                }
            };

            JsonArrayRequest request = new JsonArrayRequest(
                    Request.Method.GET,
                    SpoonacularAPI.GET_RECIPE_INFO_BULK + ids,
                    null,
                    forEachSearchRecipe,
                    errorListener
            );

            // Add to request queue in RecipeRequestQueue singleton class
            RecipeRequestQueue
                    .getInstance(context)
                    .getRequestQueue()
                    .add(request);
        }

    }

    public static SearchRecipeList getInstance(Context context) {
        if (instance == null) {
            instance = new SearchRecipeList(context);
        }
        return instance;
    }

    public List<String> getAutoCompleteStrings() {

        // Only return string array when strings are loaded
        synchronized (this) {
            if (autocompleteStrings.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Log.e("whipit", "thread exception", e);
                }
            }
        }

        return autocompleteStrings;
    }
}
