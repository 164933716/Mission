package com.versalinks.mission;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ModeView extends LinearLayout {

    private int size;
    private int padding;

    public ModeView(Context context) {
        this(context, null);
    }

    public ModeView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ModeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        size = AndroidUtil.dp2Px(20);
        padding = AndroidUtil.dp2Px(2);
        setGravity(Gravity.CENTER_VERTICAL);
    }

    public void setMode(String... mode) {
        removeAllViews();
        if (mode == null | mode.length == 0) {
            return;
        }
        for (String data : mode) {
            ImageView imageView = new ImageView(getContext());
            if (TextUtils.equals("步行", data)) {
                imageView.setImageResource(R.drawable.ic_walk);
            } else if (TextUtils.equals("缆车", data)) {
                imageView.setImageResource(R.drawable.ic_cable);
            } else if (TextUtils.equals("驾车", data)) {
                imageView.setImageResource(R.drawable.ic_driver);
            } else {
                imageView.setImageResource(android.R.color.transparent);
            }
            imageView.setColorFilter(Color.WHITE);
            imageView.setPadding(padding, padding, padding, padding);
            imageView.setBackgroundColor(Color.parseColor("#489f88"));
            LinearLayout.LayoutParams layoutParams = new LayoutParams(size, size);
            addView(imageView, layoutParams);
        }

    }
}
