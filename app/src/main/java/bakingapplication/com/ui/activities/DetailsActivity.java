package bakingapplication.com.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import bakingapplication.com.R;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bakingapplication.com.R;
import bakingapplication.com.models.Ingredients;
import bakingapplication.com.models.Recipe;
import bakingapplication.com.ui.fragments.AllRecipesFragment;
import bakingapplication.com.ui.fragments.DetailsFragment;
import bakingapplication.com.ui.fragments.StepsFragment;
import bakingapplication.com.widget.AppWidget;
import bakingapplication.com.widget.AppWidgetService;
import bakingapplication.com.widget.LoremViewsFactory;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.OnImageClickListener{

    public static boolean mTwoPane;
    Recipe mRecipe;
    public static List<String> ingredientsList2 = new ArrayList<>() ;
    private List<Ingredients> ingredients;

    public static ArrayAdapter<String> adapterWidget;

    public static ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        setTitle("Details");

        String word= getIntent().getStringExtra(AppWidget.EXTRA_WORD);

        if(findViewById(R.id.android_me_linear_layout) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPane = true;


            if(savedInstanceState == null) {
                // In two-pane mode, add initial BodyPartFragments to the screen
                FragmentManager fragmentManager = getSupportFragmentManager();

                // Creating a new head fragment
                StepsFragment stepsFragment = new StepsFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.steps_container, stepsFragment)
                        .commit();

            }
        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPane = false;
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add_to_widget) {

            Ingredients ingredientsObj;
            mRecipe = AllRecipesFragment.mRecipes.get(AllRecipesFragment.postionFrag);
            ingredients = mRecipe.getIngredients();
            AppWidget.name = mRecipe.getName();
            String ingredientsElement;

            ingredientsList2.clear();

            for (int i =0; i<ingredients.size();i++){
                ingredientsObj = ingredients.get(i);
                ingredientsElement = ingredientsObj.getIngredient() + "(" +ingredientsObj.getQuantity()+ingredientsObj.getMeasure()+")";
                ingredientsList2.add(ingredientsElement);
            }


            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
            int [] appWidgetIds =appWidgetManager.getAppWidgetIds(new ComponentName(this,AppWidget.class));

//            adapterWidget = new ArrayAdapter<>(this,
//                    android.R.layout.simple_list_item_1, ingredientsList2);
//            listView = findViewById(R.id.widget_listview);


            AppWidget appWidget = new AppWidget();
            appWidget.onUpdate(this,appWidgetManager,appWidgetIds);
            return true;
        } else
            return super.onOptionsItemSelected(item);

    }

    @Override
    public void onImageSelected(int position) {
        if (mTwoPane) {
            // Create two=pane interaction

            if (findViewById(R.id.android_me_linear_layout) != null) {
                // This LinearLayout will only initially exist in the two-pane tablet case
                mTwoPane = true;

                FragmentManager fragmentManager = getSupportFragmentManager();

                // Creating a new head fragment
                StepsFragment stepsFragment = new StepsFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.steps_container, stepsFragment)
                        .commit();

            } else {
                // We're in single-pane mode and displaying fragments on a phone in separate activities
                mTwoPane = false;

            }


        }
    }

}