package com.mantu.gdt.ad

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.mantu.gdt.ad.databinding.GdtBannerViewBinding
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError

class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), UnifiedBannerADListener {
    var mBannerView: UnifiedBannerView? = null

    init {
        GdtBannerViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun loadAd(activity: Activity, posID: String) {
        if (null != mBannerView) {
            mBannerView?.destroy()
            removeAllViews()
            mBannerView = null
        }
        mBannerView = UnifiedBannerView(activity, posID, this)
        addView(mBannerView)
        mBannerView?.loadAD()
    }

    fun destroy() {
        mBannerView?.destroy()
    }

    override fun onNoAD(p0: AdError?) {
        "BannerView onNoAD ${p0?.errorCode} ${p0?.errorMsg}".logI(GdtUtils.TAG)
    }

    override fun onADReceive() {
        "BannerView onADReceive".logI(GdtUtils.TAG)
        mBannerView?.setDownloadConfirmListener(DownloadConfirmHelper.DOWNLOAD_CONFIRM_LISTENER)
    }

    override fun onADExposure() {
        "BannerView onADExposure".logI(GdtUtils.TAG)
    }

    override fun onADClosed() {
        "BannerView onADClosed".logI(GdtUtils.TAG)
        mBannerView?.setRefresh(0)
    }

    override fun onADClicked() {
        "BannerView onADClicked".logI(GdtUtils.TAG)
    }

    override fun onADLeftApplication() {
        "BannerView onADLeftApplication".logI(GdtUtils.TAG)
    }
}