package com.mantu.gdt.ad

import android.app.Activity
import com.mantu.gdt.ad.GdtUtils.TAG
import com.mantu.gdt.ad.utils.RomUtils
import com.miui.zeus.mimo.sdk.InterstitialAd
import com.miui.zeus.mimo.sdk.InterstitialAd.InterstitialAdInteractionListener
import com.miui.zeus.mimo.sdk.InterstitialAd.InterstitialAdLoadListener
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.comm.util.AdError


class InterstitialADHelper {

    private var mExpressInterstitialAD: UnifiedInterstitialAD? = null
    private var mInterstitialAd: InterstitialAd? = null

    fun loadAD(activity: Activity, postId: String, upId: String) {
        if (activity.isFinishing) return
        if (!GdtUtils.isInterstitial) return
        if (RomUtils.checkIsMiuiRom()) {
            "InterstitialADHelper xm loadMiAd".logI(TAG)
            loadMiAd(activity, postId, upId)
        } else {
            "InterstitialADHelper xm loadGdtAD".logI(TAG)
            loadGdtAD(activity, postId)
        }
    }

    fun loadMiAd(activity: Activity, postId: String, upId: String) {
        mInterstitialAd = InterstitialAd()
        mInterstitialAd?.loadAd(upId, object : InterstitialAdLoadListener {
            override fun onAdRequestSuccess() {
                //广告请求成功
            }

            override fun onAdLoadSuccess() {
                //广告加载（缓存）成功，在需要的时候在此处展示广告
                mInterstitialAd?.show(activity, object : InterstitialAdInteractionListener {
                    override fun onAdClick() {
                        // 广告被点击
                        "InterstitialADHelper xm onAdClick".logI(TAG)
                    }

                    override fun onAdShow() {
                        // 广告展示
                        "InterstitialADHelper xm onAdShow".logI(TAG)
                    }

                    override fun onAdClosed() {
                        // 广告关闭
                        "InterstitialADHelper xm onAdClosed".logI(TAG)
                        destroy()
                    }

                    override fun onRenderFail(code: Int, msg: String) {
                        // 广告渲染失败
                        "InterstitialADHelper xm onRenderFail $code $msg".logI(TAG)
                    }

                    override fun onVideoStart() {
                        //视频开始播放
                        "InterstitialADHelper xm onVideoStart".logI(TAG)
                    }

                    override fun onVideoPause() {
                        //视频暂停
                        "InterstitialADHelper xm onVideoPause".logI(TAG)
                    }

                    override fun onVideoResume() {
                        //视频继续播放;
                        "InterstitialADHelper xm onVideoResume".logI(TAG)
                    }

                    override fun onVideoEnd() {
                        //视频播放结束;
                        "InterstitialADHelper xm onVideoEnd".logI(TAG)
                    }
                })
            }

            override fun onAdLoadFailed(errorCode: Int, errorMsg: String) {
                "InterstitialADHelper xm onAdLoadFailed $errorCode $errorMsg ".logI(TAG)
                loadGdtAD(activity, postId)
                // 请求加载失败
            }
        })

    }

    fun loadGdtAD(activity: Activity, postId: String) {
        mExpressInterstitialAD = UnifiedInterstitialAD(activity, postId,
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
                    destroy()
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
                    destroy()
                    "InterstitialADHelper onRenderFail".logI(TAG)
                }
            })
        mExpressInterstitialAD?.loadAD()
    }


    fun destroy() {
        try {
            mExpressInterstitialAD?.destroy()
            mExpressInterstitialAD = null
            mInterstitialAd?.destroy()
            mInterstitialAd = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}