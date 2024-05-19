package com.mantu.gdt.ad.web;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.webkit.HttpAuthHandler;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.mantu.gdt.ad.GdtUtils;

import java.util.ArrayList;
import java.util.List;

public class QYWebViewClient extends WebViewClient {

    private static Logger sLogger = new Logger(QYWebViewClient.class);

    private QYWebView.OnQYWebViewCustomListener mQYWebViewCustomListener;

    private static List<String> sScheme;

    static {
        sScheme = new ArrayList();
        sScheme.add("weixin");
        sScheme.add("alipays");
        sScheme.add("intent");
        sScheme.add("ctrip");
        sScheme.add("mqqapi");
        sScheme.add("tuniuapp");
        sScheme.add("jianshu");
        sScheme.add("bilibili");
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        sLogger.infoLog("shouldOverrideUrlLoading");
        try {
            Uri uri = Uri.parse(url);
            if (uri != null) {
                String scheme = uri.getScheme();
                if (sScheme.contains(scheme)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    GdtUtils.INSTANCE.getApplication().startActivity(intent);
                    return true;
                }
            }
            if (mQYWebViewCustomListener != null)
                return mQYWebViewCustomListener.shouldOverrideUrlLoading(view, url);
        } catch (Exception e) {
            sLogger.printStackTrace(e);
            return true;
        }
        view.loadUrl(url);
        return false;
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
        WebResourceResponse response = null;
//        Log.e("yyyyyy", "shouldInterceptRequest    " + url);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            response = super.shouldInterceptRequest(view, url);
            if (response == null)
                return null;
            sLogger.infoLog(response.toString());
        }
        return response;
    }

    @Override
    public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        sLogger.infoLog("onPageFinished url:" + url);
//        Log.e("yyyyyy", "onPageFinished    " + url);
        if (mQYWebViewCustomListener != null)
            mQYWebViewCustomListener.onPageEndLoad(view, url);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        sLogger.errorLog("onPageStarted");
        if (mQYWebViewCustomListener != null)
            mQYWebViewCustomListener.onPageStartLoad(view, url, favicon);
        super.onPageStarted(view, url, favicon);
    }

    public void setQYWebViewCustomListener(QYWebView.OnQYWebViewCustomListener QYWebViewCustomListener) {
        this.mQYWebViewCustomListener = QYWebViewCustomListener;
    }
}
