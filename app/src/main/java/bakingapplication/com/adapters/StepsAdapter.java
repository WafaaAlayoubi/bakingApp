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
import bakingapplication.com.models.Steps;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.MyViewHolder> {

    private Context context;
    private List<Steps> stepsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.index_step_tv)
        TextView mStepIndex;

        @BindView(R.id.step_tv)
        TextView mStepName;

        public MyViewHolder(final View view) {
            super(view);
            ButterKnife.bind(this,view);


        }
    }


    public StepsAdapter(Context context, List<Steps> stepsList) {
        this.context = context;
        this.stepsList = stepsList;
    }

    @Override
    public StepsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        @SuppressLint("ResourceType") View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_list, parent, false);



        return new StepsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(StepsAdapter.MyViewHolder holder, int position) {
        Steps getStep = stepsList.get(position);
        holder.mStepIndex.setText(position+". ");
        holder.mStepName.setText(getStep.getShortDescription());
    }

    @Override
    public int getItemCount() {

        return stepsList.size();
    }
}
