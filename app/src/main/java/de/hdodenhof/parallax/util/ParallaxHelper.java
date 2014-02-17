package de.hdodenhof.parallax.util;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.ScrollView;

import de.hdodenhof.parallax.R;
import de.hdodenhof.parallax.activities.MainActivity;
import de.hdodenhof.parallax.widgets.ParallaxScrollView;

public class ParallaxHelper implements ParallaxScrollView.OnScrollChangedListener, AbsListView.OnScrollListener {

    public static enum Type {
        SCROLLVIEW,
        LISTVIEW
    }

    private MainActivity mActivity;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private View mHeaderPlaceholder;
    private Type mType;
    
    private boolean mHandleResume = false;

    private int mLastDampedScroll = 0;
    private int mCurrentAlpha = 0;
    private int mHeaderTop = 0;

    public ParallaxHelper(MainActivity activity, Type type){
        mActivity = activity;
        mType = type;
    }

    public void onCreateView(final View rootView){
        onCreateView(rootView, null);
    }

    public void onCreateView(final View rootView, View headerPlaceholder){
        mActionBarBackgroundDrawable = mActivity.getActionBarBackgroundDrawable();
        mHeader = rootView.findViewById(R.id.header);
        mHeaderPlaceholder = headerPlaceholder;

        if (mType == Type.LISTVIEW && mHeaderPlaceholder == null){
            throw new IllegalArgumentException("'headerPlaceholder' must not be null");
        }

        if (mHandleResume && mType == Type.SCROLLVIEW){
            rootView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    rootView.getViewTreeObserver().removeOnPreDrawListener(this);
                    mHeader.offsetTopAndBottom(mHeaderTop);
                    mHandleResume = false;
                    return true;
                }
            });
        }
    }

    public void onResume(){
        mActionBarBackgroundDrawable.setAlpha(mCurrentAlpha);
    }

    public void onPause(){
        mHandleResume = true;
    }

    @Override
    public void onScrollChanged(ScrollView view, int l, int t, int oldl, int oldt) {
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
        } else {
            if (mHandleResume) {
                mHeader.offsetTopAndBottom(mHeaderTop);
                mHandleResume = false;
            }

            if (topChild == mHeaderPlaceholder) {
                // Header is visible
                handleScroll(-topChild.getTop());
            } else {
                // Header is invisible
                mHeaderTop = mHeader.getTop();
                mActionBarBackgroundDrawable.setAlpha(255);
            }
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
