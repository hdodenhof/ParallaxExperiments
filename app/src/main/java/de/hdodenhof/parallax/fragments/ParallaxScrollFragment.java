package de.hdodenhof.parallax.fragments;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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

    private Drawable mActionBarBackgroundDrawable;
    private ParallaxScrollView mScrollView;
    private ParallaxHelper mParallaxHelper;

    private int mCurrentAlpha = 0;

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActionBarBackgroundDrawable = ((MainActivity) activity).getActionBarBackgroundDrawable();
    }

    @Override
    public void onDetach() {
        mActionBarBackgroundDrawable = null;
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mParallaxHelper.onResume();
        mActionBarBackgroundDrawable.setAlpha(mCurrentAlpha);
    }

    @Override
    public void onPause() {
        mCurrentAlpha = mParallaxHelper.getCurrentAlpha();
        mParallaxHelper.onPause();
        super.onPause();
    }
}
