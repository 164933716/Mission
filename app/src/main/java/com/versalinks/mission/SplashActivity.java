package com.versalinks.mission;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;

import com.versalinks.mission.databinding.ActivitySplashBinding;

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
            DataUtils.getInstance().createRoute(context);
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
