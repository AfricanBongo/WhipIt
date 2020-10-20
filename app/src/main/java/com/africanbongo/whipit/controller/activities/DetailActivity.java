package com.africanbongo.whipit.controller.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.africanbongo.whipit.R;
import com.africanbongo.whipit.controller.customviews.NumberedView;
import com.africanbongo.whipit.model.RecipeChannel;
import com.africanbongo.whipit.model.interfaces.Recipe;
import com.africanbongo.whipit.model.myrecipe.MyRecipeList;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;

public class DetailActivity extends AppCompatActivity {

    // Recipe being displayed in the fragment
    private Recipe currentRecipe;

    // Views of the activity
    private ImageView imageView;
    private Button cookMeButton;
    private TextView servingsTextView;
    private TextView summaryTextView;
    private LinearLayout stepsLayout;
    private LinearLayout ingredientsLayout;

    // Used to download recipes
    private MenuItem downloadItem;

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

        servingsTextView = findViewById(R.id.servings);

        summaryTextView = findViewById(R.id.summary);
        stepsLayout = findViewById(R.id.steps_layout);
        ingredientsLayout = findViewById(R.id.ingredients_layout);

        // Fill views with appropriate information
        servingsTextView.setText("for " + currentRecipe.getServings() + " servings");
        fillIngredients();
        fillSummary();
        fillSteps();

        // Set tool bar title to recipe title
        getSupportActionBar().setTitle(currentRecipe.getTitle());

        // Get image view and load recipe image
        imageView = findViewById(R.id.detail_image_view);
        Glide
                .with(getApplicationContext())
                .load(currentRecipe.getImageURL())
                .placeholder(R.drawable.grey_box_placeholder)
                .into(imageView);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        // Configure the download button
        downloadItem = menu.findItem(R.id.action_download);

        Drawable downloadIcon = null;
        String downloadText = null;

        boolean saved = MyRecipeList
                .getInstance(getApplicationContext())
                .contains(currentRecipe.getApiId());

        // Set the download icon and text if the recipe has been downloaded or not
        if (saved) {
            downloadIcon = getDrawable(R.drawable.ic_baseline_done_24);
            downloadText = getString(R.string.delete);
        } else {
            downloadIcon = getDrawable(R.drawable.baseline_get_app_24);
            downloadText = getString(R.string.download);
        }

        // Set icon and text
        downloadItem.setIcon(downloadIcon).setTitle(downloadText);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = null;
        switch (item.getItemId()) {
            // When the web menu item is clicked, open web recipe
            case R.id.action_go_to_source:
                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentRecipe.getSourceURL()));
                break;
            case R.id.action_share:
                String shareText = "Hey, I loved this recipe" +
                        " and I want you to check out this recipe, \"" + currentRecipe.getTitle() +
                        "\"" + " at:\n" + currentRecipe.getSourceURL();
                intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, shareText);
                break;
            case R.id.action_download:
                downloadRecipe();
                break;
        }

        if (intent != null) {
            // Start the activity
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    // Responds to when the download item is clicked
    public void downloadRecipe() {
        boolean saved = MyRecipeList
                .getInstance(getApplicationContext())
                .contains(currentRecipe.getApiId());

        Drawable downloadIcon = null;
        String downloadText = null;

        // The recipe is already saved, delete it, and icon = download, title = download recipe
        if (saved) {
            MyRecipeList
                    .getInstance(getApplicationContext())
                    .deleteRecipe(currentRecipe.getApiId());

            downloadIcon = getDrawable(R.drawable.baseline_get_app_24);
            downloadText = getString(R.string.download);

            String deleteText = "\"" + currentRecipe.getTitle() + "\"" +
                    " is being deleted";

            // Show snack bar
            Snackbar snackbar = Snackbar.make(findViewById(R.id.detail_coordinator),
                    deleteText, Snackbar.LENGTH_SHORT);

            // Save recipe if undo is selected
            snackbar.setAction("Undo", v -> {
                        MyRecipeList.getInstance(getApplicationContext()).saveRecipe(currentRecipe);
                        // Change the icon and title of the downloadItem
                        downloadItem
                                .setIcon(getDrawable(R.drawable.ic_baseline_done_24))
                                .setTitle(getString(R.string.delete));
                    });

            snackbar.show();
        }
        // Otherwise save it, and icon = downloaded/done, title = remove recipe
        else {
            MyRecipeList
                    .getInstance(getApplicationContext())
                    .saveRecipe(currentRecipe);

            downloadIcon = getDrawable(R.drawable.ic_baseline_done_24);
            downloadText = getString(R.string.delete);

            // Show toast to user
            String savedText = "\"" + currentRecipe.getTitle() + "\"" +
                    " has been saved";

            Toast.makeText(getApplicationContext(), savedText, Toast.LENGTH_SHORT)
                    .show();
        }

        // Change the icon and download text
        downloadItem.setIcon(downloadIcon).setTitle(downloadText);
    }
    // Fill ingredient columns with text
    public void fillIngredients() {
        String[] ingredients = currentRecipe.getIngredients().split("\t");

        for (int i = 0; i < ingredients.length; i++) {
            // Create step view and add to linear layout
            NumberedView numberedView = new NumberedView(getApplicationContext(), i + 1, ingredients[i]);

            ingredientsLayout.addView(numberedView.getStepView());
        }
    }

    // Fill summary text view from HTML text
    public void fillSummary() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            summaryTextView.setText(Html.fromHtml(currentRecipe.getSummary(),Html.FROM_HTML_MODE_LEGACY));
        } else {
            summaryTextView.setText(Html.fromHtml(currentRecipe.getSummary()));
        }
    }

    public void fillSteps() {
        String[] steps = currentRecipe.getSteps().split("\t");

        for (int i = 0; i < steps.length; i++) {
            // Create step view and add to linear layout
            NumberedView numberedView = new NumberedView(getApplicationContext(), i + 1, steps[i]);
            stepsLayout.addView(numberedView.getStepView());
        }
    }
}