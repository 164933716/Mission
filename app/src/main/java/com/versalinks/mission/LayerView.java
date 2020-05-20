package com.versalinks.mission;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LayerView extends FrameLayout {
    private boolean isCheck;
    private RadioButton radio;
    private Layer.Item item;
    private ImageView ivLogo;
    private TextView tvLabel;

    public LayerView(@NonNull Context context) {
        this(context, null);
    }

    public LayerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public LayerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_layer, this, true);
        ivLogo = findViewById(R.id.ivLogo);
        tvLabel = findViewById(R.id.tvLabel);
        radio = findViewById(R.id.radio);
        check(isCheck);
        bind(item);
    }

    public void check(boolean isCheck) {
        this.isCheck = isCheck;
        if (radio != null) {
            radio.setChecked(isCheck);
        }
    }

    public void bind(Layer.Item item) {
        this.item = item;
        if (item != null) {
            if (ivLogo != null) {
                ivLogo.setImageResource(item.resID);
            }
            if (tvLabel != null) {
                tvLabel.setText(item.label);
            }
        }
    }
}
