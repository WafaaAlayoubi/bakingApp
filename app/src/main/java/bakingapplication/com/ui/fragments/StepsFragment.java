package bakingapplication.com.ui.fragments;


import android.net.Uri;
import android.os.Bundle;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.util.List;

import bakingapplication.com.R;
import bakingapplication.com.adapters.StepsAdapter;
import bakingapplication.com.api.AppRetrofit;
import bakingapplication.com.api.AppServices;
import bakingapplication.com.models.Ingredients;
import bakingapplication.com.models.Recipe;
import bakingapplication.com.models.Steps;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    @BindView(R.id.exoplayerview)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.pb_loading_indicator3)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.descreption_tv)
    TextView mDescreptionTv;

    SimpleExoPlayer exoPlayer;

    private List<Steps> steps;

    String videourl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";

    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this, viewRoot);

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
                Toast.makeText(getActivity(), "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });


        return viewRoot;
    }

    private void generateDataList(List<Recipe> recipeList) {

        int recipePosition = AllRecipesFragment.postionFrag;

        steps = recipeList.get(recipePosition).getSteps();

        int stepsPosition = DetailsFragment.stepPosition;

        videourl = steps.get(stepsPosition).getVideoURL();

        mDescreptionTv.setText(steps.get(stepsPosition).getDescription());

        if (videourl.equals("")){
            exoPlayerView.setVisibility(View.GONE);
        }
        else {
            exoPlayerView.setVisibility(View.VISIBLE);
            try {
                BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
                TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
                exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
                Uri videouri = Uri.parse(videourl);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
                exoPlayerView.setPlayer(exoPlayer);
                exoPlayer.prepare(mediaSource);
                exoPlayer.setPlayWhenReady(true);

            } catch (Exception e) {

            }
        }



    }

    @Override
    public void onPause() {
        super.onPause();

            if (player != null)
                player.setPlayWhenReady(false);

    }
}
