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

    public static final String API_KEY_GET = "apiKey=";

    // Keys to access api
    public static final String FIRST_KEY = API_KEY_GET + "38b8d36dd8b54ed090d449516d3e4512";
    public static final String SECOND_KEY = null;
    public static final String THIRD_KEY = null;
    public static final String[] keys = new String[] {FIRST_KEY, SECOND_KEY, THIRD_KEY};

    // URL used to get recipe information using the recipe id
    public static final String GET_RECIPE_INFO_ID_START = "https://api.spoonacular.com/recipes/";
    public static final String GET_RECIPE_INFO_ID_END = "/information?" + FIRST_KEY;

    // URL used to get recipe information using the recipe name
    public static final String GET_RECIPE_INFO_NAME = "https://api.spoonacular.com/recipes/complexSearch?" +
            FIRST_KEY + "&query=";
}
