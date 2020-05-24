package com.versalinks.mission;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MenuChoose extends LinearLayout {
    private ItemClickListener itemClickListener;

    public MenuChoose(@NonNull Context context) {
        this(context, null);
    }

    public MenuChoose(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuChoose(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOrientation(VERTICAL);
        setBackgroundColor(Color.parseColor("#ffffff"));
        TextView textViewTitle = new TextView(context);
        textViewTitle.setPadding(AndroidUtil.dp2Px(15), AndroidUtil.dp2Px(35), AndroidUtil.dp2Px(45), AndroidUtil.dp2Px(20));
        textViewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f);
        textViewTitle.setTypeface(Typeface.DEFAULT_BOLD);
        textViewTitle.setTextColor(Color.parseColor("#333333"));
        textViewTitle.setText("用户名");
        textViewTitle.setBackgroundColor(Color.parseColor("#ebebeb"));
        addView(textViewTitle, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        createIndex1(context, 1,"我的轨迹", R.drawable.ic_gps);
        createIndex1(context, 2,"我的标注", R.drawable.ic_camera_permission);
    }

    private void createIndex1(@NonNull Context context,int index, String name, int resID) {
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER_VERTICAL);
        linearLayout.setPadding(AndroidUtil.dp2Px(15), AndroidUtil.dp2Px(20), AndroidUtil.dp2Px(45), AndroidUtil.dp2Px(20));
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(resID);
        linearLayout.addView(imageView, new LayoutParams(AndroidUtil.dp2Px(20), AndroidUtil.dp2Px(20)));
        Space space = new Space(context);
        linearLayout.addView(space, new LayoutParams(AndroidUtil.dp2Px(5), 0));
        linearLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener != null) {
                    itemClickListener.itemClick(index);
                }
            }
        });

        TextView textView = new TextView(context);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setTextColor(Color.parseColor("#666666"));
        textView.setText(name);
        linearLayout.addView(textView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(linearLayout, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    interface ItemClickListener {
        void itemClick(int index);
    }
}
