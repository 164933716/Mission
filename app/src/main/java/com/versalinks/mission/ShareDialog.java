package com.versalinks.mission;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShareDialog extends BaseDialog {
    private List<SDK> allList = new ArrayList<>();

    public class SDK {
        private final String label;
        private final int res;

        public SDK(String label, int res) {
            this.label = label;
            this.res = res;
        }
    }

    public ShareDialog(@NonNull Context context) {
        super(context, R.style.DialogStyle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView recycler = findViewById(R.id.recycler);
        recycler.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));
        allList.add(new SDK("微信", R.drawable.ic_wechat));
        allList.add(new SDK("朋友圈", R.drawable.ic_wechat_circel));
        allList.add(new SDK("QQ", R.drawable.ic_qq));
        allList.add(new SDK("支付宝", R.drawable.ic_ali));
        BaseAdapter<SDK> adapter = new BaseAdapter<SDK>(R.layout.item_share, allList) {
            @Override
            protected void convert(View helper, int position, int viewType) {
                SDK item = allList.get(position);
                ImageView ivLogo = helper.findViewById(R.id.ivLogo);
                TextView tvLabel = helper.findViewById(R.id.tvLabel);
                ivLogo.setImageResource(item.res);
                tvLabel.setText(item.label);
            }
        };
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, int viewType) {
                dismiss();
            }
        });
        recycler.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        window.setGravity(Gravity.BOTTOM);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
        window.setAttributes(lp);
    }
}
