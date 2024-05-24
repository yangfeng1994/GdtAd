package com.mantu.gdt.ad.web;

import android.view.View;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class QYWebChromeClient extends WebChromeClient {

	private static Logger sLogger = new Logger(QYWebChromeClient.class);

	private QYWebView.OnQYWebViewCustomListener mQYWebViewCustomListener;

    @Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		sLogger.infoLog("onShowCustomView  onShowCustomView");
		if (mQYWebViewCustomListener != null) {
			mQYWebViewCustomListener.onShowCustomView(view, callback);
		}
	}

	@Override
	public void onHideCustomView() {
		super.onHideCustomView();
		sLogger.infoLog("onHideCustomView  onHideCustomView");
		if (mQYWebViewCustomListener != null) {
			mQYWebViewCustomListener.onHideCustomView();
		}
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		if (mQYWebViewCustomListener != null)
			mQYWebViewCustomListener.onProgressChanged(view, newProgress);
		super.onProgressChanged(view, newProgress);
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		sLogger.infoLog("onReceivedTitle:" + title + ":" + view.getUrl());
		super.onReceivedTitle(view, title);
//		QYWebView qyView = (QYWebView) view;
//		if (!TextUtils.isEmpty(qyView.mModeJS)) {
//			qyView.loadUrl(qyView.mModeJS);
//		}
		String url = view.getUrl();
		if (mQYWebViewCustomListener != null)
			mQYWebViewCustomListener.onTitleChanged(view, title, true, url);
	}

	@Override
	public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
		return super.onJsAlert(view, url, message, result);
	}

	public void setQYWebViewCustomListener(QYWebView.OnQYWebViewCustomListener mQYWebViewCustomListener) {
		this.mQYWebViewCustomListener = mQYWebViewCustomListener;
	}
}
