package de.hdodenhof.parallax.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import de.hdodenhof.parallax.R;
import de.hdodenhof.parallax.activities.MainActivity;
import de.hdodenhof.parallax.util.ParallaxHelper;

public class ParallaxListFragment extends ListFragment {

    private View mHeaderPlaceholder;
    private Drawable mActionBarBackgroundDrawable;
    private ParallaxHelper mParallaxHelper;
    private int mCurrentAlpha = 0;

    public ParallaxListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParallaxHelper = new ParallaxHelper((MainActivity) getActivity());
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mHeaderPlaceholder = inflater.inflate(R.layout.header_placeholder, null);
        mParallaxHelper.onCreateView(rootView, mHeaderPlaceholder);

        return rootView;
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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().addHeaderView(mHeaderPlaceholder);
        getListView().setOnScrollListener(mParallaxHelper);
        getListView().setAdapter(new SimpleArrayAdapter(getActivity()));
    }

    private class SimpleArrayAdapter extends ArrayAdapter<String> {
        public SimpleArrayAdapter(Context context) {
            super(context, R.layout.listitem);
        }

        @Override
        public int getCount() {
            return 50;
        }

        @Override
        public String getItem(int position) {
            return "This is list fragment row number " + position;
        }
    }
}
