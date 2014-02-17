package de.hdodenhof.parallax.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.hdodenhof.parallax.R;
import de.hdodenhof.parallax.activities.MainActivity;
import de.hdodenhof.parallax.util.ParallaxHelper;
import de.hdodenhof.parallax.widgets.ParallaxScrollView;

public class ParallaxScrollFragment extends Fragment {

    private ParallaxScrollView mScrollView;
    private ParallaxHelper mParallaxHelper;

    public ParallaxScrollFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParallaxHelper = new ParallaxHelper((MainActivity) getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mScrollView.setOnScrollChangedListener(mParallaxHelper);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scroll, container, false);

        mScrollView = (ParallaxScrollView) rootView.findViewById(R.id.scroll);
        mParallaxHelper.onCreateView(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mParallaxHelper.onResume();
    }

    @Override
    public void onPause() {
        mParallaxHelper.onPause();
        super.onPause();
    }
}
