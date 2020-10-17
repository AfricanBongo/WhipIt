package com.africanbongo.whipit.controller.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.model.myrecipe.MyRecipe;
import com.africanbongo.whipit.model.myrecipe.MyRecipeList;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/*
Credit to Glide library for loading images of the app
 */

public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.MyRecipesViewHolder> {
    public class MyRecipesViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout linearLayout;
        private CardView cardView;
        private ImageView recipeImage;
        private TextView recipeTitle;

        public MyRecipesViewHolder(@NonNull View itemView) {
            super(itemView);

            linearLayout = itemView.findViewById(R.id.my_recipes_layout);
            cardView = itemView.findViewById(R.id.recipe_cardview_offline);
            recipeImage = itemView.findViewById(R.id.image_cardview_offline);
            recipeTitle = itemView.findViewById(R.id.text_cardview_offline);
        }
    }

    private Context context;
    private List<MyRecipesViewHolder> viewHolders;

    public MyRecipesAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
        viewHolders = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyRecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.fragment_my_recipes, parent, false);

        return new MyRecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecipesViewHolder holder, int position) {
        MyRecipe recipe = (MyRecipe) MyRecipeList
                .getInstance(context)
                .getRecipeList()
                .get(position);

        // Try to retrieve image from storage
        Glide
                .with(context)
                .load(recipe.getImageURL())
                .error(context.getDrawable(R.drawable.placeholder))
                .into(holder.recipeImage);

        holder.recipeTitle.setText(recipe.getTitle());

        // Add view holder to the list
        viewHolders.add(holder);
    }

    @Override
    public int getItemCount() {
        return MyRecipeList
                .getInstance(context)
                .getRecipeList()
                .size();
    }

    // Clear all images in image views when the adapters fragment is destroyed
    public void clearImages() {
        if (viewHolders != null) {
            for (MyRecipesViewHolder viewHolder : viewHolders) {
                Glide.with(viewHolder.cardView.getContext())
                        .clear(viewHolder.recipeImage);
            }
        }

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}
