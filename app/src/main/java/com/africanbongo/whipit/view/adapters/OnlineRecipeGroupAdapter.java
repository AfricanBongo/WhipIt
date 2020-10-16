package com.africanbongo.whipit.view.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.model.interfaces.RecipeList;
import com.africanbongo.whipit.model.offline.OfflineRecipeList;
import com.africanbongo.whipit.model.online.OnlineRecipe;
import com.africanbongo.whipit.model.online.OnlineRecipeList;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


/*
Shows the recipe card views
 */

public class OnlineRecipeGroupAdapter extends RecyclerView.Adapter<OnlineRecipeGroupAdapter.OnlineRecipeViewHolder> {
    public class OnlineRecipeViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView recipeTitle;
        private ImageView recipeImage;
        private FloatingActionButton favoriteButton;

        @RequiresApi(api = Build.VERSION_CODES.M)
        public OnlineRecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.recipe_cardview);
            recipeTitle = itemView.findViewById(R.id.text_cardview);
            recipeImage = itemView.findViewById(R.id.image_cardview);
            recipeImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
            favoriteButton = itemView.findViewById(R.id.fab_download);

            favoriteButton.setOnClickListener(v -> {
                OnlineRecipe recipe = (OnlineRecipe) cardView.getTag();
                Drawable drawable;
                String toastString;
                Context context = v.getContext();

                // If the recipe was already downloaded delete it from my recipes
                if (OfflineRecipeList.getInstance(context).contains(recipe.getApiId())) {
                    OfflineRecipeList.getInstance(context).deleteRecipe(recipe.getApiId());
                    // Set icon to download image
                    drawable =  v.getResources().getDrawable(R.drawable.baseline_get_app_24);
                    toastString = "Removing saved recipe";
                }
                // Else download recipe
                else {
                    OfflineRecipeList.getInstance(context).saveRecipe(recipe);
                    // Set icon to done image
                    drawable = v.getResources().getDrawable(R.drawable.ic_baseline_done_24);
                    toastString = "Saving recipe";
                }

                Toast.makeText(context, toastString, Toast.LENGTH_LONG).show();
                favoriteButton.setImageDrawable(drawable);
            });
        }
    }

    // Context is used for the Volley Request Queue
    private Context context;

    private Drawable solidFavorite;
    private Drawable borderFavorite;

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

    @RequiresApi(api = Build.VERSION_CODES.M)
    @NonNull
    @Override
    public OnlineRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.online_recipe_card, parent, false);

        return new OnlineRecipeViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull OnlineRecipeViewHolder holder, int position) {
        OnlineRecipe recipe = (OnlineRecipe) recipeList.getRecipeList().get(position);
        holder.cardView.setTag(recipe);
        holder.recipeTitle.setText(recipe.getTitle());

        // If the recipe is already saved show a solid favourite button
        if (OfflineRecipeList.getInstance(context).contains(recipe.getApiId())) {
            holder.favoriteButton.setForeground(solidFavorite);
        } else {
            holder.favoriteButton.setForeground(borderFavorite);
        }

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
