package de.hdodenhof.parallax.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import de.hdodenhof.parallax.R;
import de.hdodenhof.parallax.activities.MainActivity;
import de.hdodenhof.parallax.util.ParallaxHelper;
import de.hdodenhof.parallax.widgets.ParallaxListView;

public class ParallaxListFragment extends Fragment {

    private View mHeaderPlaceholder;
    private ParallaxHelper mParallaxHelper;

    private ParallaxListView mListView;
    private SimpleArrayAdapter mAdapter;

    public ParallaxListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mParallaxHelper = new ParallaxHelper((MainActivity) getActivity(), ParallaxHelper.Type.LISTVIEW);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Simulate adapter data refresh
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.setSize(500);
            }
        }, 5000);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        mListView = (ParallaxListView) rootView.findViewById(android.R.id.list);

        mHeaderPlaceholder = inflater.inflate(R.layout.header_placeholder, null);
        mParallaxHelper.onCreateView(rootView, mHeaderPlaceholder);

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mListView.addHeaderView(mHeaderPlaceholder);
        mListView.setOnScrollListener(mParallaxHelper);

        mAdapter = new SimpleArrayAdapter(getActivity(), 100);
        mListView.setAdapter(mAdapter);
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
