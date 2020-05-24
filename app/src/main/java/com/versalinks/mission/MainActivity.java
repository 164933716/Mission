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
import android.os.IBinder;
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
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.versalinks.mission.databinding.ActivityMainBinding;

import java.io.File;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private WebView webView;
    private GPSService gpsService;
    GPSService.GPSListener gpsListener = new GPSService.GPSListener() {
        @Override
        public void gps(Model_GPS modelGps) {
            binding.ivCurrent.setTag(modelGps);
            webView.evaluateJavascript(OptUtils.updateLocation(modelGps), null);
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
        Intent intent = new Intent(this, GPSService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        int heightOK = AndroidUtil.dp2Px(240);
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
                jump2Activity(intent);
            }
        });
        binding.vMenuChoose.setItemClickListener(new MenuChoose.ItemClickListener() {
            @Override
            public void itemClick(int index) {
                if (index == 1) {
                    jump2Activity(RoutesActivity.class);
                    binding.drawerLayout.closeDrawer(GravityCompat.START, false);
                } else if (index == 2) {
                    jump2Activity(MarkersActivity.class);
                    binding.drawerLayout.closeDrawer(GravityCompat.START, false);
                } else {

                }
            }
        });
        binding.vLayerChoose.setItemClickListener(new LayerChoose.ItemClickListener() {
            @Override
            public void itemClick(Layer.Item item) {

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
                webView.evaluateJavascript(OptUtils.zoomOut(), null);
            }
        });
        binding.iv6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.evaluateJavascript(OptUtils.zoomIn(), null);
            }
        });
        binding.ivCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object tag = binding.ivCurrent.getTag();
                if (tag instanceof Model_GPS) {
                    webView.evaluateJavascript(OptUtils.recenter((Model_GPS) tag), null);
                }
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
//        ValueAnimator animator = ValueAnimator.ofFloat(0.2f, 1f);
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float v1 = (Float) animation.getAnimatedValue();
//                binding.container.setAlpha(v1);
//            }
//        });
//        animator.setInterpolator(new DecelerateInterpolator());
//        animator.setDuration(3000);
//        animator.start();
    }

    public static class JSInterface {
        @JavascriptInterface
        public void fetchRoute(String jsonPoi1, String jsonPoi2) {
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
}
