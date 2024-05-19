package com.mantu.gdt.ad

import android.app.Activity
import com.mantu.gdt.ad.GdtUtils.TAG
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.comm.util.AdError
import java.lang.Exception


class InterstitialADHelper {

    private var mExpressInterstitialAD: UnifiedInterstitialAD? = null

    fun loadAD(
        activity: Activity,
        postId: String
    ) {
        if (activity.isFinishing) return
        if (!GdtUtils.isInterstitial) return
        mExpressInterstitialAD =
            UnifiedInterstitialAD(activity, postId,
                object : UnifiedInterstitialADListener {

                    override fun onADReceive() {
                        "InterstitialADHelper onADReceive".logI(TAG)
                    }

                    override fun onVideoCached() {
                        "InterstitialADHelper onVideoCached".logI(TAG)
                    }

                    override fun onNoAD(p0: AdError?) {
                        "InterstitialADHelper onNoAD ${p0?.errorCode}  ${p0?.errorMsg}".logI(TAG)
                    }

                    override fun onADOpened() {
                        "InterstitialADHelper onADOpened".logI(TAG)
                    }

                    override fun onADExposure() {
                        "InterstitialADHelper onADExposure".logI(TAG)
                    }

                    override fun onADClicked() {
                        "InterstitialADHelper onADClicked".logI(TAG)
                    }

                    override fun onADLeftApplication() {
                        "InterstitialADHelper onADLeftApplication".logI(TAG)
                    }

                    override fun onADClosed() {
                        "InterstitialADHelper onADClosed".logI(TAG)
                    }

                    override fun onRenderSuccess() {
                        "InterstitialADHelper onRenderSuccess".logI(TAG)
                        if (DownloadConfirmHelper.USE_CUSTOM_DIALOG) {
                            "注册二次确认下载弹窗 InterstitialADHelper".logI(TAG)
                            mExpressInterstitialAD?.setDownloadConfirmListener(DownloadConfirmHelper.DOWNLOAD_CONFIRM_LISTENER)
                        }
                        mExpressInterstitialAD?.show()
                    }

                    override fun onRenderFail() {
                        "InterstitialADHelper onRenderFail".logI(TAG)
                    }
                })
        mExpressInterstitialAD?.loadAD()
    }


    fun destroy() {
        try {
            mExpressInterstitialAD?.destroy()
            mExpressInterstitialAD = null
        } catch (e: Exception) {
        }
    }
}