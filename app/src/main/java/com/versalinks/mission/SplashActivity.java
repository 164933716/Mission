package com.versalinks.mission;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.versalinks.mission.databinding.ActivitySplashBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
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
            String jsonPoi = DataUtils.getJson(context, "poi.geojson");
            FeatureCollection featureCollectionPoi = FeatureCollection.fromJson(jsonPoi);
            List<Feature> pois = featureCollectionPoi.features();
            Observable<List<Feature>> poiOb = DataUtils.getInstance().saveMarker(pois);
            BaseOb<List<Feature>> poiObBase = new BaseOb<List<Feature>>() {
                @Override
                public void onDataDeal(List<Feature> data, String message) {
                    LogUtils.e(data.size());
                }
            };
            poiObBase.bindObed(poiOb);
            String jsonRoute = DataUtils.getJson(context, "route.geojson");
            FeatureCollection featureCollectionRoute = FeatureCollection.fromJson(jsonRoute);
            List<Feature> routes = featureCollectionRoute.features();

            Observable<List<Feature>> routeOb = DataUtils.getInstance().saveRoute(routes);
            BaseOb<List<Feature>> routeObBase = new BaseOb<List<Feature>>() {
                @Override
                public void onDataDeal(List<Feature> data, String message) {
                    LogUtils.e(data.size());
                }
            };
            routeObBase.bindObed(routeOb);

            Observable<List<Feature>> recordOb = DataUtils.getInstance().saveRecord(routes);
            BaseOb<List<Feature>> recordBaseOb = new BaseOb<List<Feature>>() {
                @Override
                public void onDataDeal(List<Feature> data, String message) {
                    LogUtils.e(data.size());
                }
            };
            recordBaseOb.bindObed(recordOb);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    jump2Activity(MainActivity.class);
                    finish();
                }
            }, 300);
        }
    };


    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
        if (permissionsOK(permission)) {
            runnable.run();
        } else {
            requestPermissions(permission, 9999, runnable);
        }
//        test();
    }

    private void test() {
        binding.ivJson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(MainActivity.class);
            }
        });
        binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jsonRoute = DataUtils.getJson(context, "张家坝.json");
                List<Model_GPS> o = new Gson().fromJson(jsonRoute, new TypeToken<List<Model_GPS>>() {
                }.getType());
                List<double[]> arrays = new ArrayList<>();
                for (Model_GPS model_gps : o) {
                    arrays.add(new double[]{model_gps.longitude, model_gps.latitude, model_gps.height});
                }
                String s = new Gson().toJson(arrays);
                LogUtils.e("s   " + s);
            }
        });
        binding.ivQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        binding.ivUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
