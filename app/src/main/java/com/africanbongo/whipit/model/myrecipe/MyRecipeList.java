package com.africanbongo.whipit.model.myrecipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import androidx.room.Room;

import com.africanbongo.whipit.model.interfaces.Recipe;
import com.africanbongo.whipit.model.interfaces.RecipeList;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
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

    private MyRecipeList(Context context) {
        this.context = context;

        // Build database
        recipeDatabase = Room
                .databaseBuilder(context, MyRecipeDatabase.class, "my_recipes")
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
            // Save the recipe into the Room Database

            synchronized (recipeDatabase.myRecipeDAO()) {
                recipeDatabase.myRecipeDAO().saveRecipe(
                        myRecipe.apiId, myRecipe.title, myRecipe.imageURL,
                        myRecipe.sourceURL, myRecipe.summary, myRecipe.steps,
                        myRecipe.ingredients
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

    // Used to save image to disk
    private String saveImage(Bitmap image, Recipe recipe) {
        String savedImagePath = null;
        String imageName = recipe.getTitle().replace(" ", "").toLowerCase();

        // Generated storage directory
        String imageFileName = "JPEG_" + imageName + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/Pictures/WhipIt");
        boolean success = true;

        // Create directory if it doesn't exist
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }

        // Save the file to memory
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try (OutputStream fOut = new FileOutputStream(imageFile)) {
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
            } catch (Exception e) {
                Log.e("whipit","Error saving recipe image to memory", e);
            }
        }
        return savedImagePath;
    }

    public void clearRecipes() {
        recipeDatabase.myRecipeDAO().clearRecipes();
    }
}
