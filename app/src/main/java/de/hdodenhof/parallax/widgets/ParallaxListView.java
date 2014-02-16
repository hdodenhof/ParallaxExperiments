package de.hdodenhof.parallax.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.AbsListView;
import android.widget.ListView;

public class ParallaxListView extends ListView implements AbsListView.OnScrollListener {

    public interface OnIdleListener {

        public void onIdle();
    }

    private OnScrollListener mWrappedOnScrollListener = null;
    private OnIdleListener mOnIdleListener = null;

    private boolean mIsOverScrollEnabled = false;
    private int mScrollState = SCROLL_STATE_IDLE;

    public ParallaxListView(Context context) {
        super(context);
    }

    public ParallaxListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOverScrollEnabled(boolean enabled) {
        mIsOverScrollEnabled = enabled;
    }

    public boolean isOverScrollEnabled() {
        return mIsOverScrollEnabled;
    }

    public void setOnIdleListener(OnIdleListener onIdleListener) {
        mOnIdleListener = onIdleListener;
    }

    public boolean isIdle() {
        return mScrollState == SCROLL_STATE_IDLE;
    }

    @Override
    protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY,
            boolean isTouchEvent) {
        return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, mIsOverScrollEnabled ? maxOverScrollX : 0,
                mIsOverScrollEnabled ? maxOverScrollY : 0, isTouchEvent);
    }

    @Override
    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mWrappedOnScrollListener = onScrollListener;
        super.setOnScrollListener(this); // we know that this.setOnScrollListener is always called
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mWrappedOnScrollListener != null) {
            mWrappedOnScrollListener.onScrollStateChanged(view, scrollState);
        }

        if (scrollState == SCROLL_STATE_IDLE && mScrollState != SCROLL_STATE_IDLE) {
            if (mOnIdleListener != null) {
                // Handle fling direction change
                Handler handler = getHandler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Still idle
                        if (mScrollState == SCROLL_STATE_IDLE && mOnIdleListener != null){
                            mOnIdleListener.onIdle();
                        }
                    }
                }, 50);
            }
        }

        mScrollState = scrollState;
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (mWrappedOnScrollListener != null) {
            mWrappedOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }
}
