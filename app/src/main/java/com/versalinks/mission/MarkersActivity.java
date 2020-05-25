package com.versalinks.mission;

import android.content.Context;
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
import com.versalinks.mission.databinding.ActivityMarkersBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class MarkersActivity extends BaseActivity<ActivityMarkersBinding> {
    private List<Model_Marker> allList = new ArrayList<>();
    private BaseAdapter<Model_Marker> adapter;

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

    private void initAdapter(List<Model_Marker> list) {
        allList.clear();
        if (list != null) {
            allList.addAll(list);
        }
        if (adapter == null) {
            adapter = new BaseAdapter<Model_Marker>(R.layout.item_marker, allList) {
                @Override
                protected void convert(View helper, int position, int viewType) {
                    Model_Marker item = allList.get(position);
                    TextView tvTitle = helper.findViewById(R.id.tvTitle);
                    TextView tvDescription = helper.findViewById(R.id.tvDescription);
                    TextView tvDate = helper.findViewById(R.id.tvDate);
                    ImageView ivLogo = helper.findViewById(R.id.ivLogo);
                    ImageView ivDelete = helper.findViewById(R.id.ivDelete);
                    TextView tvDistance = helper.findViewById(R.id.tvDistance);
                    tvDistance.setText(DataUtils.convertToDistance(item.gps.height));
                    tvTitle.setText(item.name);
                    tvDescription.setText(item.type.name);
                    tvDate.setText(DataUtils.convertToDate(item.createTime));
                    ivDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            allList.remove(position);
                            adapter.notifyItemRemoved(position);
                            adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                        }
                    });
                }
            };
            binding.recycler.setAdapter(adapter);
        }
        adapter.notifyDataSetChanged();
    }

    private void loadData() {
        Observable<List<Model_Marker>> listObservable = DataUtils.getInstance().queryMarker();
        BaseOb<List<Model_Marker>> baseOb = new BaseOb<List<Model_Marker>>() {
            @Override
            public void onDataDeal(List<Model_Marker> data, String message) {
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
        return createViewByID(R.layout.activity_markers);
    }
}
