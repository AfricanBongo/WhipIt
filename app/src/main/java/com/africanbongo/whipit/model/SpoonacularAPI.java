package com.africanbongo.whipit.model;

/*
Contains the api keys used to access information from the spoonacular api
 */
public class SpoonacularAPI {
    // Pieces of a URL used to grab the specific type of recipes from spoonacular api
    public static final String GET_RANDOM_RECIPES_START = "https://api.spoonacular.com/recipes/random?";
    public static final String GET_RANDOM_RECIPES_END = "&number=50&tags=";

    // Pieces of a URL used to grab autocomplete strings from spoonacular api
    public static final String AUTOCOMPLETE_START = "https://api.spoonacular.com/recipes/autocomplete?";
    public static final String AUTOCOMPLETE_END = "&number=10&query=";

    // Pieces of a URL used to get information about a certain recipe
    public static final String GET_RECIPE_INFO_START = "";
    public static final String GET_RECIPE_INFO_END = "";

    public static final String API_KEY_GET = "apiKey=";

    // Keys to access api
    public static final String FIRST_KEY = API_KEY_GET + "ddfab49f8dd0483ba21ccf2944815631";
    public static final String SECOND_KEY = null;
    public static final String THIRD_KEY = null;
    public static final String[] keys = new String[] {FIRST_KEY, SECOND_KEY, THIRD_KEY};
}
