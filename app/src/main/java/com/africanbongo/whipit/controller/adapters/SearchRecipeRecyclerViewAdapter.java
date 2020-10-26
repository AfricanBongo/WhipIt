package com.africanbongo.whipit.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.adapters.MyRecipesAdapter;

public class SearchRecipeRecyclerViewAdapter extends RecyclerView.Adapter<MyRecipesAdapter.MyRecipesViewHolder> {

    private androidx.appcompat.widget.SearchView recipeSearchView;

    public SearchRecipeRecyclerViewAdapter(SearchView searchView) {

    }

    @NonNull
    @Override
    public MyRecipesAdapter.MyRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_my_recipes, parent, false);

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipesAdapter.MyRecipesViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}