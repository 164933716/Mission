package com.versalinks.mission;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LayerView extends FrameLayout {
    private boolean isCheck;
    private RadioButton radio;

    public LayerView(@NonNull Context context) {
        this(context, null);
    }

    public LayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public LayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_layer, this, true);
        radio = findViewById(R.id.radio);
        check(isCheck);
    }

    public void check(boolean isCheck) {
        this.isCheck = isCheck;
        if (radio != null) {
            radio.setChecked(isCheck);
        }
    }
}
