package com.africanbongo.whipit.view.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.view.fragments.OfflineRecipeFragment;
import com.africanbongo.whipit.view.fragments.OnlineRecipeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navView;
    private static Fragment exploreFragment;
    private static Fragment myRecipesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navView = findViewById(R.id.nav_view);

        // Create new explore fragment if the activity is being launched for the first time
        if (savedInstanceState == null) {
            if (exploreFragment == null) {
                exploreFragment = new OnlineRecipeFragment();
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_container, exploreFragment)
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
        Fragment fragment = null;

        switch(menuItem.getItemId()) {
            case R.id.navigation_explore:
                if (exploreFragment == null) {
                    exploreFragment = new OnlineRecipeFragment();
                }
                fragment = exploreFragment;
                break;
            case R.id.navigation_my_recipes:
                if (myRecipesFragment == null) {
                    myRecipesFragment = new OfflineRecipeFragment();
                }
                fragment = myRecipesFragment;
                break;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}