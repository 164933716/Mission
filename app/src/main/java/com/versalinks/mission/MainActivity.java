package com.versalinks.mission;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.versalinks.mission.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.realm.RealmList;

import static androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED;
import static com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED;

public class MainActivity extends BaseActivity<ActivityMainBinding> {
    private Handler handler = new Handler(Looper.getMainLooper());
    private WebView webView;
    private GPSService gpsService;
    boolean needAnimal = true;
    GPSService.GPSListener gpsListener = new GPSService.GPSListener() {
        @Override
        public void gps(Model_GPS modelGps) {
            Model_GPS model_gps = new Model_GPS();
            model_gps.latitude = 27.8601391146d;
            model_gps.longitude = 108.7107853492d;
            model_gps.height = 1446.697d;
            binding.ivCurrent.setTag(model_gps);
            if (initOk) {
                webView.evaluateJavascript(OptUtils.updateLocation(model_gps), null);
                if (needAnimal) {
//                    webView.evaluateJavascript(OptUtils.recenter(), null);
                    needAnimal = false;
                }
            }

        }
    };
    GPSService.TrackListener trackListener = new GPSService.TrackListener() {
        @Override
        public void trackStart() {
            valueAnimatorForRecord.start();
        }

        @Override
        public void trackPause() {
            valueAnimatorForRecord.cancel();
            binding.vGpsOpt.setAlpha(1f);
        }

        @Override
        public void trackResume() {
            valueAnimatorForRecord.start();
        }

        @Override
        public void trackEnd(List<Model_GPS> gpsList) {
            LogUtils.e("trackEnd");
            valueAnimatorForRecord.cancel();
            binding.vGpsOpt.setAlpha(1f);
        }

        @Override
        public void trackProgress(double altitude, double distance, long duration, List<Model_GPS> list) {

        }
    };
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("TAG", "onServiceConnected");
            if (service instanceof GPSService.GPSBinder) {
                gpsService = ((GPSService.GPSBinder) service).getService();
                gpsService.addGPSListener(gpsListener);
                gpsService.addTrackListener(trackListener);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            if (gpsService != null) {
                gpsService.removeGPSListener(gpsListener);
                gpsService.stopLocate();
            }
            gpsService = null;
        }
    };
    private ValueAnimator valueAnimatorForRecord;
    private long mLastClickTime;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean initOk = false;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
        valueAnimatorForRecord = ValueAnimator.ofFloat(0.4f, 1f);
        valueAnimatorForRecord.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float v1 = (Float) animation.getAnimatedValue();
                binding.vGpsOpt.setAlpha(v1);
            }
        });
        valueAnimatorForRecord.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimatorForRecord.setRepeatMode(ValueAnimator.REVERSE);
        valueAnimatorForRecord.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimatorForRecord.setDuration(600);

        int heightOK = AndroidUtil.dp2Px(210);
        binding.vGestureOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float v1 = (Float) animation.getAnimatedValue();
                        ViewGroup.LayoutParams layoutParams = binding.vGestureLayout.getLayoutParams();
                        layoutParams.height = (int) (v1 * heightOK);
                        binding.vGestureLayout.setLayoutParams(layoutParams);
                    }
                });
                valueAnimator.setDuration(200);
                if (0 == binding.vGestureLayout.getHeight()) {
                    valueAnimator.start();
                } else if (heightOK == binding.vGestureLayout.getHeight()) {
                    valueAnimator.reverse();
                }
            }
        });

        binding.vLayerOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.END, true);
            }
        });
        binding.vMenuOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawerLayout.openDrawer(GravityCompat.START, true);
            }
        });
        binding.vMarkOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(GPSMarkActivity.class);
            }
        });
        binding.vGpsOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(GPSRecordActivity.class);
            }
        });
        binding.vRoutesOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                File file = captureWebViewX(webView);
//                if (file != null) {
//                    LogUtils.e(file.getAbsolutePath());
//                }
                Intent intent = new Intent(context, RoutesActivity.class);
