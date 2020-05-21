package com.versalinks.mission;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;
import com.versalinks.mission.databinding.ActivityRoutesBinding;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class RoutesActivity extends BaseActivity<ActivityRoutesBinding> {
    private List<Route> allList = new ArrayList<>();
    private BaseAdapter<Route> adapter;
    private Realm realm;
    private RealmResults<Route> allAsync;

    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();
        binding.vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.refresh.setRefreshHeader(new ClassicsHeader(context));
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
        binding.recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
//        binding.ivLogo
        loadData();
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
        allAsync = realm.where(Route.class).findAllAsync();
        allAsync.addChangeListener(new RealmChangeListener<RealmResults<Route>>() {
            @Override
            public void onChange(@NonNull RealmResults<Route> routes) {
                LogUtils.e("onChange" + routes.size());
                binding.refresh.finishRefresh();
                initAdapter(allAsync);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (allAsync != null) {
            allAsync.removeAllChangeListeners(); // remove all registered listeners
        }
        if (realm != null && !realm.isClosed()) {
            realm.close();
        }
        super.onDestroy();
    }

    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_routes);
    }
}
