package com.africanbongo.whipit.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.model.RecipeChannel;
import com.africanbongo.whipit.model.interfaces.Recipe;
import com.bumptech.glide.Glide;

import java.net.URI;

public class DetailActivity extends AppCompatActivity {

    // Recipe being displayed in the fragment
    private Recipe currentRecipe;

    // Views of the activity
    private ImageView imageView;
    private Button cookMeButton;
    private TextView firstHalfIngredients;
    private TextView secondHalfIngredients;
    private TextView summaryTextView;
    private TextView instructionsTextView;
    private TextView sourceUrlClickableText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState == null) {
            // Get recipe from channel
            currentRecipe = RecipeChannel.getRecipeChannel().getCurrentRecipe();
        }

        // Get views from layout
        Toolbar toolbar = findViewById(R.id.toolbar_detail);
        setSupportActionBar(toolbar);

        // Set tool bar title to recipe title
        getSupportActionBar().setTitle(currentRecipe.getTitle());


        // Get image view and load recipe image
        imageView = findViewById(R.id.detail_image_view);
        Glide
                .with(getApplicationContext())
                .load(currentRecipe.getImageURL())
                .placeholder(R.drawable.grey_box_placeholder)
                .into(imageView);

        // Show the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_go_to_source:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, currentRecipe.getSourceURL());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}