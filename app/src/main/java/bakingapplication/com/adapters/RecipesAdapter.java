package bakingapplication.com.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import bakingapplication.com.R;
import bakingapplication.com.models.Recipe;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.MyViewHolder>{
    private Context context;
    private List<Recipe> recipeList;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.recipe_name_tv)
        TextView mRecipeName;

        @BindView(R.id.servings_tv)
        TextView mRecipeServings;

        public MyViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this,view);


        }
    }


    public RecipesAdapter(Context context, List<Recipe> recipeList) {
        this.context = context;
        this.recipeList = recipeList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("ResourceType") View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list, parent, false);



        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Recipe getRecipe = recipeList.get(position);
        holder.mRecipeName.setText(getRecipe.getName());
        holder.mRecipeServings.setText("servings: "+getRecipe.getServings());
    }

    @Override
    public int getItemCount() {

        return recipeList.size();
    }

}
