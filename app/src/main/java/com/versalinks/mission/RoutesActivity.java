package com.versalinks.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;
import com.versalinks.mission.databinding.ActivityRoutesBinding;

import java.util.ArrayList;
import java.util.List;

public class RoutesActivity extends BaseActivity<ActivityRoutesBinding> {
    private List<Route> allList = new ArrayList<>();
    private BaseAdapter<Route> adapter;

    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
        binding.vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.refresh.setEnableRefresh(true);
        binding.refresh.setEnableLoadMore(false);
        binding.refresh.setEnableAutoLoadMore(false);
        binding.refresh.setOnMultiListener(new SimpleMultiListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                super.onRefresh(refreshLayout);
                loadData();
            }
        });
//        binding.ivLogo
    }

    private void initAdapter(List<Route> list) {
        allList.clear();
        if (list != null) {
            allList.addAll(list);
        }
        if (adapter == null) {
            adapter = new BaseAdapter<Route>(R.layout.item_route, allList) {
                @Override
                protected void convert(View helper, int position, int viewType) {

                }
            };
            binding.recycler.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        binding.refresh.finishRefresh();
        initAdapter(null);
    }

    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_routes);
    }
}
