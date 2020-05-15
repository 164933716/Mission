package com.versalinks.mission;

import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

public class PermissionDialog extends BaseDialog {

    private final Type[] type;
    private ClickListener clickListener;

    public PermissionDialog setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
        return PermissionDialog.this;
    }

    interface ClickListener {
        void exit(PermissionDialog dialog);

        void goSetting(PermissionDialog dialog);
    }

    enum Type {
        File(R.drawable.ic_sdcard, "文件存储"),
        Gps(R.drawable.ic_gps, "地理位置"),
        ;

        public int resId;
        public String msg;

        Type(int resId, String msg) {
            this.resId = resId;
            this.msg = msg;
        }
    }

    public PermissionDialog(@NonNull Context context, Type... type) {
        super(context, R.style.DialogStyle);
        setCancelable(false);
        setCanceledOnTouchOutside(false);
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_permission);
        TextView tvTitle = findViewById(R.id.tvTitle);
        Spanned spanned = HtmlCompat.fromHtml("使用" + "<font color='#03DAC5'" + ">" + BuildConfig.modeName + "</font>" + "需要开启以下权限", HtmlCompat.FROM_HTML_OPTION_USE_CSS_COLORS);
        tvTitle.setText(spanned);
        LinearLayout container = findViewById(R.id.dialogContainer);
        View dialogLeft = findViewById(R.id.dialogLeft);
        View dialogRight = findViewById(R.id.dialogRight);
        for (Type type : type) {
            View inflate = LayoutInflater.from(context).inflate(R.layout.item_permission, container, false);
            ImageView image = inflate.findViewById(R.id.image);
            TextView text = inflate.findViewById(R.id.text);
            image.setImageResource(type.resId);
            text.setText(type.msg);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = AndroidUtil.dp2Px(10);
            container.addView(inflate, layoutParams);
        }
        dialogLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.exit(PermissionDialog.this);
                }
            }
        });
        dialogRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.goSetting(PermissionDialog.this);
                }
            }
        });
    }
}
