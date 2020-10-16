package com.africanbongo.whipit.model.myrecipe;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/*
Database that stores offline recipes
 */
@Database(entities = {MyRecipe.class}, version = 1)
public abstract class MyRecipeDatabase extends RoomDatabase {
    public abstract MyRecipeDAO myRecipeDAO();
}
