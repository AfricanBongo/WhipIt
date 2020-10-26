package com.africanbongo.whipit.model.searchrecipe;
import android.content.Context;
import android.util.Log;

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
    private List<SearchRecipe> currentSearches;
    private List<String> autocompleteStrings;
    private Context context;


    private final String autocompleteQuery = SpoonacularAPI.AUTOCOMPLETE_START +
            SpoonacularAPI.FIRST_KEY + SpoonacularAPI.AUTOCOMPLETE_END;

    private SearchRecipeList(Context context) {
        this.context = context;
        autocompleteStrings = new ArrayList<>();
    }

    @Override
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

    // Load recipes as results of the search
    @Override
    public void loadRecipeInfo() {
        currentSearches = new ArrayList<>();

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
