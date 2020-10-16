package com.africanbongo.whipit.model.offline;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Room;

import com.africanbongo.whipit.model.interfaces.Recipe;
import com.africanbongo.whipit.model.interfaces.RecipeList;
import com.africanbongo.whipit.model.online.OnlineRecipe;
import com.africanbongo.whipit.view.fragments.MyRecipesFragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

/*
Singleton class used to load offline recipes from Room Database
And perform functions on elements of the database
 */
public class OfflineRecipeList implements RecipeList {
    private List<OfflineRecipe> offlineRecipeList;
    private OfflineRecipeDatabase recipeDatabase;
    private Context context;
    private static OfflineRecipeList instance;

    private OfflineRecipeList(Context context) {
        this.context = context;

        // Build database
        recipeDatabase = Room
                .databaseBuilder(context, OfflineRecipeDatabase.class, "offline_recipes")
                .allowMainThreadQueries()
                .build();

        loadRecipeInfo();
    }
    @Override
    public List<OfflineRecipe> getRecipeList() {
        return offlineRecipeList;
    }

    @Override
    public void loadRecipeInfo() {
        offlineRecipeList = recipeDatabase.offlineRecipeDAO().getAllOfflineRecipes();
    }

    // Remove a recipe from the database and list
    public boolean deleteRecipe(int id) {
        recipeDatabase.offlineRecipeDAO().deleteRecipe(id);
        return removeRecipeFromList(id);
    }

    // Remove a recipe from the list
    private boolean removeRecipeFromList(int id) {
        OfflineRecipe recipe = (OfflineRecipe) getRecipe(id);
        return offlineRecipeList.remove(recipe);
    }

    // Get a recipe from the list
    public Recipe getRecipe(int id) {
        for (OfflineRecipe recipe : offlineRecipeList) {
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
            final Handler handler = new Handler();
            handler.post(() -> {
                OfflineRecipe offlineRecipe = new OfflineRecipe();
                offlineRecipe.apiId = recipe.getApiId();
                offlineRecipe.ingredients = recipe.getIngredients();
                offlineRecipe.title = recipe.getTitle();
                offlineRecipe.steps = recipe.getSteps();
                offlineRecipe.summary = recipe.getSummary();
                offlineRecipe.sourceURL = recipe.getSourceURL();

                OnlineRecipe onlineRecipe = (OnlineRecipe) recipe;

                // Download image and save it to memory
                Glide
                        .with(context)
                        .asBitmap()
                        .load(onlineRecipe.getImageURL())
                        .into(new CustomTarget<Bitmap>(100, 100) {
                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }

                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                offlineRecipe.imageURI = saveImage(resource, offlineRecipe);
                            }
                        });

                // Save the recipe into the Room Database
                recipeDatabase.offlineRecipeDAO().saveRecipe(
                        offlineRecipe.apiId, offlineRecipe.title, offlineRecipe.imageURI,
                        offlineRecipe.sourceURL, offlineRecipe.summary, offlineRecipe.steps,
                        offlineRecipe.ingredients
                );

                // Update list adapter
                if (MyRecipesFragment.myRecipesAdapter != null) {
                    MyRecipesFragment.myRecipesAdapter.notifyDataSetChanged();
                }
            });
        }
    }
    public static OfflineRecipeList getInstance(Context context) {
        if (instance == null) {
            instance = new OfflineRecipeList(context);
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
}
