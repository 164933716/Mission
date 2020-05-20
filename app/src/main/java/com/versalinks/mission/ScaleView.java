package com.versalinks.mission;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class ScaleView extends LinearLayout {

    private View vBig;
    private View vSmall;
    private ScaleClickListener scaleClickListener;
    private boolean bigEnable=true;
    private boolean smallEnable=true;

    public ScaleView(Context context) {
        this(context, null);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_view_scale, this, true);
        vBig = findViewById(R.id.vBig);
        vSmall = findViewById(R.id.vSmall);
        vBig.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scaleClickListener != null) {
                    scaleClickListener.bigClick();
                }
            }
        });
        vSmall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (scaleClickListener != null) {
                    scaleClickListener.smallClick();
                }
            }
        });
        setBigEnable(bigEnable);
        setSmallEnable(smallEnable);
    }

    public void setBigEnable(boolean enable) {
        this.bigEnable = enable;
        vBig.setEnabled(enable);
    }

    public void setSmallEnable(boolean enable) {
        this.smallEnable = enable;
        vSmall.setEnabled(enable);
    }

    public void setScaleClickListener(ScaleClickListener scaleClickListener) {
        this.scaleClickListener = scaleClickListener;
    }

   public interface ScaleClickListener {
        void bigClick();

        void smallClick();

    }
}
