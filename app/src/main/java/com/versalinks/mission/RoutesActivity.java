package com.versalinks.mission;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;
import com.versalinks.mission.databinding.ActivityRoutesBinding;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class RoutesActivity extends BaseActivity<ActivityRoutesBinding> {
    private List<Model_Route> allList = new ArrayList<>();
    private BaseAdapter<Model_Route> adapter;

    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
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
        binding.recycler.addItemDecoration(new HLineDividerAll(Color.TRANSPARENT, 10f));
        binding.recycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        binding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        Serializable thumbFileSer = intent.getSerializableExtra("thumbFile");
        if (thumbFileSer instanceof File) {
            if (((File) thumbFileSer).exists() && ((File) thumbFileSer).length() > 0) {
                Glide.with(context).load(thumbFileSer).centerCrop().into(binding.ivLogo);
                binding.ivLogo.setVisibility(View.VISIBLE);
            } else {
                binding.ivLogo.setVisibility(View.GONE);
            }
        } else {
            binding.ivLogo.setVisibility(View.GONE);
        }
        loadData();
    }

    private void initAdapter(List<Model_Route> list) {
        allList.clear();
        if (list != null) {
            allList.addAll(list);
        }
        if (adapter == null) {
            adapter = new BaseAdapter<Model_Route>(R.layout.item_route, allList) {
                @Override
                protected void convert(View helper, int position, int viewType) {
                    Model_Route item = allList.get(position);
                    ImageView ivLogo = helper.findViewById(R.id.ivLogo);
                    ModeView ivMode = helper.findViewById(R.id.ivMode);
                    TextView tvDuration = helper.findViewById(R.id.tvDuration);
                    TextView tvDifficult = helper.findViewById(R.id.tvDifficult);
                    TextView tvDistance = helper.findViewById(R.id.tvDistance);
                    TextView tvDistanceUp = helper.findViewById(R.id.tvDistanceUp);
                    TextView tvTitle = helper.findViewById(R.id.tvTitle);
                    TextView tvDescription = helper.findViewById(R.id.tvDescription);
                    Pair<String, String> durationWithUnit = DataUtils.convertToDurationWithUnit(item.goDuration);
                    tvDuration.setText("大约需要" + durationWithUnit.first + durationWithUnit.second);
                    tvDifficult.setText(item.goDifficulty);
                    tvDistance.setText(DataUtils.convertToDistance(item.distance));
                    tvDistanceUp.setText(DataUtils.convertToDistance(item.goUp));
                    tvTitle.setText(item.name);
                    tvDescription.setText(item.description);
                    String[] strings = item.goMode.toArray(new String[0]);
                    ivMode.setMode(strings);
                }
            };
            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, int viewType) {
                    Model_Route model_route = allList.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("model_route", model_route);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
            binding.recycler.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        Observable<List<Model_Route>> listObservable = DataUtils.getInstance().queryRoute();
        BaseOb<List<Model_Route>> baseOb = new BaseOb<List<Model_Route>>() {
            @Override
            public void onDataDeal(List<Model_Route> data, String message) {
                LogUtils.e("onChange    " + data.size());
                binding.refresh.finishRefresh();
                initAdapter(data);
            }
        };
        baseOb.bindObed(listObservable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_routes);
    }
}
