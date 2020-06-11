package com.versalinks.mission;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.blankj.utilcode.util.LogUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.versalinks.mission.databinding.ActivitySplashBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * OK
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private Handler handler = new Handler(Looper.getMainLooper());
    protected Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Realm.init(App.getContext());
            RealmConfiguration config = new RealmConfiguration.Builder().directory(AndroidUtil.getFolder(context)).deleteRealmIfMigrationNeeded().schemaVersion(1).build();
            Realm.setDefaultConfiguration(config);

            String jsonRoute = DataUtils.getJson(context, "route.geojson");
            FeatureCollection featureCollectionRoute = FeatureCollection.fromJson(jsonRoute);
            List<Feature> routes = featureCollectionRoute.features();
            Observable<List<Feature>> routeOb = DataUtils.getInstance().saveRoute(routes);

            String jsonMarker = DataUtils.getJson(context, "poi.geojson");
            FeatureCollection featureCollectionMarker = FeatureCollection.fromJson(jsonMarker);
            List<Feature> markers = featureCollectionMarker.features();
            Observable<List<Feature>> markerOb = DataUtils.getInstance().saveMarker(markers);
            Observable<Boolean> zip = Observable.zip(routeOb, markerOb, new BiFunction<List<Feature>, List<Feature>, Boolean>() {
                @Override
                public Boolean apply(List<Feature> features, List<Feature> features2) throws Exception {
                    return true;
                }
            });

            BaseOb<Boolean> baseOb = new BaseOb<Boolean>() {
                @Override
                public void onDataDeal(Boolean data, String message) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            jump2Activity(MainActivity.class);
                            finish();
                        }
                    }, 50);
                }
            };
            baseOb.bindObed(zip);

        }
    };


    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
        if (permissionsOK(permission)) {
            runnable.run();
        } else {
            requestPermissions(permission, 9999, runnable);
        }
