package com.versalinks.mission;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

import androidx.annotation.NonNull;

public class ChooseDialog extends BaseDialog {
    public ChooseDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayerChoose layerChoose = new LayerChoose(context);
        setContentView(layerChoose);
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window == null) {
            return;
        }
        Context context = getContext();
        if (context instanceof Activity) {
            if (((Activity) context).isFinishing()) {
                return;
            }
        }

        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.END);
        lp.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        lp.height = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(lp);
    }
}
