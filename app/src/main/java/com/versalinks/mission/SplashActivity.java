package com.versalinks.mission;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.versalinks.mission.databinding.ActivitySplashBinding;

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
