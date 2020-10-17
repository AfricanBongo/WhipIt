package com.africanbongo.whipit.controller.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.adapters.MyRecipesAdapter;

/**
 * A fragment representing a list of offline recipes.
 */

public class MyRecipesFragment extends Fragment {

    private MyRecipesAdapter myRecipesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_recipes_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            if (myRecipesAdapter == null) {
                myRecipesAdapter = new MyRecipesAdapter(context);
            }

            recyclerView.setAdapter(myRecipesAdapter);
        }
        return view;
    }

    // Update the recipes saved
    public void updateList() {
        if (myRecipesAdapter != null) {
            myRecipesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        myRecipesAdapter.clearImages();
    }
}