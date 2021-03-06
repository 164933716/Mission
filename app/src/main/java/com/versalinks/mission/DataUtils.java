package com.versalinks.mission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.FileIOUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
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

    public Observable<List<Model_Marker>> deleteMarker(String name) {
        Observable<List<Model_Marker>> listObservable = Observable.create(new ObservableOnSubscribe<List<Model_Marker>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Model_Marker>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            RealmResults<Model_Marker> all = realm.where(Model_Marker.class).equalTo("name", name).findAll();
                            List<Model_Marker> markers = realm.copyFromRealm(all);
                            all.deleteAllFromRealm();
                            emitter.onNext(markers);
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

    public Observable<List<Feature>> saveMarker(List<Feature> features) {
        Observable<List<Feature>> routeObservable = Observable.create(new ObservableOnSubscribe<List<Feature>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Feature>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            for (Feature feature : features) {
                                Geometry geometry = feature.geometry();
                                if (geometry instanceof Point) {
                                    Point point = (Point) geometry;
                                    double longitude = point.longitude();
                                    double latitude = point.latitude();
                                    double altitude = point.altitude();
                                    Model_Marker item = new Model_Marker();
                                    item.createTime = DataUtils.getAimMills();
                                    item.name = feature.getStringProperty("名称");
                                    item.type = new Model_MarkerType("景点");
                                    item.gps = new Model_GPS(longitude, latitude, altitude);
                                    item.photos = new RealmList<>();
                                    String image = feature.getStringProperty("图片");
                                    if (!TextUtils.isEmpty(image)) {
                                        item.photos.add(image);
                                    }
                                    realm.copyToRealmOrUpdate(item);
                                }
                            }
                            emitter.onNext(features);
                            emitter.onComplete();
                        }
                    });
                }
            }
        });
        return routeObservable;
    }

    public Observable<List<Feature>> saveRoute(List<Feature> features) {
        Observable<List<Feature>> routeObservable = Observable.create(new ObservableOnSubscribe<List<Feature>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Feature>> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            for (int i = 0; i < features.size(); i++) {
                                Feature feature = features.get(i);
                                Geometry geometry = feature.geometry();
                                if (geometry instanceof LineString) {
                                    LineString lineString = (LineString) geometry;
                                    Model_Route item = new Model_Route();
                                    item.createTime = feature.getNumberProperty("createTime").longValue();
                                    item.goDuration = feature.getNumberProperty("goDuration").longValue();
                                    item.name = feature.getStringProperty("name");
                                    item.description = feature.getStringProperty("description");
                                    item.goDifficulty = feature.getStringProperty("goDifficulty");
                                    item.distance = feature.getNumberProperty("distance").doubleValue();
                                    item.distanceByAltitude = feature.getNumberProperty("distanceByAltitude").doubleValue();
                                    item.altitudeMin = feature.getNumberProperty("altitudeMin").doubleValue();
                                    item.altitudeMax = feature.getNumberProperty("altitudeMax").doubleValue();
                                    item.goUp = feature.getNumberProperty("goUp").doubleValue();
                                    item.goDown = feature.getNumberProperty("goDown").doubleValue();
                                    item.modeList = new RealmList<>();
                                    JsonElement transportation = feature.getProperty("transportation");
                                    JsonArray asJsonArray = transportation.getAsJsonArray();
                                    for (JsonElement jsonElement : asJsonArray) {
                                        String asString = jsonElement.getAsString();
                                        item.modeList.add(asString);
                                    }

                                    item.goMode = new RealmList<>();
                                    JsonElement jsonElement1 = feature.getProperty("goMode");
                                    JsonArray jsonArray1 = jsonElement1.getAsJsonArray();
                                    for (JsonElement element : jsonArray1) {
                                        String asString = element.getAsString();
                                        item.goMode.add(asString);
                                    }

                                    item.gpsList = new RealmList<>();
                                    List<Point> coordinates = lineString.coordinates();
                                    for (Point point : coordinates) {
                                        double longitude = point.longitude();
                                        double latitude = point.latitude();
                                        double altitude = point.altitude();
                                        item.gpsList.add(new Model_GPS(longitude, latitude, altitude));
                                    }
                                    realm.copyToRealmOrUpdate(item);
                                }
                            }
                            emitter.onNext(features);
                            emitter.onComplete();
                        }
                    });
                }
            }
        });
        return routeObservable;
    }

    public Observable<Model_Marker> saveMarker(Model_Marker modelMarker) {
        Observable<Model_Marker> routeObservable = Observable.create(new ObservableOnSubscribe<Model_Marker>() {
            @Override
            public void subscribe(ObservableEmitter<Model_Marker> emitter) throws Exception {
                try (Realm realm = Realm.getDefaultInstance()) {
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(@NonNull Realm realm) {
                            realm.copyToRealmOrUpdate(modelMarker);
                            emitter.onNext(modelMarker);
                            emitter.onComplete();
                        }
                    });
                }
            }
        });
        return routeObservable;

    }


    public static double randomDistance() {
        Random random = new Random();
        double random1 = Math.random();
        double v = 600 + random1 * random.nextInt(3000);
        return v;
    }


    public static double randomUpOrDown() {
        Random random = new Random();
        double random1 = Math.random();
        double v = 100 + random1 * random.nextInt(500);
        return v;
    }


    public static String randomDescription1() {
        return "从张家坝出发步行到苏家坡，沿路经过护国寺、棉絮岭、薄刀岭、静心池等，全长大约8公里";
    }

    public static Pair<String, String> convertToDistanceWithUnit(double distance) {
        Pair<String, String> pair;
        String distanceString;
        if (distance < 1000) {
            long value = new BigDecimal(distance).setScale(0, BigDecimal.ROUND_HALF_UP).longValue();
            pair = new Pair<>(String.valueOf(value), "m");
        } else {
            double value = new BigDecimal(distance / 1000).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
            pair = new Pair<>(String.valueOf(value), "km");
        }
        return pair;
    }

    public static String convertToDistance(double distance) {
        String distanceString;
        if (distance < 1000) {
            distanceString = new BigDecimal(distance).setScale(0, BigDecimal.ROUND_HALF_UP).longValue() + "米";
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

    public static Pair<String, String> convertToDurationWithUnit(long ms) {
        Pair<String, String> pair;
        String durationString;
        if (ms < 60 * 1000) {
            durationString = "1分钟";
            pair = new Pair<>(String.valueOf(1), "小时");
        } else if (ms < 60 * 60 * 1000) {
            int i = new BigDecimal(ms / (60 * 1000)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            durationString = i + "分钟";
            pair = new Pair<>(String.valueOf(i), "小时");
        } else {
            int h = new BigDecimal(ms / (60 * 60 * 1000)).setScale(0, BigDecimal.ROUND_HALF_UP).intValue();
            float m = ((ms % (60 * 60 * 1000)) / (60 * 1000));
            String hString = h + "小时";
            String mString = m + "分钟";
            float v = m / 60;
            float hm = new BigDecimal(h + v).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
            durationString = hm + "小时";
            pair = new Pair<>(String.valueOf(hm), "小时");
        }
        return pair;
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

    public static long getAimMills() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date parse = simpleDateFormat.parse("2020-05-01 09:00:00");
            if (parse != null) {
                return parse.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date().getTime();
    }

    public static String getNowString() {
        return TimeUtils.getNowString();
    }

    public static String getJsonV2(Context context, String fileName) {
        String json = FileIOUtils.readFile2String(new File(AndroidUtil.getFolder(context), "model/data/" + fileName));
        return json;
    }

    public static String getJson(Context context, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open("model/data/" + fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static String getJsonByAssets(Context context, String fileName) {
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
                .originalEnable(false)
                .showSingleMediaType(true)
                .theme(R.style.Matisse_Dracula)
                .maxSelectable(size)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .showPreview(true) // Default is `true`
                .forResult(code);
    }

    public static boolean copyFileOrDir(Context context, String folder, String path) {
        AssetManager assetManager = context.getAssets();
        String[] files = null;
        try {
            files = assetManager.list(path);
            if (files != null) {
                if (files.length == 0) {
                    boolean copyFile = copyFile(context, folder, path);
                    if (!copyFile) {
                        return false;
                    }
                } else {
                    File dir = new File(folder, path);
                    if (!dir.exists()) {
                        dir.mkdir();
                    }
                    for (String file : files) {
                        copyFileOrDir(context, folder, path + "/" + file);
                    }
                }
            }
        } catch (IOException ex) {
            Log.e("tag", "I/O Exception", ex);
            return false;
        }
        return true;
    }

    public static boolean copyFile(Context context, String folder, String filename) {
        AssetManager assetManager = context.getAssets();

        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            File file = new File(folder, filename);
            String newFileName = file.getAbsolutePath();
            out = new FileOutputStream(newFileName);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
            return false;
        }
        return true;
    }
}
