package com.africanbongo.whipit.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.adapters.ExploreAdapter;

/**
 * A fragment representing a list of Items.
 */
public class ExploreRecipeFragment extends Fragment {

    private ExploreAdapter exploreAdapter;

    public ExploreRecipeFragment(@NonNull ExploreAdapter exploreAdapter) {
        this.exploreAdapter = exploreAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(exploreAdapter);
        }
        return view;
    }
}