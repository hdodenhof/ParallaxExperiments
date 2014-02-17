package de.hdodenhof.parallax.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
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

    private ActionBar mActionBar;
    private Drawable mActionBarBackgroundDrawable;
    private View mHeader;
    private View mHeaderPlaceholder;
    private Type mType;

    private boolean mHandleResume = false;

    private int mLastDampedScroll = 0;
    private int mCurrentAlpha = 0;
    private int mHeaderTop = 0;

    public ParallaxHelper(ActionBarActivity activity, Type type){
        mActionBar = activity.getSupportActionBar();
        mType = type;

        mActionBarBackgroundDrawable = activity.getResources().getDrawable(R.drawable.ab_solid);
    }

    public void onCreateView(final View rootView){
        onCreateView(rootView, null);
    }

    public void onCreateView(final View rootView, View headerPlaceholder){
        mHeader = rootView.findViewById(R.id.header);
        mHeaderPlaceholder = headerPlaceholder;

        if (mType == Type.LISTVIEW && mHeaderPlaceholder == null){
            throw new IllegalArgumentException("'headerPlaceholder' must not be null");
        }

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mActionBarBackgroundDrawable.setCallback(mDrawableCallback);
        }

        mActionBar.setBackgroundDrawable(mActionBarBackgroundDrawable);

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
        final int headerHeight = mHeader.getHeight() - mActionBar.getHeight();
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

    private Drawable.Callback mDrawableCallback = new Drawable.Callback() {

        @Override
        public void invalidateDrawable(Drawable who) {
            mActionBar.setBackgroundDrawable(who);
        }

        @Override
        public void scheduleDrawable(Drawable who, Runnable what, long when) {
        }

        @Override
        public void unscheduleDrawable(Drawable who, Runnable what) {
        }
    };

}
