package com.versalinks.mission;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class TipDialog extends BaseDialog {

    public TipDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip);

    }
}
