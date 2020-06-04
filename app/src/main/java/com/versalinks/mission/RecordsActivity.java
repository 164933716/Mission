package com.versalinks.mission;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.simple.SimpleMultiListener;
import com.versalinks.mission.databinding.ActivityRecordsBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class RecordsActivity extends BaseActivity<ActivityRecordsBinding> {
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

        loadData();
    }

    private void initAdapter(List<Model_Route> list) {
        allList.clear();
        if (list != null) {
            allList.addAll(list);
        }
        if (adapter == null) {
            adapter = new BaseAdapter<Model_Route>(R.layout.item_record, allList) {
                @Override
                protected void convert(View helper, int position, int viewType) {
                    Model_Route item = allList.get(position);
                    ImageView ivLogo = helper.findViewById(R.id.ivLogo);
                    TextView tvDistance = helper.findViewById(R.id.tvDistance);
                    TextView tvDistanceUp = helper.findViewById(R.id.tvDistanceUp);
                    TextView tvTitle = helper.findViewById(R.id.tvTitle);
                    TextView tvDescription = helper.findViewById(R.id.tvDescription);
                    TextView tvDate = helper.findViewById(R.id.tvDate);
                    TextView tvShare = helper.findViewById(R.id.tvShare);
                    tvDistance.setText(DataUtils.convertToDistance(item.distance));
                    tvDistanceUp.setText(DataUtils.convertToDistance(item.goUp));
                    tvTitle.setText(item.name);
                    tvDescription.setText(item.description);
                    tvDate.setText(DataUtils.convertToDate(item.createTime));
                }
            };
            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position, int viewType) {
                    Model_Route model_record = allList.get(position);
                    Intent intent = new Intent();
                    intent.putExtra("model_record", model_record);
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
        return createViewByID(R.layout.activity_records);
    }
}
