package com.versalinks.mission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.TimeUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.realm.Realm;
import io.realm.RealmResults;

public class DataUtils {
    private static DataUtils dataUtils;

    private DataUtils() {

    }

    public static DataUtils getInstance() {
        if (dataUtils == null) {
            synchronized (DataUtils.class) {
                if (dataUtils == null) {
                    dataUtils = new DataUtils();
                }
            }
        }
        return dataUtils;
    }

    public static String nowDate() {
        return new SimpleDateFormat("yyyy-MM-dd_HH-MM-ss").format(new Date());
    }

    public Observable<List<Model_Route>> queryRoute() {
        Observable<List<Model_Route>> listObservable = Observable.create(new ObservableOnSubscribe<List<Model_Route>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model_Route>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            RealmResults<Model_Route> all = realm.where(Model_Route.class).findAll();
                            List<Model_Route> routes = realm.copyFromRealm(all);
                            emitter.onNext(routes);
                            emitter.onComplete();
                        }
                    });
                }
            }
        });
        return listObservable;
    }

    public Observable<List<Model_Marker>> queryMarker() {
        Observable<List<Model_Marker>> listObservable = Observable.create(new ObservableOnSubscribe<List<Model_Marker>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model_Marker>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            RealmResults<Model_Marker> all = realm.where(Model_Marker.class).findAll();
                            List<Model_Marker> markers = realm.copyFromRealm(all);
                            emitter.onNext(markers);
                            emitter.onComplete();
                        }
                    });
                }
            }
        });
        return listObservable;
    }

    public Observable<List<Model_Route>> updateRoute(String name, Model_Route modelRoute) {
        //Route{time=1590057601112, routeName='2020-05-21 18:40:01', gpsList= 0}
        Observable<List<Model_Route>> listObservable = Observable.create(new ObservableOnSubscribe<List<Model_Route>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model_Route>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            RealmResults<Model_Route> all = realm.where(Model_Route.class).equalTo("routeName", name).findAll();
                            for (Model_Route model_route : all) {
                                model_route.time = DataUtils.getNowMills();
                                model_route.gpsList.clear();
                                model_route.gpsList.addAll(modelRoute.gpsList);
                            }
                            List<Model_Route> routes = realm.copyFromRealm(all);
                            emitter.onNext(routes);
                            emitter.onComplete();
                        }
                    });
                }

            }
        });
        return listObservable;

    }

    public Observable<List<Model_Route>> deleteRoute(String name) {
        Observable<List<Model_Route>> listObservable = Observable.create(new ObservableOnSubscribe<List<Model_Route>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model_Route>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            RealmResults<Model_Route> all = realm.where(Model_Route.class).equalTo("routeName", name).findAll();
                            List<Model_Route> routes = realm.copyFromRealm(all);
                            all.deleteAllFromRealm();
                            emitter.onNext(routes);
                            emitter.onComplete();
                        }
                    });
                }

            }
        });
        return listObservable;
    }

    public Observable<Model_Route> saveRoute(List<Model_GPS> gpsList) {
        Observable<Model_Route> routeObservable = Observable.create(new ObservableOnSubscribe<Model_Route>() {
            @Override
            public void subscribe(ObservableEmitter<Model_Route> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            Model_Route route = realm.createObject(Model_Route.class);
                            for (Model_GPS modelGps : gpsList) {
                                Model_GPS model_gps = realm.createObject(Model_GPS.class);
                                model_gps.latitude = modelGps.latitude;
                                model_gps.longitude = modelGps.longitude;
                                model_gps.altitude = modelGps.altitude;
                                route.gpsList.add(model_gps);
                            }
                            route.time = DataUtils.getNowMills();
                            route.routeName = DataUtils.getNowString();
                            Model_Route model_route = realm.copyFromRealm(route);
                            emitter.onNext(model_route);
                            emitter.onComplete();
                        }
                    });
                }
            }
        });
        return routeObservable;

    }

    public Observable<Model_Marker> saveMarker(long createTime,
                                               String markerName,
                                               Model_MarkerType markerType,
                                               Model_GPS gps,
                                               List<String> photos) {
        Observable<Model_Marker> routeObservable = Observable.create(new ObservableOnSubscribe<Model_Marker>() {
            @Override
            public void subscribe(ObservableEmitter<Model_Marker> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            Model_Marker item = realm.createObject(Model_Marker.class);
                            item.createTime = createTime;
                            item.markerName = markerName;
                            Model_MarkerType object = realm.createObject(Model_MarkerType.class);
                            object.name = markerType.name;
                            item.markerType = object;
                            Model_GPS object1 = realm.createObject(Model_GPS.class);
                            object1.latitude = gps.latitude;
                            object1.longitude = gps.longitude;
                            object1.altitude = gps.altitude;
                            item.gps = object1;
                            item.photos.addAll(photos);
                            Model_Marker modelMarker = realm.copyFromRealm(item);
                            emitter.onNext(modelMarker);
                            emitter.onComplete();
                        }
                    });
                }
            }
        });
        return routeObservable;

    }

    public static String random() {
        Random random = new Random();
        double random1 = Math.random();
        double v = 600 + random1 * random.nextInt(3000);
        return convertToDistance(v);
    }

    public static String randomUpOrDown() {
        Random random = new Random();
        double random1 = Math.random();
        double v = 100 + random1 * random.nextInt(500);
        return convertToDistance(v);
    }

    public static String randomTitle() {
        return "武大科技园-光谷软件园";
    }

    public static String randomDescription() {
        return "从武大科技园出发步行到光谷软件园，沿路经过万科红郡、万科城市花园、关山大道、关南小区等，全长大约3公里";
    }

    public static String convertToDistance(double distance) {
        String distanceString;
        if (distance < 1000) {
            distanceString = new BigDecimal(distance).setScale(2, BigDecimal.ROUND_HALF_UP).longValue() + "米";
        } else {
            double f1 = new BigDecimal(distance / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            distanceString = f1 + "公里";
        }
        return distanceString;
    }

    public static String convertToDate(long seconds) {
        return TimeUtils.millis2String(seconds, new SimpleDateFormat("yyyy-MM-dd HH:ss"));
    }

    public static String convertToDate(long seconds, SimpleDateFormat format) {
        return TimeUtils.millis2String(seconds, format);
    }

    public static String convertToTime(long seconds) {
        String timeStr = "00:00";
        long hour;
        long minute;
        long second;
        if (seconds > 0) {
            minute = seconds / 60;
            if (minute < 60) {
                second = seconds % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99) {
                    return "99:59:59";
                }
                minute = minute % 60;
                second = seconds - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }

    public static long getNowMills() {
        return System.currentTimeMillis();
    }

    public static String getNowString() {
        return TimeUtils.getNowString();
    }

    public String getJson(Context context, String fileName) {
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

    public static void chooseImage(Activity context, int size, int code) {
        Matisse.from(context)
                .choose(MimeType.ofImage())
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, BuildConfig.APPLICATION_ID + ".provider", AndroidUtil.getFolder(context).getAbsolutePath()))
                .originalEnable(true)
                .showSingleMediaType(true)
                .theme(R.style.Matisse_Dracula)
                .maxSelectable(size)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showPreview(true) // Default is `true`
                .forResult(code);
    }
}