//        binding.ivJson.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                步行();
////                缆车();
//            }
//        });
    }

    private void 缆车() {
        String jsonByAssets = DataUtils.getJsonByAssets(context, "map10.geojson");
//                LogUtils.e("jsonByAssets    " + jsonByAssets);
        FeatureCollection featureCollectionRoute = FeatureCollection.fromJson(jsonByAssets);
        List<Feature> routes = featureCollectionRoute.features();
//                Point.fromLngLat()
//                LineString lineString=Feature.fromGeometry()
//                LineString.fromLngLats()
        List<Point> points = new ArrayList<>();
        List<String> models = new ArrayList<>();//步行、汽车、缆车

        if (routes != null) {
            for (int i = 0; i < routes.size(); i++) {

                Feature feature = routes.get(i);
                String mode = feature.getStringProperty("mode");
                int stroke = feature.getNumberProperty("stroke-width").intValue();
                if (i > 0) {
                    Feature featureLast = routes.get(i - 1);
                    String modeLast = featureLast.getStringProperty("mode");
                    if (TextUtils.equals(mode, modeLast)) {
                        models.add(modeLast);
                    } else {
                        models.add("切换");
                    }
                }
                Geometry geometry = feature.geometry();
                if (geometry instanceof LineString) {
                    List<String> model = new ArrayList<>();//步行、汽车、缆车
                    LineString lineString = (LineString) geometry;
                    List<Point> coordinates = lineString.coordinates();
                    for (int i1 = 0; i1 < coordinates.size(); i1++) {
                        if (i1 > 0) {
                            model.add(mode);
                        }
                    }
                    if (stroke == 2) {
                        Collections.reverse(coordinates);
                    }
                    LogUtils.e("coordinates " + coordinates.size());
                    LogUtils.e("model " + model.size());
                    points.addAll(coordinates);
                    models.addAll(model);
                }
            }

        }
        List<Float> distances = new ArrayList<>();
        float distance = 0;
        for (int i = 0; i < points.size(); i++) {
            Point modelGps = points.get(i);
            if (i == 0) {
                distances.add(distance);
            } else {
                Point modelGpsLast = points.get(i - 1);
                if (modelGpsLast == null) {
                    continue;
                }
                float vvv = AMapUtils.calculateLineDistance(new LatLng(modelGpsLast.latitude(), modelGpsLast.longitude()), new LatLng(modelGps.latitude(), modelGps.longitude()));
                distance += vvv;
                distances.add(distance);
            }
        }

        LineString lineString = LineString.fromLngLats(points);
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonElements = new JsonArray();
        JsonArray jsonElements1 = new JsonArray();
        for (Float model : distances) {
            jsonElements1.add(model);
        }
        for (String model : models) {
            jsonElements.add(model);
        }
        jsonObject.add("transportation", jsonElements);
        jsonObject.add("distanceItem", jsonElements1);
        Feature feature = Feature.fromGeometry(lineString, jsonObject);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);
        String s = featureCollection.toJson();
        AndroidUtil.saveText(AndroidUtil.getTempTxtFile(context, "线路1包含缆车en.geojson"), s);
        LogUtils.e("coordinates ok" + points.size());
        LogUtils.e("model ok" + models.size());
        LogUtils.e("distances ok" + distances.size());
    }

    private void 步行() {
        String jsonByAssets = DataUtils.getJsonByAssets(context, "route_east.geojson");
//                LogUtils.e("jsonByAssets    " + jsonByAssets);
        FeatureCollection featureCollectionRoute = FeatureCollection.fromJson(jsonByAssets);
        List<Feature> routes = featureCollectionRoute.features();
//                Point.fromLngLat()
//                LineString lineString=Feature.fromGeometry()
//                LineString.fromLngLats()
        List<Point> points = new ArrayList<>();
        List<String> models = new ArrayList<>();//步行、汽车、缆车

        if (routes != null) {
            List<String> model = new ArrayList<>();//步行、汽车、缆车
            for (int i = 0; i < routes.size(); i++) {
                Feature feature = routes.get(i);
                Geometry geometry = feature.geometry();
                if (geometry instanceof LineString) {
                    LineString lineString = (LineString) geometry;
                    List<Point> coordinates = lineString.coordinates();
                    for (int i1 = 0; i1 < coordinates.size(); i1++) {
                        if (i1 > 0) {
                            models.add("驾车");
                        }
                    }
                    points.addAll(coordinates);
                }
                LogUtils.e("model " + model.size());
                LogUtils.e("points " + points.size());
            }

        }


        LineString lineString = LineString.fromLngLats(points);
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonElements = new JsonArray();

        for (String model : models) {
            jsonElements.add(model);
        }
        jsonObject.add("transportation", jsonElements);
        Feature feature = Feature.fromGeometry(lineString, jsonObject);
        FeatureCollection featureCollection = FeatureCollection.fromFeature(feature);
        String s = featureCollection.toJson();
        AndroidUtil.saveText(AndroidUtil.getTempTxtFile(context, "测试.geojson"), s);
        LogUtils.e("coordinates ok" + points.size());
        LogUtils.e("model ok" + models.size());
    }


    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_splash);
    }

    @Override
    protected void resultPermissions(boolean isPermissionsOk, String[] permissions, final int requestCode) {
        super.resultPermissions(isPermissionsOk, permissions, requestCode);
        if (requestCode == 9999) {
            if (isPermissionsOk) {
                runnable.run();
            } else {
                new PermissionDialog(context, PermissionDialog.Type.File, PermissionDialog.Type.Gps, PermissionDialog.Type.Camera).setClickListener(new PermissionDialog.ClickListener() {
                    @Override
                    public void exit(PermissionDialog dialog) {
                        dialog.dismiss();
                        finish();
                    }

                    @Override
                    public void goSetting(PermissionDialog dialog) {
                        dialog.dismiss();
                        AndroidUtil.toAppSetting(SplashActivity.this, requestCode, BuildConfig.APPLICATION_ID);
                        finish();
                    }
                }).show();
            }
        }
    }

}
