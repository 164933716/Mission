package com.versalinks.mission;

import android.Manifest;
import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.versalinks.mission.databinding.ActivitySplashBinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * OK
 */
public class SplashActivity extends BaseActivity<ActivitySplashBinding> {
    private String[] permission = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    protected Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Observable.timer(300, TimeUnit.MILLISECONDS).subscribe(new Observer<Long>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(Long aLong) {

                }

                @Override
                public void onError(Throwable e) {
                    onComplete();
                }

                @Override
                public void onComplete() {
//                    jump2Activity(MainActivity.class);
//                    finish();
                }
            });
        }
    };


    @Override
    protected void onCreate(@NonNull ActivitySplashBinding binding) {
        binding.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChooseDialog chooseDialog=new ChooseDialog(context);
                chooseDialog.show();
            }
        });
        ChooseDialog chooseDialog=new ChooseDialog(context);
        chooseDialog.show();
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
                new PermissionDialog(context, PermissionDialog.Type.File, PermissionDialog.Type.Gps).setClickListener(new PermissionDialog.ClickListener() {
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

    private String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
