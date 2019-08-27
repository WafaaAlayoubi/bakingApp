package bakingapplication.com.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bakingapplication.com.R;
import bakingapplication.com.adapters.RecipesAdapter;
import bakingapplication.com.adapters.StepsAdapter;
import bakingapplication.com.api.AppRetrofit;
import bakingapplication.com.api.AppServices;
import bakingapplication.com.models.Ingredients;
import bakingapplication.com.models.Recipe;
import bakingapplication.com.models.Steps;
import bakingapplication.com.rv.RecyclerTouchListener;
import bakingapplication.com.ui.activities.DetailsActivity;
import bakingapplication.com.ui.activities.StepsActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {

    public static int stepPosition;

    @BindView(R.id.details_lv)
    ListView listView;

    @BindView(R.id.scroll_v)
    ScrollView scrollView;

    @BindView(R.id.steps_rv)
    RecyclerView mRecipesRecyclerView;

    @BindView(R.id.pb_loading_indicator2)
    ProgressBar mLoadingIndicator;

    private List<Ingredients> ingredients;
    private List<Steps> steps;

    private StepsAdapter adapterRv;

    List<String> ingredientsList = new ArrayList<>() ;
    int position;


    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this,viewRoot);


        scrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                //replace this line to scroll up or down
                scrollView.scrollTo(0,0);
            }
        }, 100L);

        mLoadingIndicator.setVisibility(View.VISIBLE);


        AppServices service = AppRetrofit.getRetrofitInstance().create(AppServices.class);
        Call<List<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {

            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                mLoadingIndicator.setVisibility(View.GONE);
                generateDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

                mLoadingIndicator.setVisibility(View.GONE);
                Toast.makeText(getActivity() ,"Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });

        mRecipesRecyclerView.addOnItemTouchListener(
                new RecyclerTouchListener(getActivity(), mRecipesRecyclerView ,new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        stepPosition = position;
                        Intent intent = new Intent(getContext(), StepsActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongClick(View view, int position) {

                    }

                })
        );

        return viewRoot;
    }

    private void generateDataList(List<Recipe> recipeList) {

        position = AllRecipesFragment.postionFrag;

         ingredients =  recipeList.get(position).getIngredients();
         steps =  recipeList.get(position).getSteps();

         String ingredientsElement;
         Ingredients ingredientsObj;

         for (int i =0; i<ingredients.size();i++){
             ingredientsObj = ingredients.get(i);
             ingredientsElement = ingredientsObj.getIngredient() + "(" +ingredientsObj.getQuantity()+ingredientsObj.getMeasure()+")";
             ingredientsList.add(ingredientsElement);

         }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity().getApplicationContext(),
                android.R.layout.simple_list_item_1, ingredientsList);


        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            }
        });

        adapterRv = new StepsAdapter(getActivity(),steps);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecipesRecyclerView.setLayoutManager(layoutManager);
        mRecipesRecyclerView.setAdapter(adapterRv);

    }

}
