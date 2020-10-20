package com.africanbongo.whipit.model.myrecipe;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.africanbongo.whipit.model.interfaces.Recipe;
import com.africanbongo.whipit.model.interfaces.RecipeList;

import java.util.List;

/*
Singleton class used to load offline recipes from Room Database
And perform functions on elements of the database
 */
public class MyRecipeList implements RecipeList {
    private List<MyRecipe> myRecipeList;
    private MyRecipeDatabase recipeDatabase;
    private Context context;
    private static MyRecipeList instance;


    // Migration after adding tag and servings to MyRecipe Object
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE my_recipes"
            + " ADD COLUMN servings INTEGER DEFAULT 1 not null");
        }
    };

    private MyRecipeList(Context context) {
        this.context = context;

        // Build database
        recipeDatabase = Room
                .databaseBuilder(context, MyRecipeDatabase.class, "my_recipes")
                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries()
                .build();

        loadRecipeInfo();
    }

    @Override
    public List<MyRecipe> getRecipeList() {
        return recipeDatabase.myRecipeDAO().getAllMyRecipes();
    }

    @Override
    public synchronized void loadRecipeInfo() {
        myRecipeList = recipeDatabase.myRecipeDAO().getAllMyRecipes();
    }

    // Remove a recipe from the database and list
    public void deleteRecipe(int id) {
        synchronized (recipeDatabase.myRecipeDAO()) {
            recipeDatabase.myRecipeDAO().deleteRecipe(id);
        }
        loadRecipeInfo();
    }

    // Get a recipe from the list
    public Recipe getRecipe(int id) {
        for (MyRecipe recipe : myRecipeList) {
            if (recipe.apiId == id) {
                return recipe;
            }
        }

        return null;
    }

    // Returns true if the recipe exists
    public boolean contains(int id) {
        Recipe recipe = getRecipe(id);
        return (recipe != null);
    }

    // Save recipe
    public void saveRecipe(Recipe recipe) {

        if (!contains(recipe.getApiId())) {
            MyRecipe myRecipe = new MyRecipe();
            myRecipe.apiId = recipe.getApiId();
            myRecipe.ingredients = recipe.getIngredients();
            myRecipe.title = recipe.getTitle();
            myRecipe.steps = recipe.getSteps();
            myRecipe.summary = recipe.getSummary();
            myRecipe.sourceURL = recipe.getSourceURL();
            myRecipe.imageURL = recipe.getImageURL();
            myRecipe.servings = recipe.getServings();

            // Add tag to recipe
            if (recipe.getTag() != null) {
                myRecipe.setTag(recipe.getTag());
            }

            // Save the recipe into the Room Database
            synchronized (recipeDatabase.myRecipeDAO()) {
                recipeDatabase.myRecipeDAO().saveRecipe(
                        myRecipe.apiId, myRecipe.title, myRecipe.imageURL,
                        myRecipe.sourceURL, myRecipe.summary, myRecipe.steps,
                        myRecipe.ingredients, myRecipe.servings
                );
            }
        }

        loadRecipeInfo();
    }

    public static MyRecipeList getInstance(Context context) {
        if (instance == null) {
            instance = new MyRecipeList(context);
        }
        return instance;
    }

    public void clearRecipes() {
        recipeDatabase.myRecipeDAO().clearRecipes();
    }
}
