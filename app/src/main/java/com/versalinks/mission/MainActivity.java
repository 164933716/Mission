package com.versalinks.mission;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.versalinks.mission.databinding.ActivityMainBinding;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(@NonNull final ActivityMainBinding binding) {
        WebViewManager.getInstance().getWebView(this, new WebViewManager.WebViewListener() {
            @SuppressLint("AddJavascriptInterface")
            @Override
            public void webInitOK(WebView webView) {
                FrameLayout container = binding.container;
                FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                container.addView(webView, layoutParams);
                webView.addJavascriptInterface(new JSInterface(), "Android");
                webView.loadUrl("file:///android_asset/model/map.html");
            }
        });
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
        WebViewManager.getInstance().onDestroy();
        super.onDestroy();
    }
}
