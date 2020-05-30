package com.versalinks.mission;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.mapbox.mapboxsdk.Mapbox;
import com.tencent.smtt.sdk.QbSdk;


public class App extends Application {
    public String toolToken = "sk.eyJ1Ijoibm9haHh1IiwiYSI6ImNqcW0xOGRmOTBzbDk0MnMxdmgxNWE0cjIifQ.muFTyK3xueum0V4iaKfEPQ";
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        QbSdk.initX5Environment(context, new QbSdk.PreInitCallback() {
            @Override
            public void onCoreInitFinished() {
                LogUtils.e("onCoreInitFinished");
            }

            @Override
            public void onViewInitFinished(boolean bool) {
                LogUtils.e("onViewInitFinished  " + bool);
            }
        });
        Mapbox.getInstance(context, toolToken);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static Context getContext() {
        return context;
    }
}
