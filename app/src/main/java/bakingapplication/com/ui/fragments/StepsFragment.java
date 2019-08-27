package bakingapplication.com.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import bakingapplication.com.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepsFragment extends Fragment {

    private lateinit var videoView: PlayerView

    @BindView(R.id.playerView)
    pla mPlayerView;


    public StepsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_steps, container, false);
        ButterKnife.bind(this,viewRoot);


        return viewRoot;
    }

}