//                intent.putExtra("thumbFile", file);
                jump2Activity(intent, 669);
            }
        });
        binding.vMenuChoose.setItemClickListener(new MenuChoose.ItemClickListener() {
            @Override
            public void itemClick(int index) {
                if (index == 1) {
                    jump2Activity(RecordsActivity.class, 668);
                } else if (index == 2) {
                    jump2Activity(MarkersActivity.class, 667);
                } else {

                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.drawerLayout.closeDrawer(GravityCompat.START, true);
                    }
                }, 500);
            }
        });
        binding.vLayerChoose.setItemClickListener(new LayerChoose.ItemClickListener() {
            @Override
            public void itemClick(Layer.Item item, boolean check) {
                if (TextUtils.equals(item.label, "道路")) {
                    if (check) {
                        webView.evaluateJavascript(OptUtils.showRoadLayer(), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.removeRoadLayer(), null);
                    }
                } else if (TextUtils.equals(item.label, "动物")) {
                    if (check) {
                        String json = DataUtils.getJson(context, "动物.geojson");
                        webView.evaluateJavascript(OptUtils.showAnimalLayer(json), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.removeAnimalLayer(), null);
                    }
                } else if (TextUtils.equals(item.label, "植物")) {
                    if (check) {
                        String json = DataUtils.getJson(context, "植物.geojson");
                        webView.evaluateJavascript(OptUtils.showPlantLayer(json), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.removePlantLayer(), null);
                    }
                } else if (TextUtils.equals(item.label, "自然科普")) {
                    if (check) {
                        String json = DataUtils.getJson(context, "自然科普.geojson");
                        webView.evaluateJavascript(OptUtils.showNaturalScienceLayer(json), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.removeNaturalScienceLayer(), null);
                    }
                } else if (TextUtils.equals(item.label, "观光旅游")) {
                    if (check) {
                        String json = DataUtils.getJson(context, "观光旅游.geojson");
                        webView.evaluateJavascript(OptUtils.showSightseeingLayer(json), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.removeSightseeingLayer(), null);
                    }
                } else if (TextUtils.equals(item.label, "专项旅游")) {
                    if (check) {
                        String json = DataUtils.getJson(context, "专项旅游.geojson");
                        webView.evaluateJavascript(OptUtils.showSpecialTourismLayer(json), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.removeSpecialTourismLayer(), null);
                    }
                } else if (TextUtils.equals(item.label, "山峰")) {
                    if (check) {
                        String json = DataUtils.getJson(context, "山峰.geojson");
                        webView.evaluateJavascript(OptUtils.showMountainPeakLayer(json), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.removeMountainPeakLayer(), null);
                    }
                } else if (TextUtils.equals(item.label, "村庄")) {
                    if (check) {
                        String json = DataUtils.getJson(context, "村庄.geojson");
                        webView.evaluateJavascript(OptUtils.showVillageLayer(json), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.removeVillageLayer(), null);
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.drawerLayout.closeDrawer(GravityCompat.END, true);
                    }
                }, 300);
            }
        });
        binding.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.rotateByDown(), null);
            }
        });
        binding.iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.rotateByUp(), null);
            }
        });
        binding.iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.rotateByLeft(), null);
            }
        });
        binding.iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.rotateByRight(), null);
            }
        });
        binding.iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.zoomIn(), null);
            }
        });
        binding.iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.zoomOut(), null);
            }
        });
        binding.ivCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Object tag = binding.ivCurrent.getTag();
//                if (tag instanceof Model_GPS) {
                webView.evaluateJavascript(OptUtils.recenter(), null);
