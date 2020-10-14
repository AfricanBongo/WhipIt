package com.africanbongo.whipit.model.offline;

import androidx.room.Database;
import androidx.room.RoomDatabase;

/*
Database that stores offline recipes
 */
@Database(entities = {OfflineRecipe.class}, version = 1)
public abstract class OfflineRecipeDatabase extends RoomDatabase {
    public abstract OfflineRecipeDAO offlineRecipeDAO();
}
