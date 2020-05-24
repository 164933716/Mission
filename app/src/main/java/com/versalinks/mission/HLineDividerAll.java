package com.versalinks.mission;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by ksy on 2018/3/1.
 */

public class HLineDividerAll extends RecyclerView.ItemDecoration {

    private int lineLeftMargin;
    private int lineRightMargin;
    private int bottomLineLeftMargin;
    private int bottomLineRightMargin;
    private int lineColor;
    private int lineSize;
    private int bottomLineSize;
    private Paint mPaint;
    private int bgColor;

    public HLineDividerAll(int lineColor, float lineSize) {
        this(lineColor, lineSize, 0, 0, 0.8f, 0, 0);
    }

    public HLineDividerAll(int lineColor, float lineSize, int lineLeftMargin, int lineRightMargin, float bottomLineSize, int bottomLineLeftMargin, int bottomLineRightMargin) {
        super();
        this.lineSize = dip2px(lineSize);
        this.bottomLineSize = dip2px(bottomLineSize);
        this.lineLeftMargin = dip2px(lineLeftMargin);
        this.lineRightMargin = dip2px(lineRightMargin);
        this.bottomLineLeftMargin = dip2px(bottomLineLeftMargin);
        this.bottomLineRightMargin = dip2px(bottomLineRightMargin);
        this.lineColor = lineColor;
        init();
    }

    public int dip2px(float dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * ((float) displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return (int) px;
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(this.lineColor);
        //背景颜色
        bgColor = Color.parseColor("#f2f2f2");
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            int adapterPosition = parent.getChildAdapterPosition(childAt);
            mPaint.setColor(this.lineColor);
            if (adapterPosition == 0) {
                c.drawRect(parent.getPaddingLeft() + lineLeftMargin, 0, parent.getWidth() - parent.getPaddingRight() - lineRightMargin, childAt.getTop(), mPaint);
            }
            c.drawRect(parent.getPaddingLeft() + lineLeftMargin, childAt.getBottom(), parent.getWidth() - parent.getPaddingRight() - lineRightMargin, childAt.getBottom() + lineSize, mPaint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int childAdapterPosition = parent.getChildAdapterPosition(view);
        if (childAdapterPosition == 0) {
            outRect.top = lineSize;
        }
        outRect.bottom = lineSize;

    }
}
