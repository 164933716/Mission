package com.versalinks.mission;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewManager {
    private static WebView mWebView;

    private WebViewManager() {

    }

    public static WebView getInstance(Context context) {
        if (mWebView == null) {
            synchronized (WebViewManager.class) {
                if (mWebView == null) {
                    mWebView = new WebView(context.getApplicationContext());
                    WebSettings mWebSettings = mWebView.getSettings();
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
                    mWebSettings.setSupportMultipleWindows(false);
                    String appCachePath = context.getApplicationContext().getCacheDir().getAbsolutePath();
                    mWebSettings.setAppCachePath(appCachePath);
                    WebViewClient webViewClient = new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                            view.loadUrl(url);
                            return true;
                        }
                    };
                    mWebView.setWebViewClient(webViewClient);
                }
            }
        }
        ViewParent parent = mWebView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(mWebView);
        }
        return mWebView;
    }


    public void onDestroy() {
        if (mWebView != null) {
            mWebView.loadUrl("about:blank");
            mWebView.stopLoading();
            ViewParent parent = mWebView.getParent();
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(mWebView);
            }
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
    }

}
