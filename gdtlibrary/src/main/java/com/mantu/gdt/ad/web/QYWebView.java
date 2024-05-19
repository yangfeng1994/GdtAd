package com.mantu.gdt.ad.web;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Map;

public class QYWebView extends WebView {

	private static Logger sLogger = new Logger(QYWebView.class);

	public QYWebViewClient mCustomWebViewClient;
	public QYWebChromeClient mQYWebChromeClient;
	public DirectInterface mDirectInterface;
	private OnQYWebViewCustomListener mQYWebViewCustomListener;
	private Map<String, String> mExtraHeaders;
	private float mXSwipeDex;
	private float mYSwipeDex;
	private int mCurrentMode;
	private GestureDetector mGestureDetector;
	private boolean mCanGesture = true;
	private Direction mDirect = Direction.NONE;
	private View mCurrentFocus;

	public boolean mIsRemoveQQAd;
	private boolean showTitle;

	public Handler mHandler = new Handler(Looper.getMainLooper()) {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			sLogger.infoLog("handleMessage what :" + msg.what);
			switch (msg.what) {
			case 0:
				if (mIsRemoveQQAd) {
					return;
				}
				if (mCurrentFocus != null && mCurrentFocus instanceof ViewGroup) {
					ViewGroup viewGroup = (ViewGroup) mCurrentFocus;
					traverseTextView(viewGroup);
				}
				break;
			case 1:
				if (mCurrentFocus != null && mCurrentFocus instanceof ViewGroup) {
					ViewGroup viewGroup = (ViewGroup) mCurrentFocus;
					traverseTextView(viewGroup);
				}
				mHandler.sendEmptyMessageDelayed(1, 500);
				break;
			}
		}
	};

	public interface OnQYWebViewCustomListener {

		void onPageStartLoad(WebView webView, String url, Bitmap favicon);

		void onPageEndLoad(WebView webView, String url);

		void onProgressChanged(WebView view, int newProgress);

		void onScroll(int dx, int dy, int oldx, int oldy);

		void onTitleChanged(WebView view, String title, boolean isRealTitle, String url);

		boolean shouldOverrideUrlLoading(WebView view, String url);

		void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback);

		void onHideCustomView();

		void onJsMessageListener(String msg);
	}

	public interface DirectInterface {
		void onDirect(float direction);

		void onUp(Direction mDirect);

		void onTouchDown();

		boolean onEdge();
	}

	public QYWebView(Context context) {
		super(context, null);
		init(context);
	}

	public QYWebView(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.webViewStyle);
	}

	public QYWebView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// 修复 WebView 初始化时会修改 Activity 语种配置的问题
		init(context);
	}

	public void onStart() {
		if (mCurrentMode == 2) {
			mHandler.removeMessages(1);
			mHandler.sendEmptyMessageDelayed(1, 500);
		}
	}

	public void onStop() {
		if (mCurrentMode == 2) {
			mHandler.removeMessages(1);
		}
	}

	public void close() {
		this.destroy();
	}


	public void refresh(String url) {
		loadUrl(url);
	}

	@Override
	public void loadUrl(String url) {
		super.loadUrl(url, mExtraHeaders);
	}

	public void loadSettings(Context context) {

		mExtraHeaders = new HashMap();
		WebSettings settings = getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setLoadsImagesAutomatically(true);
		settings.setUseWideViewPort(true);
		setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		setHorizontalScrollBarEnabled(false);
		setHorizontalScrollbarOverlay(false);
		settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		settings.setLoadWithOverviewMode(false);
		settings.setGeolocationEnabled(true);
		settings.setSaveFormData(true);
		settings.setSavePassword(true);
		CookieManager.getInstance().setAcceptCookie(true);
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);
		settings.setSupportMultipleWindows(false);
		settings.setEnableSmoothTransition(true);
		// HTML5 API flags
		// 设置数据库缓存路径
		// 开启 Application Caches 功能
//		settings.setAppCacheEnabled(true);
		settings.setDatabaseEnabled(true);
		settings.setDomStorageEnabled(true);
		settings.setTextSize(QYConfig.getTextSize());
		// HTML5 configuration settings.
