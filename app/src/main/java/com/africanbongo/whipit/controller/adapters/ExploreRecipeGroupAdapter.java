package com.africanbongo.whipit.controller.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.activities.DetailActivity;
import com.africanbongo.whipit.model.RecipeChannel;
import com.africanbongo.whipit.model.explorerecipe.ExploreRecipe;
import com.africanbongo.whipit.model.explorerecipe.ExploreRecipeList;
import com.africanbongo.whipit.model.interfaces.Recipe;
import com.africanbongo.whipit.model.interfaces.RecipeList;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;


/*
Shows the recipe card views
 */

public class ExploreRecipeGroupAdapter extends RecyclerView.Adapter<ExploreRecipeGroupAdapter.ExploreRecipeViewHolder> {
    public class ExploreRecipeViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView recipeTitle;
        private ImageView recipeImage;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public ExploreRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.recipe_cardview);
            recipeTitle = itemView.findViewById(R.id.text_cardview);
            recipeImage = itemView.findViewById(R.id.image_cardview);

            // When the image is clicked
            // Open the recipe in the DetailActivity
            recipeImage.setOnClickListener(v -> {
                Recipe recipe = (Recipe) cardView.getTag();

                // Send intent to DetailActivity
                Intent intent = new Intent(v.getContext(), DetailActivity.class);

                // Put recipe in the channel
                RecipeChannel.getRecipeChannel().putRecipe(recipe);

                // Start DetailActivity
                v.getContext().startActivity(intent);
            });
        }
    }

    // Context is used for the Volley Request Queue
    private Context context;

    // Get views, used for stopping shimmer animations
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout parentView;

    // Data source of the adapter
    private RecipeList recipeList;

    public ExploreRecipeGroupAdapter(Context context, String recipeGroup, ShimmerFrameLayout shimmerFrameLayout,
                                     LinearLayout linearLayout) {
        this.context = context;
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.parentView = linearLayout;

        // Set visibility to be initially gone as shimmer animation displays
        parentView.setVisibility(View.GONE);

        // Create new recipe List for the adapter
        recipeList = new ExploreRecipeList(recipeGroup, this, context);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public ExploreRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.explore_recipe_card, parent, false);

        return new ExploreRecipeViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull ExploreRecipeViewHolder holder, int position) {
        ExploreRecipe recipe = (ExploreRecipe) recipeList.getRecipeList().get(position);
        holder.cardView.setTag(recipe);
        holder.recipeTitle.setText(recipe.getTitle());

        // Try to retrieve the image of the card, apply shimmer animation
        Glide.with(context)
                .load(recipe.getImageURL())
                .placeholder(context.getDrawable(R.drawable.placeholder))
                .error(context.getDrawable(R.drawable.placeholder))
                .into(holder.recipeImage);
    }

    // Stops the shimmer animation and loads the data into the recycler view
    public void stopLoadingAnimations() {
        new Handler().postDelayed(() -> {
            if (shimmerFrameLayout != null) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                parentView.setVisibility(View.VISIBLE);
            }
        }, 500);

    }

    @Override
    public int getItemCount() {
        return recipeList.getRecipeList().size();
    }
}
