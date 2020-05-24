package com.versalinks.mission;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.blankj.utilcode.util.LogUtils;
import com.tencent.smtt.sdk.QbSdk;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class App extends Application {

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
                LogUtils.e("onViewInitFinished  "+bool);
            }
        });
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
