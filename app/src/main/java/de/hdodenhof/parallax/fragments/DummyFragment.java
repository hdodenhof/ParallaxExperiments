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

public class DummyFragment extends ListFragment {

    private Drawable mActionBarBackgroundDrawable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dummy, container, false);
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
        mActionBarBackgroundDrawable.setAlpha(255);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
            return "This is dummy fragment row number " + position;
        }
    }
}
