package com.africanbongo.whipit.view.adapters;

import android.content.Context;
import android.os.Handler;
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
import com.africanbongo.whipit.model.interfaces.RecipeList;
import com.africanbongo.whipit.model.online.OnlineRecipe;
import com.africanbongo.whipit.model.online.OnlineRecipeList;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;


/*
Shows the recipe card views
 */

public class OnlineRecipeGroupAdapter extends RecyclerView.Adapter<OnlineRecipeGroupAdapter.OnlineRecipeViewHolder> {
    public class OnlineRecipeViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView recipeTitle;
        private ImageView recipeImage;

        public OnlineRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.recipe_cardview);
            recipeTitle = itemView.findViewById(R.id.text_cardview);
            recipeImage = itemView.findViewById(R.id.image_cardview);
            recipeImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    // Context is used for the Volley Request Queue
    private Context context;

    // Get views, used for stopping shimmer animations
    private ShimmerFrameLayout shimmerFrameLayout;
    private LinearLayout parentView;

    // Data source of the adapter
    private RecipeList recipeList;

    public OnlineRecipeGroupAdapter(Context context, String recipeGroup, ShimmerFrameLayout shimmerFrameLayout,
                                    LinearLayout linearLayout) {
        this.context = context;
        this.shimmerFrameLayout = shimmerFrameLayout;
        this.parentView = linearLayout;

        // Set visibility to be initially gone as shimmer animation displays
        parentView.setVisibility(View.GONE);

        recipeList = new OnlineRecipeList(recipeGroup, this, context);
    }

    @NonNull
    @Override
    public OnlineRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_card, parent, false);

        return new OnlineRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnlineRecipeViewHolder holder, int position) {
        OnlineRecipe recipe = (OnlineRecipe) recipeList.getRecipeList().get(position);
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
