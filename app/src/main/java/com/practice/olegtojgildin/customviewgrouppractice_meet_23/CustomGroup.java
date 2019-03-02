package com.practice.olegtojgildin.customviewgrouppractice_meet_23;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

/**
 * Created by olegtojgildin on 01/03/2019.
 */

public class CustomGroup extends ViewGroup {
    public CustomGroup(Context context) {
        super(context);
        init(context);
    }

    public CustomGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public CustomGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);

    }

    private void init(Context context) {
        final Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        Point deviceDisplay = new Point();
        display.getSize(deviceDisplay);
        deviceHeight = deviceDisplay.y;
    }

    private int deviceHeight;
    private int mLeftWidth;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();

        mLeftWidth = 0;

        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;

        int countColumn = 0;
        int mTopHeight = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {

                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                maxHeight = Math.max(maxHeight,
                        child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
                mTopHeight += child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                if (mTopHeight / deviceHeight > countColumn) {
                    countColumn++;
                    maxWidth += child.getMeasuredWidth();
                } else {
                    maxWidth += Math.max(maxWidth,
                            child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin);
                }
                childState = combineMeasuredStates(childState, child.getMeasuredState());
            }
        }

        maxWidth += mLeftWidth;

        maxHeight = Math.max(maxHeight, getSuggestedMinimumHeight());
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, heightMeasureSpec,
                        childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxWidth;

        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxWidth = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                return;

            child.measure(MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.AT_MOST), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();

            if (curTop + curHeight >= childBottom) {
                curTop = childTop;
                curLeft += maxWidth;
                maxWidth = 0;
            }
            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
            if (maxWidth < curWidth)
                maxWidth = curWidth;
            curTop += curHeight;
        }
    }


    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new CustomGroup.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public static class LayoutParams extends MarginLayoutParams {
        /**
         * The gravity to apply with the View to which these layout parameters
         * are associated.
         */
        public int gravity = Gravity.TOP | Gravity.START;

        public static final int POSITION_LEFT = 1;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
