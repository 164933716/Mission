package com.versalinks.mission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
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
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.Sort;

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
                            RealmResults<Model_Route> all = realm.where(Model_Route.class).sort("createTime", Sort.DESCENDING).findAll();
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

    public Observable<List<Model_Record>> queryRecord() {
        Observable<List<Model_Record>> listObservable = Observable.create(new ObservableOnSubscribe<List<Model_Record>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model_Record>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            RealmResults<Model_Record> all = realm.where(Model_Record.class).sort("createTime", Sort.DESCENDING).findAll();
                            List<Model_Record> routes = realm.copyFromRealm(all);
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
                            RealmResults<Model_Marker> all = realm.where(Model_Marker.class).sort("createTime", Sort.DESCENDING).findAll();
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

    public Observable<List<Model_Route>> deleteRoute() {
        Observable<List<Model_Route>> listObservable = Observable.create(new ObservableOnSubscribe<List<Model_Route>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model_Route>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            RealmResults<Model_Route> all = realm.where(Model_Route.class).findAll();
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

    public Observable<List<Model_Route>> deleteRoute(String name) {
        Observable<List<Model_Route>> listObservable = Observable.create(new ObservableOnSubscribe<List<Model_Route>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model_Route>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            RealmResults<Model_Route> all = realm.where(Model_Route.class).equalTo("name", name).findAll();
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

    public Observable<Model_Record> saveRecord(List<Model_GPS> gpsList) {
        Observable<Model_Record> routeObservable = Observable.create(new ObservableOnSubscribe<Model_Record>() {
            @Override
            public void subscribe(ObservableEmitter<Model_Record> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            Model_Record route = realm.createObject(Model_Record.class);
                            for (Model_GPS modelGps : gpsList) {
                                Model_GPS model_gps = realm.createObject(Model_GPS.class);
                                model_gps.latitude = modelGps.latitude;
                                model_gps.longitude = modelGps.longitude;
                                model_gps.height = modelGps.height;
                                route.gpsList.add(model_gps);
                            }
                            route.createTime = DataUtils.getNowMills();
                            route.name = DataUtils.getNowString();
                            Model_Record model_route = realm.copyFromRealm(route);
                            emitter.onNext(model_route);
                            emitter.onComplete();
                        }
                    });
                }
            }
        });
        return routeObservable;

    }

    public Observable<Model_Route> saveRoute(Model_Route modelRoute) {
        Observable<Model_Route> routeObservable = Observable.create(new ObservableOnSubscribe<Model_Route>() {
            @Override
            public void subscribe(ObservableEmitter<Model_Route> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            realm.copyToRealmOrUpdate(modelRoute);
                            emitter.onNext(modelRoute);
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
                            object1.height = gps.height;
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

    public static int randomDuration() {
        Random random = new Random();
        return random.nextInt(1000 * 60 * 300);
    }

    public static double randomDistance() {
        Random random = new Random();
        double random1 = Math.random();
        double v = 600 + random1 * random.nextInt(3000);
        return v;
    }

    public static double randomHeight() {
        Random random = new Random();
        double random1 = Math.random();
        return random1 * random.nextInt(500);
    }

    public static double randomUpOrDown() {
        Random random = new Random();
        double random1 = Math.random();
        double v = 100 + random1 * random.nextInt(500);
        return v;
    }

    public static String randomTitle() {
        return "金顶-九龙池";
    }

    public static String randomGoMode() {
        return "walk";
    }

    public static String randomGoDiffclut() {
        return "easy";
    }

    public static String randomDescription() {
        return "从金顶出发步行到九龙池，沿路经过护国寺、棉絮岭、薄刀岭、静心池等，全长大约3公里";
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

    public static String convertToDuration(long seconds) {
        String durationString;
        if (seconds < 60 * 1000) {
            durationString = "1分钟";
        } else if (seconds < 60 * 60 * 1000) {
            int i = new BigDecimal(seconds / (60 * 1000)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            durationString = i + "分钟";
        } else {
            int h = new BigDecimal(seconds / (60 * 60 * 1000)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            int m = new BigDecimal((seconds % (60 * 60 * 1000) / 60)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            String hString = h + "小时";
            String mString = m + "分钟";
            durationString = hString + mString;
        }
        return durationString;
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

    public static String getJson(Context context, String fileName) {
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
    public void createRouteByJson(Context context) {
        BaseOb<Model_Route> baseOb = new BaseOb<Model_Route>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onDataDeal(Model_Route route, String message) {
                if (route != null) {
                    String json = new Gson().toJson(route);
                    AndroidUtil.saveText(AndroidUtil.getTempTxtFile(context, "route.txt"), json);
                } else {
                    LogUtils.e("null");
                }
            }
        };
        String json = DataUtils.getJson(context, "route.json");
        Model_Route o = new Gson().fromJson(json, new TypeToken<Model_Route>() {
        }.getType());
        Observable<Model_Route> routeObservable = DataUtils.getInstance().saveRoute(o);
        baseOb.bindObed(routeObservable);
    }

    public void createRoute(Context context) {
        BaseOb<Model_Route> baseOb = new BaseOb<Model_Route>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onDataDeal(Model_Route route, String message) {
                if (route != null) {
                    String json = new Gson().toJson(route);
                    AndroidUtil.saveText(AndroidUtil.getTempTxtFile(context, "route.txt"), json);
                } else {
                    LogUtils.e("null");
                }
            }
        };
        Model_Route model_route = new Model_Route();
        model_route.createTime = DataUtils.getNowMills();
        model_route.goDuration = DataUtils.randomDuration();
        model_route.name = DataUtils.randomTitle();
        model_route.description = DataUtils.randomDescription();
        model_route.goMode = DataUtils.randomGoMode();
        model_route.goDifficult = DataUtils.randomGoDiffclut();
        model_route.distance = DataUtils.randomDistance();
        model_route.goUp = DataUtils.randomUpOrDown();
        model_route.goDown = DataUtils.randomUpOrDown();
        String json = DataUtils.getJson(context, "route_gps_list.json");
        List<Model_GPS> o = new Gson().fromJson(json, new TypeToken<List<Model_GPS>>() {
        }.getType());
        if (o != null) {
            model_route.gpsList = new RealmList<>();
            model_route.gpsList.addAll(o);
        }
        Observable<Model_Route> routeObservable = DataUtils.getInstance().saveRoute(model_route);
        baseOb.bindObed(routeObservable);
    }
}
