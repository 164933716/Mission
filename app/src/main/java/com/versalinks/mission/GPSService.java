package com.versalinks.mission;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps.AMapUtils;
import com.blankj.utilcode.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class GPSService extends Service {

    private GPSBinder mBinder;
    private AMapLocationClient mLocationClient;
    private List<TrackListener> trackListeners = new ArrayList<>();
    private List<GPSListener> gpsListeners = new ArrayList<>();
    private List<GPSEnableListener> gpsEnableListeners = new ArrayList<>();
    private Handler handler = new Handler(Looper.getMainLooper());

    public List<Model_GPS> getList() {
        return list;
    }

    private List<Model_GPS> list = new ArrayList<>();

    public long getDuration() {
        return duration;
    }

    public double getDistance() {
        return distance;
    }

    private long duration = 0;
    private double distance = 0;
    private RecordState recordState = RecordState.Normal;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            LogUtils.e("runnable    run");
            Model_GPS modelGpsLast = null;
            if (list.size() > 1) {
                modelGpsLast = list.get(list.size() - 1);
            }
            if (modelGps != null) {
                list.add(modelGps);
            }
            duration++;
            if (modelGpsLast != null && modelGps != null) {
                float v = AMapUtils.calculateLineDistance(modelGpsLast.getLatLng(), modelGps.getLatLng());
                distance += v;
            }
            for (TrackListener trackListener : trackListeners) {
                trackListener.trackProgress(list.size() > 0 ? list.get(list.size() - 1).height : 0, distance, duration, list);
            }
            handler.postDelayed(this, 1000);
        }
    };
    private Model_GPS modelGps;
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation mapLocation) {
            if (mapLocation != null) {
                if (mapLocation.getErrorCode() == 0) {
                    double latitude = mapLocation.getLatitude();//获取纬度
                    double longitude = mapLocation.getLongitude();//获取经度
                    String city = mapLocation.getCity();//城市信息
                    double altitude = mapLocation.getAltitude();
                    double[] doubles = CoordinateTransformUtil.gcj02towgs84(longitude, latitude);
                    latitude = doubles[1];
                    longitude = doubles[0];
                    modelGps = new Model_GPS(longitude, latitude, altitude);
//                    Log.e("GPS", "latitude:" + latitude + "longitude:" + longitude + "altitude:" + altitude + "city:" + city);
                    for (GPSListener gpsListener : gpsListeners) {
                        gpsListener.gps(modelGps);
                    }
                    int locationQualityReport = mapLocation.getLocationQualityReport().getGPSStatus();
                    String gpsStatusString = getGPSStatusString(locationQualityReport);
                    for (GPSEnableListener gpsEnableListener : gpsEnableListeners) {
                        gpsEnableListener.enable(gpsStatusString);
                    }

                } else {
                    modelGps = null;
                    Log.e("GPS", "location Error, ErrCode:"
                            + mapLocation.getErrorCode() + ", errInfo:"
                            + mapLocation.getErrorInfo());
                    for (GPSListener gpsListener : gpsListeners) {
                        gpsListener.gps(modelGps);
                    }
                    for (GPSEnableListener gpsEnableListener : gpsEnableListeners) {
                        gpsEnableListener.enable("无法获取GPS");
                    }
                }
            }
        }
    };

    public RecordState getRecordState() {
        return recordState;
    }

    enum SatellitesStatus {
        CONNECTING, NO_SIGNAL, ERROR, OFF, MODEOFF, PERMISSION, CONNECTED
    }

    enum RecordState {
        Pause,
        Ing,
        Normal,
        NUll

    }

    public GPSService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new GPSBinder();
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClient.setLocationListener(locationListener);
        setLocationByGPS(3000);
        startLocate();
    }

    private String getGPSStatusString(int statusCode) {
        String str = "";
        SatellitesStatus satellitesStatus;
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                satellitesStatus = SatellitesStatus.CONNECTED;
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                satellitesStatus = SatellitesStatus.ERROR;
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                satellitesStatus = SatellitesStatus.OFF;
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                satellitesStatus = SatellitesStatus.MODEOFF;
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                satellitesStatus = SatellitesStatus.PERMISSION;
                break;
            default:
                satellitesStatus = SatellitesStatus.CONNECTING;
        }
        return str;
    }


    public void setLocationByGPS(int ms) {
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setInterval(ms);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setSensorEnable(true);
        mLocationClient.setLocationOption(mLocationOption);
    }

    public void addGPSListener(GPSListener gpsListener) {
        this.gpsListeners.add(gpsListener);
    }

    public void removeGPSListener(GPSListener gpsListener) {
        this.gpsListeners.remove(gpsListener);
    }

    public void addTrackListener(TrackListener trackListener) {
        this.trackListeners.add(trackListener);
    }

    public void removeTrackListener(TrackListener trackListener) {
        this.trackListeners.remove(trackListener);
    }

    public void addGPSEnableListener(GPSEnableListener gpsEnableListener) {
        this.gpsEnableListeners.add(gpsEnableListener);
    }

    public void removeGPSEnableListener(GPSEnableListener gpsEnableListener) {
        this.gpsEnableListeners.remove(gpsEnableListener);
    }

    private void startLocate() {
        if (mLocationClient != null) {
            AMapLocation lastKnownLocation = mLocationClient.getLastKnownLocation();
            if (locationListener != null) {
                locationListener.onLocationChanged(lastKnownLocation);
            }
            mLocationClient.startLocation();
        }
    }

    public void pauseTrack() {
        recordState = RecordState.Pause;
        handler.removeCallbacks(runnable);
        for (TrackListener trackListener : trackListeners) {
            trackListener.trackPause();
        }
    }

    public void resumeTrack() {
        recordState = RecordState.Ing;
        handler.post(runnable);
        for (TrackListener trackListener : trackListeners) {
            trackListener.trackResume();
        }
    }

    public void startTrack() {
        setLocationByGPS(1000);
        recordState = RecordState.Ing;
        distance = 0;
        duration = 0;
        list.clear();
        handler.post(runnable);
        for (TrackListener trackListener : trackListeners) {
            trackListener.trackStart();
        }
    }

    public void stopTrack() {
        setLocationByGPS(3000);
        recordState = RecordState.NUll;
        handler.removeCallbacks(runnable);
        List<Model_GPS> gpsList = new ArrayList<>(list.size());
        gpsList.addAll(list);
        for (TrackListener trackListener : trackListeners) {
            trackListener.trackEnd(gpsList);
        }
        duration = 0;
        distance = 0;
        list.clear();
    }

    public void stopLocate() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    interface TrackListener {
        void trackStart();

        void trackPause();

        void trackResume();

        void trackEnd(List<Model_GPS> gpsList);

        void trackProgress(double altitude, double distance, long duration, List<Model_GPS> list);
    }

    interface GPSListener {
        void gps(Model_GPS gps);
    }

    interface GPSEnableListener {
        void enable(String message);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        if (mLocationClient != null) {
            mLocationClient.onDestroy();
        }
        super.onDestroy();
    }

    public class GPSBinder extends Binder {
        GPSService getService() {
            return GPSService.this;
        }
    }
}
