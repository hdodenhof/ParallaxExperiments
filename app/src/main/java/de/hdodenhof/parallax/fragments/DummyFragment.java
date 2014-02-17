package de.hdodenhof.parallax.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import de.hdodenhof.parallax.R;

public class DummyFragment extends ListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dummy, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((ActionBarActivity) activity).getSupportActionBar().setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.ab_solid));
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
