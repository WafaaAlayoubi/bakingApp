package bakingapplication.com.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import bakingapplication.com.R;
import bakingapplication.com.adapters.RecipesAdapter;
import bakingapplication.com.api.AppRetrofit;
import bakingapplication.com.api.AppServices;
import bakingapplication.com.models.Recipe;
import bakingapplication.com.rv.MyDividerItemDecoration;
import bakingapplication.com.rv.RecyclerTouchListener;
import bakingapplication.com.ui.activities.DetailsActivity;
import bakingapplication.com.ui.activities.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllRecipesFragment extends Fragment {

    @BindView(R.id.all_recipes_rv)
    RecyclerView mRecipesRecyclerView;

    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;

    public static int postionFrag;



    public static List<Recipe> mRecipes;
    private Unbinder unbinder;
    private RecipesAdapter adapter;


    public AllRecipesFragment() {
        // Required empty public constructor
    }

    public interface OnRecipeClickListener {
        void onRecipeSelected(Recipe recipe);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_all_recipes, container, false);
       ButterKnife.bind(this,viewRoot);

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

                        postionFrag = position;

                        Intent intent = new Intent(getContext(), DetailsActivity.class);
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

        mRecipes = recipeList;

        if (MainActivity.mTwoPaneMain == false) {
            adapter = new RecipesAdapter(getActivity(), recipeList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecipesRecyclerView.setLayoutManager(layoutManager);
            mRecipesRecyclerView.setAdapter(adapter);
        }
        else {
            adapter = new RecipesAdapter(getActivity(), recipeList);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            mRecipesRecyclerView.setLayoutManager(layoutManager);
            mRecipesRecyclerView.setAdapter(adapter);
        }
    }



}
