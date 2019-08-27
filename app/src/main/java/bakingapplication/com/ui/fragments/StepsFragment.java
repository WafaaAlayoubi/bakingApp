package bakingapplication.com.ui.fragments;


import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
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
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment implements View.OnClickListener, ExoPlayer.EventListener{

    @BindView(R.id.exoplayerview)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.pb_loading_indicator3)
    ProgressBar mLoadingIndicator;

    @BindView(R.id.descreption_tv)
    TextView mDescreptionTv;

    @BindView(R.id.prev_btn)
    Button mprevBtn;

    @BindView(R.id.next_btn)
    Button mNextBtn;

    SimpleExoPlayer mExoPlayer;

    private List<Steps> steps;

    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private static final String TAG = StepsFragment.class.getSimpleName();

    List<Recipe> recipeList1 ;

    int recipePosition;
    int stepsPosition;

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
                 recipePosition = AllRecipesFragment.postionFrag;
                 stepsPosition = DetailsFragment.stepPosition;
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

    @OnClick(R.id.next_btn)
    void submitButton(View view) {
        int pos = stepsPosition+1;
        if (steps.size()> pos ) {
            stepsPosition++;
            onDestroy();
            generateDataList(recipeList1);
        }
        else
        {
            onDestroy();
            stepsPosition = 0;
            generateDataList(recipeList1);
        }
    }

    @OnClick(R.id.prev_btn)
    void submitButton2(View view) {
        int pos = stepsPosition-1;
        if ( pos >= 0 ) {
            stepsPosition--;
            onDestroy();
            generateDataList(recipeList1);
        }
        else
        {
            onDestroy();
            stepsPosition = steps.size()-1;
            generateDataList(recipeList1);
        }
    }

    private void generateDataList(List<Recipe> recipeList) {

        recipeList1 = recipeList;
        steps = recipeList.get(recipePosition).getSteps();

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
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
                Uri videouri = Uri.parse(videourl);
                DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
                ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
                MediaSource mediaSource = new ExtractorMediaSource(videouri, dataSourceFactory, extractorsFactory, null, null);
                exoPlayerView.setPlayer(mExoPlayer);
                mExoPlayer.prepare(mediaSource);
                mExoPlayer.setPlayWhenReady(true);

                initializeMediaSession();

            } catch (Exception e) {

            }
        }



    }

    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mMediaSession = new MediaSessionCompat(getActivity(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mMediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mMediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mMediaSession.setActive(true);

    }


    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    mExoPlayer.getCurrentPosition(), 1f);
        } else if((playbackState == ExoPlayer.STATE_READY)){
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    mExoPlayer.getCurrentPosition(), 1f);
        }



        mMediaSession.setPlaybackState(mStateBuilder.build());


    }

    @Override
    public void onClick(View view) {

    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {

            mExoPlayer.seekTo(0);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(!videourl.equals("")) {
            releasePlayer();
            mMediaSession.setActive(false);
        }
    }

    private void releasePlayer() {

        if(!videourl.equals("")){

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;

        }


    }
}
