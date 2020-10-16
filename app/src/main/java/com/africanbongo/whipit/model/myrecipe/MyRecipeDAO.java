package com.africanbongo.whipit.model.myrecipe;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MyRecipeDAO {
    @Query("INSERT INTO my_recipes (apiId, title, imageURL, sourceURL, summary, steps, ingredients) VALUES " +
            "(:apiId, :title, :imageURL, :sourceURL, :summary, :steps, :ingredients)")
    void saveRecipe(int apiId, String title, String imageURL, String sourceURL, String summary, String steps, String ingredients);

    @Query("SELECT * FROM my_recipes")
    List<MyRecipe> getAllMyRecipes();

    @Query("DELETE FROM my_recipes WHERE apiId = :apiId")
    void deleteRecipe(int apiId);

    @Query("DELETE FROM my_recipes")
    void clearRecipes();
}