//                }
            }
        });
        binding.ivCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.pointToNorth(), null);
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(binding.behavior);
        bottomSheetBehavior.setState(STATE_COLLAPSED);
        int height = AndroidUtil.dp2Px(120);

        bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float mSlideOffset) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.height = (int) (height * (1 - mSlideOffset));
                binding.containerRouteInfo.setLayoutParams(layoutParams);
                binding.containerRouteInfo.setAlpha(1 - mSlideOffset);
            }
        });

        binding.vContainerRouteInfoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setRouteToNULL();
                binding.containerNormal.setVisibility(View.VISIBLE);
                Object tag = binding.ivCurrent.getTag();
                if (tag instanceof Model_GPS) {
//                    webView.evaluateJavascript(OptUtils.flyTo((Model_GPS) tag), null);
                }
            }
        });
        binding.vRouteInfoFly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = binding.vRouteInfoFly.getTag();
                if (tag != null) {
                    webView.evaluateJavascript(OptUtils.flyPause(), null);
                    binding.vRouteInfoFly.setTag(null);
                    binding.vRouteInfoFly.setImageResource(R.drawable.ic_fly);
                } else {
                    webView.evaluateJavascript(OptUtils.flyStart(), null);
                    binding.vRouteInfoFly.setTag("flying");
                    binding.vRouteInfoFly.setImageResource(R.drawable.ic_media_pause);
                }
            }
        });
        binding.vRouteInfoCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                webView.evaluateJavascript(OptUtils.flyStop(), null);
            }
        });
        binding.vRouteInfoHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = binding.routeChart.getTag();
                if (tag instanceof Model_Route) {
                    Model_Route item = (Model_Route) tag;
                    binding.containerRoute.setVisibility(View.GONE);
                    binding.containerRouteChart.setVisibility(View.VISIBLE);
                    binding.routeChart.setPoints(item.gpsList);
                    binding.tvRouteChart.setText("距离：" + DataUtils.convertToDistance(0) + "\u0020" + "|" + "\u0020" + "海拔：" + DataUtils.convertToDistance(item.gpsList.get(0).height));
                } else if (tag instanceof Model_Record) {
                    Model_Record item = (Model_Record) tag;
                    binding.containerRoute.setVisibility(View.GONE);
                    binding.containerRouteChart.setVisibility(View.VISIBLE);
                    binding.routeChart.setPoints(item.gpsList);
                    binding.tvRouteChart.setText("距离：" + DataUtils.convertToDistance(0) + "\u0020" + "|" + "\u0020" + "海拔：" + DataUtils.convertToDistance(item.gpsList.get(0).height));
                }
            }
        });
        binding.vRouteChartClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.containerRouteChart.setVisibility(View.GONE);
                binding.containerRoute.setVisibility(View.VISIBLE);
            }
        });
        binding.vRouteDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(STATE_COLLAPSED);
                    return;
                }
            }
        });
        binding.routeChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Object data1 = e.getData();
                LogUtils.e(data1);
                float x = e.getX();
                float y = e.getY();
                binding.tvRouteChart.setText("距离：" + DataUtils.convertToDistance(x) + "\u0020" + "|" + "\u0020" + "海拔：" + DataUtils.convertToDistance(y));
            }

            @Override
            public void onNothingSelected() {
                binding.tvRouteChart.setText(null);

            }
        });
        binding.ivMarkerShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog = new ShareDialog(context);
                shareDialog.show();
            }
        });
        binding.vMarkerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMarkerToNULL();
                Object tag = binding.ivCurrent.getTag();
                if (tag instanceof Model_GPS) {
//                    webView.evaluateJavascript(OptUtils.flyTo((Model_GPS) tag), null);
                }
                binding.containerNormal.setVisibility(View.VISIBLE);
            }
        });
        webView = new WebView(context.getApplicationContext());
        WebSettings mWebSettings = webView.getSettings();
        mWebSettings.setSupportZoom(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setAllowContentAccess(true);
        mWebSettings.setAllowFileAccess(true);
        mWebSettings.setAllowFileAccessFromFileURLs(true);
        mWebSettings.setAllowUniversalAccessFromFileURLs(true);
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setHorizontalScrollBarEnabled(false);//水平不显示
        webView.setVerticalScrollBarEnabled(false); //垂直不显示
        String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
        mWebSettings.setAppCachePath(appCachePath);
        WebViewClient webViewClient = new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        };
        webView.setWebViewClient(webViewClient);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                if (i >= 100) {
                    webView.evaluateJavascript(OptUtils.init(), null);
                }
            }
        });
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        binding.drawerLayout.addView(webView, 0, layoutParams);
        webView.addJavascriptInterface(new JSInterface(), "Android");
        webView.loadUrl("file:///android_asset/model/map.html");
        Intent intent = new Intent(this, GPSService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public class JSInterface {
        @JavascriptInterface
        public void fetchRoute(String jsonPoi1, String jsonPoi2) {
        }

        @JavascriptInterface
        public void putUserTourHeights(String json) {
            LogUtils.e("json    " + json);
        }

        @JavascriptInterface
        public void putCameraParam(String json) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        double heading = jsonObject.optDouble("heading");
//                LogUtils.e("heading     " + heading);
                        binding.ivCompass.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.ivCompass.setRotation(-(float) heading);
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @JavascriptInterface
        public void poiClick(String json) {
            LogUtils.e("json    " + json);
//            Intent intent = new Intent(context, PoiDetailActivity.class);
//            intent.putExtra("geoJson", json);
//            jump2Activity(intent);
        }

        @JavascriptInterface
        public void flyThroughStoped() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    binding.vRouteInfoFly.setTag(null);
                    binding.vRouteInfoFly.setImageResource(R.drawable.ic_fly);
                }
            });

        }

        @JavascriptInterface
        public void initOk() {
            handler.post(new Runnable() {
                @Override
                public void run() {
//                    String json1 = DataUtils.getJson(context, "自然科普.geojson");
//                    webView.evaluateJavascript(OptUtils.showNaturalScienceLayer(json1), null);
                    String json2 = DataUtils.getJson(context, "植物.geojson");
                    webView.evaluateJavascript(OptUtils.showPlantLayer(json2), null);
                    initOk = true;
                }
            });

        }
    }

    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        if (gpsService != null) {
            gpsService.removeGPSListener(gpsListener);
            gpsService.stopLocate();
            unbindService(connection);
        }
        if (webView != null) {
            webView.removeJavascriptInterface("Android");
            webView.loadUrl("about:blank");
            webView.stopLoading();
            ViewParent parent = webView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(webView);
            }
            webView.setWebChromeClient(null);
            webView.setWebViewClient(null);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

