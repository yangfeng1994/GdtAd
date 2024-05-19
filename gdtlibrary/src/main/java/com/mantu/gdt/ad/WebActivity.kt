package com.mantu.gdt.ad

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.ComponentActivity
import com.mantu.gdt.ad.databinding.ActivityWebBinding
import com.mantu.gdt.ad.web.QYWebView

inline fun QYWebView.setOnQYWebViewCustomListener(
    crossinline onPageStartLoad: (webView: WebView?, url: String?, favicon: Bitmap?) -> Unit = { view, url, favicon -> },
    crossinline onTitleChanged: (
        view: WebView?,
        title: String?,
        isRealTitle: Boolean,
        url: String?
    ) -> Unit = { view, title, isRealTitle, url -> },

    crossinline onProgressChanged: (view: WebView?, newProgress: Int) -> Unit = { view, newProgress -> },
    crossinline onPageEndLoad: (webView: WebView?, url: String?) -> Unit = { view, url -> },
    crossinline shouldOverrideUrlLoading: (view: WebView?, url: String?) -> Boolean = { view, url -> false }
): QYWebView.OnQYWebViewCustomListener {
    val listener = object : QYWebView.OnQYWebViewCustomListener {
        override fun onPageStartLoad(webView: WebView?, url: String?, favicon: Bitmap?) {
            onPageStartLoad(webView, url, favicon)
        }

        override fun onPageEndLoad(webView: WebView?, url: String?) {
            onPageEndLoad(webView, url)
        }

        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            onProgressChanged(view, newProgress)
        }

        override fun onScroll(dx: Int, dy: Int, oldx: Int, oldy: Int) {

        }

        override fun onTitleChanged(
            view: WebView?,
            title: String?,
            isRealTitle: Boolean,
            url: String?
        ) {
            onTitleChanged(view, title, isRealTitle, url)
        }

        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            return shouldOverrideUrlLoading(view, url)
        }

        override fun onShowCustomView(view: View?, callback: WebChromeClient.CustomViewCallback?) {

        }

        override fun onHideCustomView() {

        }

        override fun onJsMessageListener(msg: String?) {

        }

    }
    setOnQYWebViewCustomListener(listener)
    return listener
}

class WebActivity : ComponentActivity() {
    val url by lazy { intent?.getStringExtra("url") ?: "" }
    val title by lazy { intent?.getStringExtra("title") ?: "" }
    lateinit var binding: ActivityWebBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        initViews()
    }

    fun initViews() {
        binding.mWebView.loadUrl(url)
        binding.tvTitle.setText(title)
        binding.mWebView.setOnQYWebViewCustomListener(
            onPageStartLoad = { webView, url, favicon ->
                binding.pbSchedule.visibility = View.VISIBLE
            },
            onTitleChanged = { view, title, isRealTitle, url ->

            },
            onProgressChanged = { view, newProgress ->
                binding.pbSchedule.progress = newProgress
            }, onPageEndLoad = { webView, url ->
                binding.pbSchedule.visibility = View.GONE
            }
        )
    }

    override fun onBackPressed() {
        if (binding.mWebView.canGoBack()) {
            binding.mWebView.goBack()
            return
        }
        binding.mWebView.close()
        super.onBackPressed()
    }


}
