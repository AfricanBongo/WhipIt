package com.africanbongo.whipit.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.model.offline.OfflineRecipe;
import com.africanbongo.whipit.model.offline.OfflineRecipeList;
import com.bumptech.glide.Glide;

public class MyRecipesAdapter extends RecyclerView.Adapter<MyRecipesAdapter.MyRecipesViewHolder> {
    public class MyRecipesViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private ImageView recipeImage;
        private TextView recipeTitle;

        public MyRecipesViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.recipe_cardview_offline);
            recipeImage = itemView.findViewById(R.id.image_cardview_offline);
            recipeTitle = itemView.findViewById(R.id.text_cardview_offline);
        }
    }

    private Context context;

    public MyRecipesAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
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
        OfflineRecipe recipe = (OfflineRecipe) OfflineRecipeList
                .getInstance(context)
                .getRecipeList()
                .get(position);

        // Try to retrieve image from storage
        Glide
                .with(context)
                .load(recipe.getImageURI())
                .error(context.getDrawable(R.drawable.placeholder))
                .into(holder.recipeImage);

        holder.recipeTitle.setText(recipe.getTitle());
    }

    @Override
    public int getItemCount() {
        return OfflineRecipeList
                .getInstance(context)
                .getRecipeList()
                .size();
    }
}
