package com.africanbongo.whipit.controller.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.fragments.MyRecipesFragment;
import com.africanbongo.whipit.controller.fragments.ExploreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private static ExploreFragment exploreFragment;
    private static MyRecipesFragment myRecipesFragment;
    private static FragmentTransaction fragmentTransaction;

    // Text to show on the action bar
    String actionBarText = null;
    String exploreBarTitle = null;
    String myRecipesBarTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);

        // Add ToolBar to acitivity
        Toolbar mainToolBar = findViewById(R.id.toolbar_main);
        setSupportActionBar(mainToolBar);


        // -------------------------------------------- Action Bar Titles -----------------------------------------------//

        // Emoticon unicode
        // Face savouring delicious food
        int sideTongueOutEmoticon = 0x1F60B;
        // Smiling face with open mouth and cold sweat
        int oneSweatEmoticon = 0x1F605;

        exploreBarTitle = "Now, what to cook?" + getEmojiByUnicode(oneSweatEmoticon) +
                getEmojiByUnicode(sideTongueOutEmoticon);

        // Smiling face with heart-shaped eyes emoticon
        int heartEyesEmoticon = 0x1F60D;
        // Drooling face emoticon
        int droolingFaceEmoticon = 0x1F924;

        myRecipesBarTitle = "My delicious collection..." + getEmojiByUnicode(heartEyesEmoticon)
                + getEmojiByUnicode(droolingFaceEmoticon);

        // -------------------------------------------- Action Bar Titles -----------------------------------------------//

        // -------------------------------------------- Fragments Display ----------------------------------------------- //

        // Create new explore fragment and my recipes fragment
        // If the activity is being launched for the first time
        if (savedInstanceState == null) {
            if (exploreFragment == null) {
                exploreFragment = new ExploreFragment();
            }

            if (myRecipesFragment == null) {
                myRecipesFragment = new MyRecipesFragment();
            }

            if (fragmentTransaction == null) {
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
            }

            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, exploreFragment)
                    .add(R.id.fragment_container, myRecipesFragment)
                    .hide(myRecipesFragment)
                    .show(exploreFragment)
                    .commit();

            // Set action bar title
            actionBarText = exploreBarTitle;
            getSupportActionBar().setTitle(actionBarText);

            // -------------------------------------------- Fragments Display ----------------------------------------------- //
        }

        // Set listener methods for the navigation bar menu items
        navView.setOnNavigationItemSelectedListener(menuItem -> {
            displayFragment(menuItem);
            return true;
        });

        navView.setOnNavigationItemReselectedListener(menuItem -> {
            displayFragment(menuItem);
        });
    }

    public void displayFragment(MenuItem menuItem) {
        Fragment fragmentToShow = null;
        Fragment fragmentToHide = null;

        switch(menuItem.getItemId()) {
            case R.id.navigation_explore:
                fragmentToShow = exploreFragment;
                fragmentToHide = myRecipesFragment;
                actionBarText = exploreBarTitle;
                break;
            case R.id.navigation_my_recipes:
                fragmentToShow = myRecipesFragment;
                fragmentToHide = exploreFragment;
                actionBarText = myRecipesBarTitle;

                // Update my recipes adapter
                myRecipesFragment.updateList();
                break;
        }

        // Show and hide fragments
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .hide(fragmentToHide)
                .show(fragmentToShow)
                .commit();

        // Change text of action bar
        getSupportActionBar().setTitle(actionBarText);
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }
}