//		settings.setAppCacheMaxSize(10 * 1024 * 1024);
		settings.setJavaScriptCanOpenWindowsAutomatically(true);
	}


	@Override
	protected void onScrollChanged(final int dx, final int dy, final int oldx, final int oldy) {
		super.onScrollChanged(dx, dy, oldx, oldy);
		if (mQYWebViewCustomListener != null) {
			mQYWebViewCustomListener.onScroll(dx, dy, oldx, oldy);
		}
	}

	public void setOnQYWebViewCustomListener(OnQYWebViewCustomListener QYWebViewCustomListener) {
		this.mQYWebViewCustomListener = QYWebViewCustomListener;
		mCustomWebViewClient.setQYWebViewCustomListener(QYWebViewCustomListener);
		mQYWebChromeClient.setQYWebViewCustomListener(QYWebViewCustomListener);
	}

	public void init(Context context) {
		mGestureDetector = new GestureDetector(getContext(), new SwipeGestureDetector());
		loadSettings(context);
		mQYWebChromeClient = new QYWebChromeClient();
		mCustomWebViewClient = new QYWebViewClient();
		setWebChromeClient(mQYWebChromeClient);
		setWebViewClient(mCustomWebViewClient);
	}

	public void setFullPlay() {
		Bundle data = new Bundle();
		data.putInt("DefaultVideoScreen", 2);
	}

	public void setWebPlay() {
		Bundle data = new Bundle();
		data.putInt("DefaultVideoScreen", 1);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		sLogger.infoLog("onAttachedToWindow");
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		sLogger.infoLog("onDetachedFromWindow");
		mIsRemoveQQAd = true;
		mHandler.removeMessages(0);
		mHandler.removeMessages(1);
	}

	public void hintQQAd(final Activity activity, int mode) {
		mCurrentMode = mode;
		if (mode == 2) {
			mCurrentFocus = activity.getCurrentFocus();
			mHandler.removeMessages(1);
			mHandler.sendEmptyMessageDelayed(1, 500);
		} else {
			mHandler.removeMessages(1);
		}
	}

	private void traverseTextView(final ViewGroup viewGroup) {
		if (viewGroup == null) {
			return;
		}
		int count = viewGroup.getChildCount();
		for (int i = 0; i < count; i++) {
			final View view = viewGroup.getChildAt(i);
			if (view != null && view instanceof TextView) {
				final TextView textView = (TextView) view;
				if (textView.getText().toString().contains("QQ浏览器")) {
					if (textView.getParent() != null && textView.getParent().getParent() != null) {
						((View) textView.getParent().getParent()).setVisibility(View.GONE);
					}
					mIsRemoveQQAd = true;
					return;
				}

			} else if (view instanceof ViewGroup) {
				this.traverseTextView((ViewGroup) view);
			}
		}
	}

	public void setDirectInterface(DirectInterface direct) {
		mDirectInterface = direct;
	}

	class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			mXSwipeDex += distanceX;
			mYSwipeDex += distanceY;
			if (Math.abs(mXSwipeDex) > 32 && Math.abs(mXSwipeDex) > Math.abs(mYSwipeDex)) {
				if (mXSwipeDex > 0) {
					mDirect = Direction.LEFT;
				} else if (mXSwipeDex < 0) {
					mDirect = Direction.RIGHT;
				}
				if (mDirectInterface != null)
					mDirectInterface.onDirect(mXSwipeDex);
			} else if (Math.abs(mYSwipeDex) > 32) {
				mCanGesture = false;
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}

	public boolean isShowTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
		case MotionEvent.ACTION_UP:
			if (mDirectInterface != null)
				mDirectInterface.onUp(mDirect);
			break;
		}
		if (mCanGesture)
			mGestureDetector.onTouchEvent(event);
		if (mDirectInterface != null) {
			boolean edge = mDirectInterface.onEdge();
			if (edge)
				return true;
			else
				return super.onTouchEvent(event);
		}
		return super.onTouchEvent(event);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			mCanGesture = true;
			mXSwipeDex = mYSwipeDex = 0;
			mDirect = Direction.NONE;
			if (mDirectInterface != null) {
				mDirectInterface.onTouchDown();
				return mDirectInterface.onEdge();
			}
		}
		return super.onInterceptTouchEvent(ev);
	}
}