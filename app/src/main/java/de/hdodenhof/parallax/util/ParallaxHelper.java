package de.hdodenhof.parallax.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ScrollView;

import de.hdodenhof.parallax.R;
import de.hdodenhof.parallax.activities.MainActivity;
import de.hdodenhof.parallax.widgets.ParallaxScrollView;

public class ParallaxHelper implements ParallaxScrollView.OnScrollChangedListener, AbsListView.OnScrollListener {

    private MainActivity mActivity;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private View mHeaderPlaceholder;

    private boolean handleResume = false;
    private boolean headerVisible = true;

    private int mHeaderHeight;
    private int mLastDampedScroll;
    private int mCurrentAlpha = 0;
    private int mHeaderTop = 0;

    public ParallaxHelper(MainActivity activity){
        mActivity = activity;
        mHeaderHeight = mActivity.getResources().getDimensionPixelSize(R.dimen.header_height);
    }

    public void onCreateView(View rootView){
        onCreateView(rootView, null);
    }

    public void onCreateView(View rootView, View headerPlaceholder){
        mActionBarBackgroundDrawable = mActivity.getActionBarBackgroundDrawable();
        mHeader = rootView.findViewById(R.id.header);
        mHeaderPlaceholder = headerPlaceholder;
    }

    public void onResume(){

    }

    public void onPause(){
        handleResume = true;
    }

    public int getCurrentAlpha() {
        return mCurrentAlpha;
    }

    @Override
    public void onScrollChanged(ScrollView view, int l, int t, int oldl, int oldt) {
        if (!headerVisible && handleResume) {
            mHeader.offsetTopAndBottom(mHeaderTop);
            mHeaderTop = 0;
        }
        handleResume = false;

        if (t < mHeaderHeight) {
            headerVisible = true;
        } else {
            headerVisible = false;
        }

        handleScroll(t);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        View topChild = view.getChildAt(0);

        if (topChild == null) {
            // List is not initialized
            return;
        } else if (topChild == mHeaderPlaceholder) {
            // Header is visible
            if (!headerVisible && handleResume) {
                mHeader.offsetTopAndBottom(mHeaderTop);
                mHeaderTop = 0;
            }
            handleResume = false;
            headerVisible = true;

            handleScroll(-topChild.getTop());
        } else {
            // Header is invisible
            if (headerVisible) {
                mHeaderTop = mHeader.getTop();
            }
            headerVisible = false;
            mActionBarBackgroundDrawable.setAlpha(255);
        }
    }

    private void handleScroll(int scrollPosition) {
        // ActionBar alpha
        final int headerHeight = mHeader.getHeight() - mActivity.getSupportActionBar().getHeight();
        final float ratio = (float) Math.min(Math.max(scrollPosition, 0), headerHeight) / headerHeight;
        final int newAlpha = (int) (ratio * 255);

        mActionBarBackgroundDrawable.setAlpha(newAlpha);
        mCurrentAlpha = newAlpha;

        // parallax
        int dampedScroll = (int) (scrollPosition * 0.5f);
        int offset = mLastDampedScroll - dampedScroll;
        mLastDampedScroll = dampedScroll;

        mHeader.offsetTopAndBottom(offset);
        mHeaderTop = mHeader.getTop();
    }

}
