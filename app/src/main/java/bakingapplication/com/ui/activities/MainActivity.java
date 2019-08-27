package bakingapplication.com.ui.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import bakingapplication.com.R;
import bakingapplication.com.ui.fragments.AllRecipesFragment;

public class MainActivity extends AppCompatActivity{

    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AllRecipesFragment allRecipesFragment = new AllRecipesFragment();

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.main_fragment, allRecipesFragment)
                .commit();

    }


}
