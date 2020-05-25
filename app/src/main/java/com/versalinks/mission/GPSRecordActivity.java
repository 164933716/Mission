package com.versalinks.mission;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.versalinks.mission.databinding.ActivityGpsRecordBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class GPSRecordActivity extends BaseActivity<ActivityGpsRecordBinding> {
    private Handler handler = new Handler(Looper.getMainLooper());
    private AMap map;
    private MapView mapView;
    private GPSService gpsService;
    private boolean needAnimal = true;
    private Marker srcMarker;
    private Marker targetMarker;
    private Marker userMarker;
    private Polyline line;
    GPSService.GPSEnableListener gpsEnableListener = new GPSService.GPSEnableListener() {
        @Override
        public void enable(String message) {
            binding.tvStatus.setText(message);
        }
    };
    GPSService.GPSListener gpsListener = new GPSService.GPSListener() {
        @Override
        public void gps(Model_GPS modelGps) {
            Log.e("TAG", "gps " + modelGps);
            binding.vCenter.setTag(modelGps);
            if (modelGps != null) {
                if (userMarker == null || userMarker.isRemoved()) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.icon_location);
                    markerOptions.icon(bitmapDescriptor);
                    markerOptions.anchor(0.5f, 0.5f);
                    markerOptions.position(modelGps.getLatLng());
                    userMarker = map.addMarker(markerOptions);
                } else {
                    userMarker.setPosition(modelGps.getLatLng());
                }
                if (needAnimal) {
                    needAnimal = false;
                    animate(modelGps, true);
                }
                Log.e("TAG", "gps hahhah");
            } else {
                needAnimal = true;
                if (userMarker != null) {
                    userMarker.remove();
                }
                Log.e("TAG", "gps hehheh");
            }

        }
    };
    GPSService.TrackListener trackListener = new GPSService.TrackListener() {
        @Override
        public void trackStart() {
            binding.tvStartOrPause.setText("暂停");
            binding.ivStartOrPause.setImageResource(R.drawable.ic_media_pause);
        }

        @Override
        public void trackPause() {
            binding.tvStartOrPause.setText("开始");
            binding.ivStartOrPause.setImageResource(R.drawable.ic_media_start);
        }

        @Override
        public void trackResume() {
            binding.tvStartOrPause.setText("暂停");
            binding.ivStartOrPause.setImageResource(R.drawable.ic_media_pause);
        }

        @Override
        public void trackEnd(List<Model_GPS> gpsList) {
            LogUtils.e("trackEnd");
            gpsService.removeTrackListener(trackListener);
            binding.tvStartOrPause.setText("开始");
            binding.ivStartOrPause.setImageResource(R.drawable.ic_media_start);
            Observable<Model_Record> o = DataUtils.getInstance().saveRecord(gpsList);
            BaseOb<Model_Record> baseOb = new BaseOb<Model_Record>() {
                @Override
                public void onDataDeal(Model_Record data, String message) {
                    TipDialog tipDialog = new TipDialog(context, "本次轨迹已保存", "可在我的-我的轨迹里进行管理");
                    tipDialog.show();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tipDialog.dismiss();
                            finish();
                        }
                    }, 1000);
                }
            };
            baseOb.bindObed(o);
        }

        @Override
        public void trackProgress(double altitude, double distance, long duration, List<Model_GPS> list) {
            binding.tvHeight.setText(DataUtils.convertToDistance(altitude));
            binding.tvDistance.setText(DataUtils.convertToDistance(distance));
            binding.tvDuration.setText(DataUtils.convertToTime(duration));
            List<LatLng> latLngs = new ArrayList<>();
            for (Model_GPS model_gps : list) {
                latLngs.add(model_gps.getLatLng());
            }
            Model_GPS gpsSrc = null;
            Model_GPS gpsTarget = null;
            if (list.size() >= 2) {
                gpsSrc = list.get(0);
                gpsTarget = list.get(list.size() - 1);
            } else if (latLngs.size() >= 1) {
                gpsSrc = list.get(0);
                gpsTarget = null;
            }
            if (gpsSrc != null) {
                if (srcMarker == null || srcMarker.isRemoved()) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.map_marker_src);
                    markerOptions.icon(bitmapDescriptor);
                    markerOptions.anchor(0.5f, 0.5f);
                    markerOptions.position(gpsSrc.getLatLng());
                    srcMarker = map.addMarker(markerOptions);
                } else {
                    srcMarker.setPosition(gpsSrc.getLatLng());
                }
            } else {
                if (srcMarker != null) {
                    srcMarker.remove();
                }
            }
            if (gpsTarget != null) {
                if (targetMarker == null) {
                    MarkerOptions markerOptions = new MarkerOptions();
                    BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.map_marker_target);
                    markerOptions.icon(bitmapDescriptor);
                    markerOptions.anchor(0.5f, 0.5f);
                    markerOptions.position(gpsTarget.getLatLng());
                    targetMarker = map.addMarker(markerOptions);
                } else {
                    targetMarker.setPosition(gpsTarget.getLatLng());
                }
            } else {
                if (targetMarker != null) {
                    targetMarker.remove();
                }
            }

            if (line == null) {
                line = map.addPolyline(new PolylineOptions().
                        addAll(latLngs).width(10).color(Color.parseColor("#3F9E6B")));
            } else {
                line.setPoints(latLngs);
            }
        }
    };
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("TAG", "GPSRecordActivity onServiceConnected");
            if (service instanceof GPSService.GPSBinder) {
                gpsService = ((GPSService.GPSBinder) service).getService();
                gpsService.addGPSEnableListener(gpsEnableListener);
                gpsService.addGPSListener(gpsListener);
                gpsService.addTrackListener(trackListener);

                GPSService.RecordState recordState = gpsService.getRecordState();
                if (recordState == GPSService.RecordState.Normal) {
                    binding.tvStartOrPause.setText("开始");
                    binding.ivStartOrPause.setImageResource(R.drawable.ic_media_start);
                } else if (recordState == GPSService.RecordState.Ing) {
                    binding.tvStartOrPause.setText("暂停");
                    binding.ivStartOrPause.setImageResource(R.drawable.ic_media_pause);
                    trackListener.trackProgress(gpsService.getList().size() > 0 ? gpsService.getList().get(gpsService.getList().size() - 1).height : 0, gpsService.getDistance(), gpsService.getDuration(), gpsService.getList());
                } else if (recordState == GPSService.RecordState.Pause) {
                    binding.tvStartOrPause.setText("开始");
                    binding.ivStartOrPause.setImageResource(R.drawable.ic_media_start);
                    trackListener.trackProgress(gpsService.getList().size() > 0 ? gpsService.getList().get(gpsService.getList().size() - 1).height : 0, gpsService.getDistance(), gpsService.getDuration(), gpsService.getList());
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (gpsService != null) {
                gpsService.removeGPSEnableListener(gpsEnableListener);
                gpsService.removeGPSListener(gpsListener);
                gpsService.removeTrackListener(trackListener);
                gpsService.stopLocate();
                gpsService.stopTrack();
            }
            gpsService = null;
        }
    };

    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
        binding.vScaleView.setScaleClickListener(new ScaleView.ScaleClickListener() {
            @Override
            public void bigClick() {
                scaleBig();
            }

            @Override
            public void smallClick() {
                scaleSmall();
            }
        });
        binding.vBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        binding.vCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = binding.vCenter.getTag();
                if (tag instanceof Model_GPS) {
                    animate((Model_GPS) tag, false);
                }
            }
        });
        binding.vStartOrPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsService != null) {
                    GPSService.RecordState recordState = gpsService.getRecordState();
                    if (recordState == GPSService.RecordState.Normal) {
                        gpsService.startTrack();
                    } else if (recordState == GPSService.RecordState.Ing) {
                        gpsService.pauseTrack();
                    } else if (recordState == GPSService.RecordState.Pause) {
                        gpsService.resumeTrack();
                    }
                }
            }
        });
        binding.vStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsService != null) {
                    GPSService.RecordState recordState = gpsService.getRecordState();
                    if (recordState == GPSService.RecordState.Normal) {
                        return;
                    }
                    if (recordState == GPSService.RecordState.NUll) {
                        return;
                    }
                    if (gpsService.getDuration() <= 3) {
                        ToastUtils.showShort("记录时间过短");
                        return;
                    }
                    gpsService.stopTrack();
                }
            }
        });

        mapView = new MapView(context);
        binding.container.addView(mapView, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mapView.onCreate(savedInstanceState);
        if (map == null) {
            map = mapView.getMap();
        }
        map.clear();
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(false);
        uiSettings.setZoomControlsEnabled(false);
        uiSettings.setCompassEnabled(false);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setZoomGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
        uiSettings.setIndoorSwitchEnabled(false);
        uiSettings.setLogoLeftMargin(AndroidUtil.dp2Px(10));
        uiSettings.setLogoBottomMargin(AndroidUtil.dp2Px(10));
        map.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {

            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                float maxZoomLevel = map.getMaxZoomLevel();
                float minZoomLevel = map.getMinZoomLevel();
                float zoom = map.getCameraPosition().zoom;
                if (zoom >= maxZoomLevel) {
                    binding.vScaleView.setBigEnable(false);
                } else {
                    binding.vScaleView.setBigEnable(true);
                }
                if (zoom <= minZoomLevel) {
                    binding.vScaleView.setSmallEnable(false);
                } else {
                    binding.vScaleView.setSmallEnable(true);
                }

            }
        });
        Intent intent = new Intent(this, GPSService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_gps_record);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (gpsService != null) {
            gpsService.removeGPSEnableListener(gpsEnableListener);
            gpsService.removeGPSListener(gpsListener);
            gpsService.removeTrackListener(trackListener);
            unbindService(connection);
        }
        mapView.onDestroy();
        super.onDestroy();
    }

    public void scaleBig() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomIn();
        if (map != null) {
            map.animateCamera(cameraUpdate);
        }
    }

    public void scaleSmall() {
        CameraUpdate cameraUpdate = CameraUpdateFactory.zoomOut();
        if (map != null) {
            map.animateCamera(cameraUpdate);
        }
    }

    public void animate(Model_GPS modelGps, boolean needScale) {
        if (modelGps == null || modelGps.getLatLng() == null) {
            return;
        }
        CameraUpdate cameraUpdate;
        if (needScale) {
            cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(modelGps.getLatLng(), map.getMaxZoomLevel(), 45, 0));
        } else {
            cameraUpdate = CameraUpdateFactory.newLatLng(modelGps.getLatLng());
        }
        map.animateCamera(cameraUpdate);
    }
}
