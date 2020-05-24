package com.versalinks.mission;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class TipDialog extends BaseDialog {

    private final String title;
    private final String description;

    public TipDialog(@NonNull Context context, String title, String description) {
        super(context, R.style.DialogStyle);
        this.title = title;
        this.description = description;
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip);
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvDescription = findViewById(R.id.tvDescription);
        tvTitle.setText(title);
        tvDescription.setText(description);
    }
}
