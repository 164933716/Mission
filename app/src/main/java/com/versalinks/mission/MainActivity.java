package com.versalinks.mission;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import com.blankj.utilcode.util.LogUtils;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.versalinks.mission.databinding.ActivityMainBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import io.realm.RealmList;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

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
            webView.evaluateJavascript(OptUtils.updateLocation(model_gps), null);
            if (needAnimal) {
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        webView.evaluateJavascript(OptUtils.recenter(), null);
                    }
                }, 500);
                needAnimal = false;
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
                File file = captureWebViewX(webView);
                if (file != null) {
                    LogUtils.e(file.getAbsolutePath());
                }
                Intent intent = new Intent(context, RoutesActivity.class);
                intent.putExtra("thumbFile", file);
                jump2Activity(intent, 669);
            }
        });
        binding.vMenuChoose.setItemClickListener(new MenuChoose.ItemClickListener() {
            @Override
            public void itemClick(int index) {
                if (index == 1) {
                    jump2Activity(RecordsActivity.class, 668);
                    binding.drawerLayout.closeDrawer(GravityCompat.START, false);
                } else if (index == 2) {
                    jump2Activity(MarkersActivity.class, 667);
                    binding.drawerLayout.closeDrawer(GravityCompat.START, false);
                } else {

                }
            }
        });
        binding.vLayerChoose.setItemClickListener(new LayerChoose.ItemClickListener() {
            @Override
            public void itemClick(Layer.Item item, boolean check) {
                if (TextUtils.equals(item.label, "道路")) {
                    binding.drawerLayout.closeDrawer(GravityCompat.END, true);
                    if (check) {
                        webView.evaluateJavascript(OptUtils.showRoadLayer(), null);
                    } else {
                        webView.evaluateJavascript(OptUtils.hideRoadLayer(), null);
                    }
                }
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
                webView.evaluateJavascript(OptUtils.recenter(), null);
            }
        });
        binding.ivCompass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.pointToNorth(), null);
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
        FrameLayout container = binding.container;
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        container.addView(webView, 0, layoutParams);
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

        }

        @JavascriptInterface
        public void putCameraParam(String json) {

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
    }

    @NonNull
    @Override
    protected View createView(Context context) {
        return createViewByID(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        if (gpsService != null) {
            gpsService.removeGPSListener(gpsListener);
            gpsService.stopLocate();
            unbindService(connection);
        }
        if (webView != null) {
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

    private File captureWebViewX(WebView webView) {
        int wholeWidth = webView.computeHorizontalScrollRange();
        int wholeHeight = webView.computeVerticalScrollRange();
        Bitmap bitmap = Bitmap.createBitmap(wholeWidth, wholeHeight, Bitmap.Config.ARGB_8888);
        Canvas x5canvas = new Canvas(bitmap);
        x5canvas.scale((float) wholeWidth / (float) webView.getContentWidth(), (float) wholeHeight / (float) webView.getContentHeight());
        if (webView.getX5WebViewExtension() == null) {
            return null;
        }
        webView.getX5WebViewExtension().snapshotWholePage(x5canvas, false, false);
        File tempImageFile = AndroidUtil.getTempImageFile(context);
        AndroidUtil.bitmap2File(bitmap, tempImageFile);
        return tempImageFile;
    }

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
                    webView.evaluateJavascript(OptUtils.clearUserTour(), null);
                    binding.containerNormalOpt.setVisibility(View.VISIBLE);
                    binding.vRoutesOpt.setVisibility(View.VISIBLE);
                    binding.containerRoute.setVisibility(View.GONE);
                    binding.containerRouteInfo.setVisibility(View.GONE);
                    binding.containerRouteChart.setVisibility(View.GONE);
                    binding.tvRouteInfoTitle.setText(null);
                    binding.tvRouteInfoDescription.setText(null);

                    Model_Marker modelMarker = (Model_Marker) model_markerSer;
                    webView.evaluateJavascript(OptUtils.updatePoiLocation(modelMarker), null);
                    binding.containerNormalAndRoute.setVisibility(View.GONE);
                    binding.containerMarker.setVisibility(View.VISIBLE);
                    binding.tvMarkerName.setText(modelMarker.name);
                    binding.tvMarkerGPS.setText(modelMarker.gps.toShow());
                    binding.tvMarkerHeight.setText(DataUtils.convertToDistance(modelMarker.gps.height));
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
                            webView.evaluateJavascript(OptUtils.clearPoiLocation(), null);
                            binding.tvMarkerName.setText(null);
                            binding.tvMarkerGPS.setText(null);
                            binding.tvMarkerHeight.setText(null);
                            binding.containerMarker.setVisibility(View.GONE);
                            binding.containerNormalAndRoute.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }
        } else {

        }
    }

    private void showRoute(Model_Route item) {
        webView.evaluateJavascript(OptUtils.clearPoiLocation(), null);
        binding.tvMarkerName.setText(null);
        binding.tvMarkerGPS.setText(null);
        binding.tvMarkerHeight.setText(null);
        binding.containerMarker.setVisibility(View.GONE);
        binding.containerNormalAndRoute.setVisibility(View.VISIBLE);

        RealmList<Model_GPS> gpsList = item.gpsList;
        webView.evaluateJavascript(OptUtils.updateUserTour(gpsList), null);
        //展示线路
        binding.containerRoute.setVisibility(View.VISIBLE);
        binding.containerNormalOpt.setVisibility(View.GONE);
        binding.vRoutesOpt.setVisibility(View.GONE);

        binding.containerRouteInfo.setVisibility(View.VISIBLE);
        binding.containerRouteChart.setVisibility(View.GONE);

        binding.tvRouteInfoTitle.setText(item.name);
        binding.tvRouteInfoDescription.setText(item.description);
        binding.vContainerRouteInfoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.clearUserTour(), null);

                binding.containerNormalOpt.setVisibility(View.VISIBLE);
                binding.vRoutesOpt.setVisibility(View.VISIBLE);
                binding.containerRoute.setVisibility(View.GONE);
                binding.containerRouteInfo.setVisibility(View.GONE);
                binding.containerRouteChart.setVisibility(View.GONE);
                binding.tvRouteInfoTitle.setText(null);
                binding.tvRouteInfoDescription.setText(null);
            }
        });
        binding.vRouteInfoHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.containerRouteChart.setVisibility(View.VISIBLE);
                binding.containerRouteInfo.setVisibility(View.GONE);
                binding.routeChart.setPoints(item.gpsList);
                binding.tvRouteChart.setText("距离：" + DataUtils.convertToDistance(0) + "\u0020" + "|" + "\u0020" + "海拔：" + DataUtils.convertToDistance(item.gpsList.get(0).height));

            }
        });
        binding.vRouteChartClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.containerRouteChart.setVisibility(View.GONE);
                binding.containerRouteInfo.setVisibility(View.VISIBLE);
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
    }

    private void showRecord(Model_Record item) {
        webView.evaluateJavascript(OptUtils.clearPoiLocation(), null);
        binding.tvMarkerName.setText(null);
        binding.tvMarkerGPS.setText(null);
        binding.tvMarkerHeight.setText(null);
        binding.containerMarker.setVisibility(View.GONE);
        binding.containerNormalAndRoute.setVisibility(View.VISIBLE);


        RealmList<Model_GPS> gpsList = item.gpsList;
        webView.evaluateJavascript(OptUtils.updateUserTour(gpsList), null);
        //展示线路
        binding.containerRoute.setVisibility(View.VISIBLE);
        binding.containerNormalOpt.setVisibility(View.GONE);
        binding.vRoutesOpt.setVisibility(View.GONE);

        binding.containerRouteInfo.setVisibility(View.VISIBLE);
        binding.containerRouteChart.setVisibility(View.GONE);

        binding.tvRouteInfoTitle.setText(item.name);
        binding.tvRouteInfoDescription.setText(item.description);
        binding.vContainerRouteInfoClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.clearUserTour(), null);

                binding.containerNormalOpt.setVisibility(View.VISIBLE);
                binding.vRoutesOpt.setVisibility(View.VISIBLE);
                binding.containerRoute.setVisibility(View.GONE);
                binding.containerRouteInfo.setVisibility(View.GONE);
                binding.containerRouteChart.setVisibility(View.GONE);
                binding.tvRouteInfoTitle.setText(null);
                binding.tvRouteInfoDescription.setText(null);
            }
        });
        binding.vRouteInfoHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.containerRouteChart.setVisibility(View.VISIBLE);
                binding.containerRouteInfo.setVisibility(View.GONE);
                binding.routeChart.setPoints(item.gpsList);
                binding.tvRouteChart.setText("距离：" + DataUtils.convertToDistance(0) + "\u0020" + "|" + "\u0020" + "海拔：" + DataUtils.convertToDistance(item.gpsList.get(0).height));

            }
        });
        binding.vRouteChartClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.containerRouteChart.setVisibility(View.GONE);
                binding.containerRouteInfo.setVisibility(View.VISIBLE);
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
    }

    @Override
    public void onBackPressed() {
        if (binding.containerMarker.getVisibility() == View.VISIBLE) {
            webView.evaluateJavascript(OptUtils.clearPoiLocation(), null);
            binding.tvMarkerName.setText(null);
            binding.tvMarkerGPS.setText(null);
            binding.tvMarkerHeight.setText(null);
            binding.containerMarker.setVisibility(View.GONE);
            binding.containerNormalAndRoute.setVisibility(View.VISIBLE);
            return;
        }
        super.onBackPressed();
    }
}
