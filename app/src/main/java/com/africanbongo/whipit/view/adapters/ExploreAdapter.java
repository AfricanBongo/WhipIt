package com.africanbongo.whipit.view.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.model.StringOps;
import com.facebook.shimmer.ShimmerFrameLayout;


/*
The adapter that shows all the recipe groups view holders
 */
public class ExploreAdapter extends RecyclerView.Adapter<ExploreAdapter.ExploreViewHolder> {
    public class ExploreViewHolder extends RecyclerView.ViewHolder {
        private TextView recipeGroupText;
        private LinearLayout parentView;
        private RecyclerView recyclerView;
        private ShimmerFrameLayout shimmerFrameLayout;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public ExploreViewHolder(@NonNull View itemView) {
            super(itemView);
            // Get recipe group and recycle the string
            String recipeGroup = StringOps.getInstance(itemView).getRecipeGroup();

            // Set up the views of the view holder
            parentView = itemView.findViewById(R.id.group_container);
            recipeGroupText = itemView.findViewById(R.id.group_text);
            recipeGroupText.setText(recipeGroup);
            recyclerView = itemView.findViewById(R.id.group_recycleview);
            shimmerFrameLayout = itemView.findViewById(R.id.shimmer_frame);

            // Set up the recycler with an adapter to display sub-lists
            recyclerView.setAdapter(
                    new OnlineRecipeGroupAdapter(itemView.getContext(), recipeGroup,
                            shimmerFrameLayout, parentView)
            );

            // Configure layout manager and add to recycler view
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    private View view;

    public ExploreAdapter(View view) {
        this.view = view;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public ExploreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_explore, parent, false);

        return new ExploreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreViewHolder holder, int position) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public int getItemCount() {
        return StringOps.getInstance(view).getRecipeGroupSize();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
