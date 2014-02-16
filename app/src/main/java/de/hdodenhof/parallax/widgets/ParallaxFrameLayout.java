package de.hdodenhof.parallax.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import de.hdodenhof.parallax.R;

public class ParallaxFrameLayout extends FrameLayout {

    private View mHeader;
    private boolean mInitialized = false;

    public ParallaxFrameLayout(Context context) {
        super(context);
    }

    public ParallaxFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ParallaxFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mHeader == null) {
            mHeader = findViewById(R.id.header);
        }

        if (!mInitialized) {
            super.onLayout(changed, left, top, right, bottom);
            mInitialized = true;
            return;
        }

        int headerTopPrevious = mHeader.getTop();

        super.onLayout(changed, left, top, right, bottom);

        int headerTopCurrent = mHeader.getTop();
        if (headerTopCurrent != headerTopPrevious) {
            mHeader.offsetTopAndBottom(headerTopPrevious - headerTopCurrent);
        }
    }
}
