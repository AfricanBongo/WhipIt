package com.africanbongo.whipit.controller.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.adapters.ExploreAdapter;
import com.africanbongo.whipit.controller.fragments.MyRecipesFragment;
import com.africanbongo.whipit.controller.fragments.ExploreRecipeFragment;
import com.africanbongo.whipit.controller.fragments.SearchRecipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private ExploreAdapter exploreAdapter;

    // Fragments used in activity
    private static MyRecipesFragment myRecipesFragment;
    private static SearchRecipeFragment searchRecipeFragment;
    private static ExploreRecipeFragment exploreRecipeFragment;

    // Text to show on the action bar
    String actionBarText = null;
    String exploreBarTitle = null;
    String myRecipesBarTitle = null;
    String searchRecipeBarTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove splash theme
        setTheme(R.style.AppTheme);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);

        // Add ToolBar to activity
        Toolbar mainToolBar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mainToolBar);

        // -------------------------------------------- Action Bar Titles -----------------------------------------------//

        // Emoticon unicode
        // Face savouring delicious food
        int sideTongueOutEmoticon = 0x1F60B;
        // Smiling face with open mouth and cold sweat
        int oneSweatEmoticon = 0x1F605;

        exploreBarTitle = "Now, what to cook?" + getEmoticonByUnicode(oneSweatEmoticon) +
                getEmoticonByUnicode(sideTongueOutEmoticon);

        // Smiling face with heart-shaped eyes emoticon
        int heartEyesEmoticon = 0x1F60D;
        // Drooling face emoticon
        int droolingFaceEmoticon = 0x1F924;

        myRecipesBarTitle = "My delicious collection..." + getEmoticonByUnicode(heartEyesEmoticon)
                + getEmoticonByUnicode(droolingFaceEmoticon);


        // Thinking face emoticon
        int thinkingFaceEmoticon = 0x1F914;

        searchRecipeBarTitle = "I want something more specific" + getEmoticonByUnicode(thinkingFaceEmoticon);

        // -------------------------------------------- Action Bar Titles -----------------------------------------------//

        // -------------------------------------------- Fragments Display ----------------------------------------------- //

        // Create new explore fragment and my recipes fragment
        // If the activity is being launched for the first time
        if (savedInstanceState == null) {
            exploreAdapter = new ExploreAdapter(navView);
            exploreRecipeFragment = new ExploreRecipeFragment(exploreAdapter);
            myRecipesFragment = new MyRecipesFragment();
            searchRecipeFragment = new SearchRecipeFragment();
        }

        // Configure the fragments and display the explore fragment first
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container, exploreRecipeFragment)
                .add(R.id.fragment_container, myRecipesFragment)
                .add(R.id.fragment_container, searchRecipeFragment)
                .hide(myRecipesFragment)
                .hide(searchRecipeFragment)
                .commit();

        // -------------------------------------------- Fragments Display ----------------------------------------------- //


        // Set action bar title
        actionBarText = exploreBarTitle;
        getSupportActionBar().setTitle(actionBarText);


        // Set listener methods for the navigation bar menu items
        navView.setOnNavigationItemSelectedListener(menuItem -> {
            displayFragment(menuItem);
            return true;
        });

        navView.setOnNavigationItemReselectedListener(menuItem ->
            displayFragment(menuItem));
    }

    public void displayFragment(MenuItem menuItem) {
        Fragment fragmentToShow = null;
        Fragment fragmentToHide = null;
        Fragment fragmentToHide2 = null;

        switch(menuItem.getItemId()) {
            case R.id.navigation_explore:
                // If the fragment was destroyed create another one
                if (!isSafeFragment(exploreRecipeFragment)) {
                    exploreRecipeFragment = new ExploreRecipeFragment(exploreAdapter);
                }

                // Show this fragment and hide the other ones
                fragmentToShow = exploreRecipeFragment;
                fragmentToHide = searchRecipeFragment;
                fragmentToHide2 = myRecipesFragment;

                // The title of the action bar
                actionBarText = exploreBarTitle;
                break;

            // Same process as above but with myRecipesFragment
            case R.id.navigation_my_recipes:
                if (!isSafeFragment(myRecipesFragment)) {
                    myRecipesFragment = new MyRecipesFragment();
                }

                fragmentToShow = myRecipesFragment;
                fragmentToHide = searchRecipeFragment;
                fragmentToHide2 = exploreRecipeFragment;

                actionBarText = myRecipesBarTitle;
                break;
            case R.id.navigation_search:
                if (!isSafeFragment(searchRecipeFragment)) {
                    searchRecipeFragment = new SearchRecipeFragment();
                }

                fragmentToShow = searchRecipeFragment;
                fragmentToHide = myRecipesFragment;
                fragmentToHide2 = exploreRecipeFragment;

                actionBarText = searchRecipeBarTitle;
        }

        // Show and hide fragments
        getSupportFragmentManager()
                .beginTransaction()
                .hide(fragmentToHide)
                .hide(fragmentToHide2)
                .show(fragmentToShow)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();

        // Change text of action bar
        getSupportActionBar().setTitle(actionBarText);
    }

    public static String getEmoticonByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // Check if a fragment is available
    public static boolean isSafeFragment( Fragment frag ) {
        return !(frag.isRemoving() || frag.getActivity() == null || frag.isDetached()
                || !frag.isAdded() || frag.getView() == null );
    }
}