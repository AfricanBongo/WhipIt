package com.africanbongo.whipit.model.offline;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OfflineRecipeDAO {
    @Query("INSERT INTO offline_recipes (id, title, imageURI, sourceURL, summary, steps, ingredients) VALUES " +
            "(:id, :title, :imageURI, :sourceURL, :summary, :steps, :ingredients)")
    void saveRecipe(int id, String title, String imageURI, String sourceURL, String summary, String steps, String ingredients);

    @Query("SELECT * FROM offline_recipes")
    List<OfflineRecipe> getAllOfflineRecipes();

    @Query("DELETE FROM offline_recipes WHERE id = :id")
    void deleteRecipe(int id);
}
