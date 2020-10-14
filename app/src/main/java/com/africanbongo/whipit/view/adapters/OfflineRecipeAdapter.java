package com.africanbongo.whipit.view.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class OfflineRecipeAdapter extends RecyclerView.Adapter<OfflineRecipeAdapter.OfflineRecipeViewHolder> {
    public class OfflineRecipeViewHolder extends RecyclerView.ViewHolder {
        public OfflineRecipeViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public OfflineRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull OfflineRecipeViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
