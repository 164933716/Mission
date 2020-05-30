package com.versalinks.mission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.mapbox.geojson.Feature;
import com.versalinks.mission.databinding.ActivityPoiDetailBinding;

public class PoiDetailActivity extends BaseActivity<ActivityPoiDetailBinding> {
    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String geoJson = intent.getStringExtra("geoJson");
        Feature feature = Feature.fromJson(geoJson);
        String 名称 = feature.getStringProperty("名称");
        String 英文名称 = feature.getStringProperty("英文名称");
        String 别名 = feature.getStringProperty("别名");
        String 简介1 = feature.getStringProperty("简介1");
        String 简介2 = feature.getStringProperty("简介2");
        String 保护级别 = feature.getStringProperty("保护级别");
        String 图片 = feature.getStringProperty("图片");
        LogUtils.e("feature     "+feature);
        binding.tvName.setText(名称);
        binding.tvEnName.setText(名称);
    }

    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_poi_detail);
    }
}
