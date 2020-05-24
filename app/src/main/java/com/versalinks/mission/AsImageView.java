package com.versalinks.mission;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

public class AsImageView extends AppCompatImageView {
    private float widthScale;
    private float heightScale;

    public AsImageView(Context context) {
        super(context);
    }

    public AsImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AsImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 控件的测量方法，该方法实现控件的测量规则，
     * 并在该方法最后调用setMeasuredDimension(widthSize,heightSize)
     * 或者super.onMeasure(widthMeasureSpec, heightMeasureSpec)
     * 保存最终测量的控件大小
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //默认测量规则
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //限制开发者只能设置了一个比例（比例得有一个实际大小的参照，比如宽200，heightScale=1f,那么高度的值=200）
        if (widthScale != 0f && heightScale != 0f) {
            throw new IllegalArgumentException("宽高比例只能设置一个");
        }
        //得到默认测量规则下测量到的宽度
        int measuredWidth = getMeasuredWidth();
        //得到默认测量规则下测量到的高度
        int measuredHeight = getMeasuredHeight();
        //判断开发者设置的宽高比
        if (widthScale != 0) {
            //开发者设置的是宽度比例，通过比例和实际高度计算实际的宽度
            measuredWidth = (int) (widthScale * measuredHeight);
            //保存测量的最终结果setMeasuredDimension()或者super.onMeasure()
            setMeasuredDimension(measuredWidth, measuredHeight);
//            super.onMeasure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
        } else if (heightScale != 0) {
            //开发者设置的是高度比例，通过比例和实际宽度计算实际的高度
            measuredHeight = (int) (heightScale * measuredWidth);
            //保存测量的最终结果setMeasuredDimension()或者super.onMeasure()
            setMeasuredDimension(measuredWidth, measuredHeight);
//            super.onMeasure(MeasureSpec.makeMeasureSpec(measuredWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY));
        }
    }

    public void setWidthScale(float widthScale) {
        this.widthScale = widthScale;
        requestLayout();
    }

    public void setHeightScale(float heightScale) {
        this.heightScale = heightScale;
        requestLayout();
    }
}
