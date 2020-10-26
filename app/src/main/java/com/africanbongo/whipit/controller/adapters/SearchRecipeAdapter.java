package com.africanbongo.whipit.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.model.searchrecipe.SearchRecipe;
import com.africanbongo.whipit.model.searchrecipe.SearchRecipeList;
import com.bumptech.glide.Glide;

public class SearchRecipeAdapter extends RecyclerView.Adapter<MyRecipesAdapter.GeneralRecipesViewHolder> {

    private Context context;

    public SearchRecipeAdapter(String recipeQuery, Context context) {
        this.context = context;

        SearchRecipeList
                .getInstance(context)
                .getRecipesFor(recipeQuery, this);
    }
    @NonNull
    @Override
    public MyRecipesAdapter.GeneralRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_my_recipes, parent, false);

        return new MyRecipesAdapter.GeneralRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipesAdapter.GeneralRecipesViewHolder holder, int position) {
        SearchRecipe searchRecipe = (SearchRecipe)
                SearchRecipeList
                        .getInstance(context)
                        .getRecipeList()
                        .get(position);

        holder
                .getRecipeTitle()
                .setText(searchRecipe.getTitle());

        holder
                .getLinearLayout()
                .setTag(searchRecipe);

        Glide.with(context)
                .load(searchRecipe.getImageURL())
                .placeholder(context.getDrawable(R.drawable.placeholder))
                .error(context.getDrawable(R.drawable.placeholder))
                .into(holder.getRecipeImage());


    }

    @Override
    public int getItemCount() {
        return SearchRecipeList
                .getInstance(context)
                .getRecipeList()
                .size();
    }
}