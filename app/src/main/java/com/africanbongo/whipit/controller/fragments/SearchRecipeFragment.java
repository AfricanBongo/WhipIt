package com.africanbongo.whipit.controller.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.BaseColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.SearchView;
import androidx.cursoradapter.widget.SimpleCursorAdapter;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.adapters.SearchRecipeRecyclerViewAdapter;
import com.africanbongo.whipit.model.searchrecipe.SearchRecipeList;

import java.util.List;

/**
 * A fragment representing a list of search recipes.
 */
public class SearchRecipeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private SearchView recipeSearchView;

    // Searches for recipes after 3 characters have been entered in the search view
    public static final int SEARCH_QUERY_THRESHOLD = 3;


    private static final String[] sAutocompleteColNames = new String[] {
            BaseColumns._ID,                         // necessary for adapter
            SearchManager.SUGGEST_COLUMN_TEXT_1      // the full search term
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_recipe_list, container, false);

        // Set the adapter
        if (view != null) {
            Context context = view.getContext();
            recipeSearchView = view.findViewById(R.id.recipe_search_view);
            RecyclerView recyclerView = view.findViewById(R.id.search_recipe_list);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new SearchRecipeRecyclerViewAdapter(recipeSearchView));

            // Set up new suggestions adapter for the search view
            recipeSearchView.setSuggestionsAdapter(new SimpleCursorAdapter(
                    context, android.R.layout.simple_list_item_1, null,
                    new String[] {SearchManager.SUGGEST_COLUMN_TEXT_1},
                    new int[] {android.R.id.text1}
            ));

            // Set query text listeners for the search view
            recipeSearchView.setOnQueryTextListener(this);
        }
        return view;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        if (newText.length() >= SEARCH_QUERY_THRESHOLD) {
            new FetchAutoCompleteStrings(getContext()).execute(newText);
        } else {
            recipeSearchView.getSuggestionsAdapter().changeCursor(null);
        }

        return true;
    }

    public class FetchAutoCompleteStrings extends AsyncTask<String, Void, Cursor> {

        private Context context;

        public FetchAutoCompleteStrings(Context context) {
            this.context = context;
        }

        @Override
        protected Cursor doInBackground(String... strings) {
            MatrixCursor cursor = new MatrixCursor(sAutocompleteColNames);

            SearchRecipeList
                    .getInstance(context)
                    .loadAutoCompleteStrings(strings[0]);

            // Get auto complete strings
            List<String> autocompleteStrings = SearchRecipeList
                    .getInstance(context)
                    .getAutoCompleteStrings();

            // Parse autocomplete strings into the MatrixCursor
            for (int i = 0; i < autocompleteStrings.size(); i++) {
                String term = autocompleteStrings.get(i);

                Object[] row = new Object[] {i, term};
                cursor.addRow(row);
            }

            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            recipeSearchView.getSuggestionsAdapter().changeCursor(cursor);
        }
    }
}