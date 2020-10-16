package com.africanbongo.whipit.view.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.view.fragments.MyRecipesFragment;
import com.africanbongo.whipit.view.fragments.ExploreFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private static Fragment exploreFragment;
    private static Fragment myRecipesFragment;
    private static FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);

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
                break;
            case R.id.navigation_my_recipes:
                fragmentToShow = myRecipesFragment;
                fragmentToHide = exploreFragment;
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .hide(fragmentToHide)
                .show(fragmentToShow)
                .commit();
    }
}