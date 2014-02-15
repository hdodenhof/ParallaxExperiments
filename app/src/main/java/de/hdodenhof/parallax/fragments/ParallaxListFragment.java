package de.hdodenhof.parallax.fragments;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import de.hdodenhof.parallax.R;
import de.hdodenhof.parallax.activities.MainActivity;
import de.hdodenhof.parallax.util.ParallaxHelper;
import de.hdodenhof.parallax.widgets.ParallaxListView;

public class ParallaxListFragment extends ListFragment {

    private View mHeaderPlaceholder;
    private Drawable mActionBarBackgroundDrawable;
    private ParallaxHelper mParallaxHelper;

    private ParallaxListView mListView;
    private SimpleArrayAdapter mAdapter;

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

        // Simulate adapter data refresh
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mListView.isIdle()){
                    mAdapter.setSize(500);
                } else {
                    mListView.setOnIdleListener(new ParallaxListView.OnIdleListener() {
                        @Override
                        public void onIdle() {
                            mAdapter.setSize(500);
                            mListView.setOnIdleListener(null);
                        }
                    });
                }
            }
        }, 5000);

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

        mListView = (ParallaxListView) getListView();

        mListView.addHeaderView(mHeaderPlaceholder);
        mListView.setOnScrollListener(mParallaxHelper);

        mAdapter = new SimpleArrayAdapter(getActivity(), 100);
        mAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                mParallaxHelper.reset();
            }
        });
        getListView().setAdapter(mAdapter);
    }

    private class SimpleArrayAdapter extends ArrayAdapter<String> {

        private int mSize;

        public SimpleArrayAdapter(Context context, int size) {
            super(context, R.layout.listitem);
            mSize = size;
        }

        public void setSize(int size){
            mSize = size;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mSize;
        }

        @Override
        public String getItem(int position) {
            return "This is list fragment row number " + position;
        }
    }
}
