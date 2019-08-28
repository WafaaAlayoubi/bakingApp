package bakingapplication.com.ui.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.test.espresso.IdlingResource;

import android.os.Bundle;

import bakingapplication.com.R;
import bakingapplication.com.RecipesIdlingResource;
import bakingapplication.com.ui.fragments.AllRecipesFragment;
import bakingapplication.com.ui.fragments.StepsFragment;

public class MainActivity extends AppCompatActivity{

    public static boolean mTwoPaneMain;

    @Nullable
    private RecipesIdlingResource idlingResource;



    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Recipes");

        if(findViewById(R.id.tablet_main) != null) {
            // This LinearLayout will only initially exist in the two-pane tablet case
            mTwoPaneMain = true;


        } else {
            // We're in single-pane mode and displaying fragments on a phone in separate activities
            mTwoPaneMain = false;
        }

        AllRecipesFragment allRecipesFragment = new AllRecipesFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.main_fragment, allRecipesFragment)
                .commit();

    }
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new RecipesIdlingResource();
        }
        return idlingResource;
    }


}
