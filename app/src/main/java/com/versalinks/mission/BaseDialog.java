package com.versalinks.mission;

import android.app.Dialog;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;


/**
 * Created by Ksy.
 */

public class BaseDialog extends Dialog {

    protected final Context context;

    public BaseDialog(@NonNull Context context) {
        this(context, R.style.DialogStyle);
    }

    public BaseDialog(@NonNull Context context, int styleId) {
        super(context, styleId);
        this.context=context;
    }

    protected void showSoftInput() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }
}
