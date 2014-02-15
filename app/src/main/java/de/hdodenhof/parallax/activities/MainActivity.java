package de.hdodenhof.parallax.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import de.hdodenhof.parallax.R;
import de.hdodenhof.parallax.fragments.DummyFragment;
import de.hdodenhof.parallax.fragments.ParallaxListFragment;
import de.hdodenhof.parallax.fragments.ParallaxScrollFragment;

public class MainActivity extends ActionBarActivity {

    private Drawable mActionBarBackgroundDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new ParallaxListFragment()).commit();
        }

        mActionBarBackgroundDrawable = getResources().getDrawable(R.drawable.ab_solid);
        mActionBarBackgroundDrawable.setAlpha(0);

        getSupportActionBar().setBackgroundDrawable(mActionBarBackgroundDrawable);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ParallaxListFragment()).addToBackStack(null).commit();
                return true;
            case R.id.action_scroll:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new ParallaxScrollFragment()).addToBackStack(null).commit();
                return true;
            case R.id.action_dummy:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, new DummyFragment()).addToBackStack(null).commit();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public Drawable getActionBarBackgroundDrawable() {
        return mActionBarBackgroundDrawable;
    }

}
