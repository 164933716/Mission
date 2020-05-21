package com.versalinks.mission;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;

import com.versalinks.mission.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    private WebView webView;
    private GPSService gpsService;
    GPSService.GPSListener gpsListener = new GPSService.GPSListener() {
        @Override
        public void gps(Model_GPS modelGps) {

        }
    };
    ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            if (service instanceof GPSService.GPSBinder) {
                gpsService = ((GPSService.GPSBinder) service).getService();
                gpsService.addGPSListener(gpsListener);
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

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreateByBinding(Bundle savedInstanceState) {
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
        binding.vGpsOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(GPSRecordActivity.class);
            }
        });
        binding.vRoutesOpt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jump2Activity(RoutesActivity.class);
            }
        });
        binding.vMenuChoose.setItemClickListener(new MenuChoose.ItemClickListener() {
            @Override
            public void itemClick(int index) {
                binding.drawerLayout.closeDrawer(GravityCompat.START, false);
                jump2Activity(GPSRecordActivity.class);
            }
        });
        binding.vLayerChoose.setItemClickListener(new LayerChoose.ItemClickListener() {
            @Override
            public void itemClick(Layer.Item item) {

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
}
