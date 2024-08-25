package com.mantu.gdt.ad

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.mantu.gdt.ad.GdtUtils.TAG
import com.mantu.gdt.ad.databinding.GdtBannerViewBinding
import com.mantu.gdt.ad.utils.RomUtils
import com.miui.zeus.mimo.sdk.BannerAd
import com.miui.zeus.mimo.sdk.BannerAd.BannerLoadListener
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError


class BannerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs), UnifiedBannerADListener {
    var mBannerView: UnifiedBannerView? = null
    var mMiBannerView: BannerAd? = null
    var closeInvoke = {}
    var posID = ""
    var miID = ""

    init {
        GdtBannerViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    fun loadAD(activity: Activity, posID: String, miAd: String, closeInvoke: () -> Unit = {}) {
        this.closeInvoke = closeInvoke
        this.posID = posID
        this.miID = miAd
        if (RomUtils.checkIsMiuiRom()) {
            "BannerView xm loadMiAD".logI(TAG)
            loadMiAD(miAd, activity)
        } else {
            "BannerView xm loadGdtAd".logI(TAG)
            loadGdtAd(activity, posID)
        }
    }

    fun loadMiAD(miAd: String, activity: Activity) {
        mMiBannerView = BannerAd()
        mMiBannerView?.loadAd(miAd, object : BannerLoadListener {
            //请求成功回调
            override fun onBannerAdLoadSuccess() {
                mMiBannerView?.showAd(
                    activity,
                    this@BannerView,
                    object : BannerAd.BannerInteractionListener {
                        override fun onAdClick() {
                            "BannerView xm onAdClick".logI(GdtUtils.TAG)
                        }

                        override fun onAdShow() {
                            "BannerView xm onAdShow".logI(GdtUtils.TAG)
                        }

                        override fun onAdDismiss() {
                            "BannerView xm onAdDismiss".logI(GdtUtils.TAG)
                            destroy()
                            closeInvoke()
                        }

                        override fun onRenderSuccess() {
                            "BannerView xm onRenderSuccess".logI(GdtUtils.TAG)
                        }

                        override fun onRenderFail(p0: Int, p1: String?) {
                            "BannerView xm onRenderFail ${p0} ${p0}".logI(GdtUtils.TAG)
                        }
                    })
            }

            //请求失败回调
            override fun onAdLoadFailed(errorCode: Int, errorMsg: String) {
                "BannerView xm onAdLoadFailed $errorCode $errorMsg".logI(GdtUtils.TAG)
                destroy()
                loadGdtAd(activity, posID)
            }
        })
    }

    fun loadGdtAd(activity: Activity, posID: String) {
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
        mMiBannerView?.destroy()
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
        closeInvoke()
    }

    override fun onADClicked() {
        "BannerView onADClicked".logI(GdtUtils.TAG)
    }

    override fun onADLeftApplication() {
        "BannerView onADLeftApplication".logI(GdtUtils.TAG)
    }
}