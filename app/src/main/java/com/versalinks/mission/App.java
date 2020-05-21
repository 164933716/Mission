package com.versalinks.mission;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import io.realm.Realm;
import io.realm.RealmConfiguration;


public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Realm.init(App.getContext());
        RealmConfiguration config = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().schemaVersion(1).build();
        Realm.setDefaultConfiguration(config);
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
