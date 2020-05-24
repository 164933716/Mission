package com.versalinks.mission;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.versalinks.mission.databinding.ActivitySplashBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

/**
 * OK
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA};
    private Handler handler = new Handler(Looper.getMainLooper());
    protected Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Realm.init(App.getContext());
            RealmConfiguration config = new RealmConfiguration.Builder().directory(AndroidUtil.getFolder(context)).deleteRealmIfMigrationNeeded().schemaVersion(1).build();
            Realm.setDefaultConfiguration(config);
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
        test();
    }

    private void test() {
        binding.ivLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseOb<Model_Route> baseOb = new BaseOb<Model_Route>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onDataDeal(Model_Route route, String message) {
                        LogUtils.e(route.toString());
                    }
                };
                Observable<Model_Route> routeObservable = DataUtils.getInstance().saveRoute(new ArrayList<>());
                baseOb.bindObed(routeObservable);
            }
        });
        binding.ivLogo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseOb<List<Model_Route>> baseOb = new BaseOb<List<Model_Route>>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onDataDeal(List<Model_Route> route, String message) {
                        if (route != null) {
                            LogUtils.e(route.toString());
                        } else {
                            LogUtils.e("null");
                        }
                    }
                };
                Observable<List<Model_Route>> routeObservable = DataUtils.getInstance().queryRoute();
                baseOb.bindObed(routeObservable);
            }
        });
        binding.ivLogo2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseOb<List<Model_Route>> baseOb = new BaseOb<List<Model_Route>>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onDataDeal(List<Model_Route> route, String message) {
                        if (route != null) {
                            LogUtils.e(route.size());
                        } else {
                            LogUtils.e("null");
                        }
                    }
                };
                Observable<List<Model_Route>> routeObservable = DataUtils.getInstance().deleteRoute("");
                baseOb.bindObed(routeObservable);
            }
        });
        binding.ivLogo3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseOb<List<Model_Route>> baseOb = new BaseOb<List<Model_Route>>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onDataDeal(List<Model_Route> route, String message) {
                        if (route != null) {
                            LogUtils.e(route.toString());
                        } else {
                            LogUtils.e("null");
                        }
                    }
                };
                Model_Route model_route = new Model_Route();
                model_route.gpsList = new RealmList<>();
                model_route.gpsList.add(new Model_GPS());
                model_route.gpsList.add(new Model_GPS());
                model_route.gpsList.add(new Model_GPS());
                model_route.gpsList.add(new Model_GPS());
                Observable<List<Model_Route>> routeObservable = DataUtils.getInstance().updateRoute("2020-05-21 18:40:01", model_route);
                baseOb.bindObed(routeObservable);
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
                new PermissionDialog(context, PermissionDialog.Type.File, PermissionDialog.Type.Gps,PermissionDialog.Type.Camera).setClickListener(new PermissionDialog.ClickListener() {
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