//    private File captureWebViewX(WebView webView) {
//        int wholeWidth = webView.computeHorizontalScrollRange();
//        int wholeHeight = webView.computeVerticalScrollRange();
//        Bitmap bitmap = Bitmap.createBitmap(wholeWidth, wholeHeight, Bitmap.Config.ARGB_8888);
//        Canvas x5canvas = new Canvas(bitmap);
//        x5canvas.scale((float) wholeWidth / (float) webView.getContentWidth(), (float) wholeHeight / (float) webView.getContentHeight());
//        if (webView.getX5WebViewExtension() == null) {
//            return null;
//        }
//        webView.getX5WebViewExtension().snapshotWholePage(x5canvas, false, false);
//        File tempImageFile = AndroidUtil.getTempImageFile(context);
//        AndroidUtil.bitmap2File(bitmap, tempImageFile);
//        return tempImageFile;
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 669) {
            if (data != null) {
                //路线选择
                Parcelable model_routeSer = data.getParcelableExtra("model_route");
                if (model_routeSer instanceof Model_Route) {
                    Model_Route item = (Model_Route) model_routeSer;
                    if (webView != null) {
                        showRoute(item);
//                        String userTourHeights = OptUtils.getUserTourHeights(new Gson().toJson(item.gpsList));
//                        webView.evaluateJavascript(userTourHeights, null);
                    }
                }

            }
        } else if (requestCode == 668) {
            if (data != null) {
                //轨迹选择
                Parcelable model_recordSer = data.getParcelableExtra("model_record");
                if (model_recordSer instanceof Model_Record) {
                    Model_Record item = (Model_Record) model_recordSer;
                    if (webView != null) {
                        showRecord(item);
                    }
                }
            }
        } else if (requestCode == 667) {
            if (data != null) {
                //标注选择
                Parcelable model_markerSer = data.getParcelableExtra("model_marker");
                if (model_markerSer instanceof Model_Marker) {
                    Model_Marker item = (Model_Marker) model_markerSer;
                    if (webView != null) {
                        showMarker(item);
                    }
                }
            }
        } else {

        }
    }

    private void showMarker(Model_Marker modelMarker) {
        setRouteToNULL();
//        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        binding.containerNormal.setVisibility(View.GONE);
        binding.containerMarker.setVisibility(View.VISIBLE);
        webView.evaluateJavascript(OptUtils.updatePoiLocation(modelMarker), null);
        binding.tvMarkerName.setText(modelMarker.name);
        binding.tvMarkerGPS.setText(modelMarker.gps.toShow());
        binding.tvMarkerHeight.setText(DataUtils.convertToDistance(modelMarker.gps.height));

    }

    private void showRoute(Model_Route item) {
        setMarkerToNULL();
//        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        binding.routeChart.setTag(item);
        RealmList<Model_GPS> gpsList = item.gpsList;

        binding.containerNormal.setVisibility(View.GONE);
        binding.containerRoute.setVisibility(View.VISIBLE);
        binding.containerRouteInfo.setVisibility(View.VISIBLE);
        binding.containerRouteChart.setVisibility(View.GONE);

        webView.evaluateJavascript(OptUtils.updateUserTour(gpsList), null);
        binding.tvRouteInfoTitle.setText(item.name);
        binding.tvRouteInfoDescription.setText(item.description);

    }

    private void showRecord(Model_Record item) {
        setMarkerToNULL();
//        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        binding.routeChart.setTag(item);
        RealmList<Model_GPS> gpsList = item.gpsList;

        binding.containerNormal.setVisibility(View.GONE);
        binding.containerRoute.setVisibility(View.VISIBLE);
        binding.containerRouteInfo.setVisibility(View.VISIBLE);
        binding.containerRouteChart.setVisibility(View.GONE);

        webView.evaluateJavascript(OptUtils.updateUserTour(gpsList), null);
        binding.tvRouteInfoTitle.setText(item.name);
        binding.tvRouteInfoDescription.setText(item.description);
    }

    @Override
    public void onBackPressed() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(STATE_COLLAPSED);
            return;
        }
        if (binding.containerMarker.getVisibility() == View.VISIBLE) {
            setMarkerToNULL();
            binding.containerNormal.setVisibility(View.VISIBLE);
            Object tag = binding.ivCurrent.getTag();
            if (tag instanceof Model_GPS) {
//                webView.evaluateJavascript(OptUtils.flyTo((Model_GPS) tag), null);
            }
            return;
        }
        if (binding.containerRouteChart.getVisibility() == View.VISIBLE) {
            binding.containerRouteChart.setVisibility(View.GONE);
            binding.containerRoute.setVisibility(View.VISIBLE);
            return;
        }
        if (binding.containerRoute.getVisibility() == View.VISIBLE) {
            setRouteToNULL();
            binding.containerNormal.setVisibility(View.VISIBLE);
            Object tag = binding.ivCurrent.getTag();
            if (tag instanceof Model_GPS) {
//                webView.evaluateJavascript(OptUtils.flyTo((Model_GPS) tag), null);
            }
            return;
        }
        final long now = System.currentTimeMillis();
        if (now - mLastClickTime > 1000) {
            mLastClickTime = now;
            ToastUtils.showShort("再按一次退出应用");
        } else {
            super.onBackPressed();

        }
    }

    private void setRouteToNULL() {
        webView.evaluateJavascript(OptUtils.flyStop(), null);

        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED, GravityCompat.START);
        webView.evaluateJavascript(OptUtils.clearUserTour(), null);
        binding.containerRoute.setVisibility(View.GONE);
        binding.containerRouteInfo.setVisibility(View.GONE);
        binding.containerRouteChart.setVisibility(View.GONE);
        binding.tvRouteInfoTitle.setText(null);
        binding.tvRouteInfoDescription.setText(null);
    }

    private void setMarkerToNULL() {
        binding.drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED, GravityCompat.START);
        webView.evaluateJavascript(OptUtils.clearPoiLocation(), null);
        webView.evaluateJavascript(OptUtils.clearPoiDetail(), null);
        binding.containerMarker.setVisibility(View.GONE);
        binding.tvMarkerName.setText(null);
        binding.tvMarkerGPS.setText(null);
        binding.tvMarkerHeight.setText(null);
    }
}
