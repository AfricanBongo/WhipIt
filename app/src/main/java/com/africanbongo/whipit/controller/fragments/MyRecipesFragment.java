package com.africanbongo.whipit.controller.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.adapters.MyRecipesAdapter;
import com.africanbongo.whipit.controller.customviews.MyRecipeItemTouchHelper;
import com.africanbongo.whipit.model.myrecipe.MyRecipe;
import com.africanbongo.whipit.model.myrecipe.MyRecipeList;
import com.google.android.material.snackbar.Snackbar;

/**
 * A fragment representing a list of offline recipes.
 *
 * Kudos to @delaroy for the library to show swiping away a list item
 * https://github.com/delaroy/SwipeToDismiss
 */

public class MyRecipesFragment extends Fragment {

    private MyRecipesAdapter myRecipesAdapter;

    // Callback that allows the user to swipe right to delete a recipe
    MyRecipeItemTouchHelper swipeToDeleteCallback =
            new MyRecipeItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            MyRecipesAdapter.GeneralRecipesViewHolder recipesViewHolder =
                    (MyRecipesAdapter.GeneralRecipesViewHolder) viewHolder;

            Context context = getContext();
            // Get the recipe
            MyRecipe recipe = (MyRecipe) recipesViewHolder.getLinearLayout().getTag();

            switch (direction) {
                // Then delete the recipe
                case ItemTouchHelper.RIGHT:
                    // Delete directly from storage
                    MyRecipeList.getInstance(context).deleteRecipe(recipe.getApiId());
                    // Update the adapter data set
                    updateList();
                    // Create the delete text to show in the snackbar
                    String deleteText = "\"" + recipe.getTitle() + "\"" +
                            " is being deleted";
                    // Create snack bar
                    Snackbar snackbar = Snackbar
                            .make(getView().findViewById(R.id.my_recipes_coordinator),
                                    deleteText, Snackbar.LENGTH_SHORT);
                    // Allow user to undo the deletion
                    snackbar.setAction("Undo", v -> {
                        MyRecipeList
                                .getInstance(getContext())
                                .saveRecipe(recipe);
                    });
                    // Show the snack bar
                    snackbar.show();
                    break;

                // Else share the recipe
                case ItemTouchHelper.LEFT:
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, recipe.shareRecipe());
                    startActivity(intent);
                    break;
            }

        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes_list, container, false);

        // Set the adapter
        if (view != null) {
            Context context = view.getContext();
            RecyclerView recyclerView = view.findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            if (myRecipesAdapter == null) {
                myRecipesAdapter = new MyRecipesAdapter(context);
            }

            recyclerView.setAdapter(myRecipesAdapter);

            // Assign item touch helper to recyclerview
            new ItemTouchHelper(swipeToDeleteCallback).attachToRecyclerView(recyclerView);
        }
        return view;
    }

    // Update the recipes saved
    public void updateList() {
        if (myRecipesAdapter != null) {
            myRecipesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRecipesAdapter.clearImages();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateList();
    }
}