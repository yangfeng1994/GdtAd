package com.mantu.gdt.ad

import android.app.Activity
import com.mantu.gdt.ad.GdtUtils.TAG
import com.mantu.gdt.ad.utils.NetworkUtils
import com.mantu.gdt.ad.utils.RomUtils
import com.miui.zeus.mimo.sdk.RewardVideoAd
import com.miui.zeus.mimo.sdk.RewardVideoAd.RewardVideoInteractionListener
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.comm.util.AdError


class RewordVideoHelper {


    private var mRewardVideoAD: RewardVideoAD? = null
    private var mMiRewardVideoAD: RewardVideoAd? = null

    var isComplete: Boolean = false

    fun loadAD(
        activity: Activity,
        rewordId: String,
        miId: String,
        succeed: (Boolean) -> Unit = {},
        failed: () -> Unit = {}
    ) {
        if (activity.isFinishing) return
        if (!GdtUtils.isShowVideo) {
            succeed(true)
            return
        }
        isComplete = false
        if (RomUtils.checkIsMiuiRom()) {
            "RewordVideoHelper xm loadMiAd".logI(TAG)
            loadMiAd(activity, rewordId, miId, succeed, failed)
        } else {
            "RewordVideoHelper xm loadGDTAD".logI(TAG)
            loadGDTAD(activity, rewordId, succeed, failed)
        }
    }

    fun loadMiAd(
        activity: Activity,
        rewordId: String,
        miId: String,
        succeed: (Boolean) -> Unit = {},
        failed: () -> Unit = {}
    ) {
        mMiRewardVideoAD = RewardVideoAd()
        mMiRewardVideoAD?.loadAd(miId, object : RewardVideoAd.RewardVideoLoadListener {
            override fun onAdRequestSuccess() {
                "RewordVideoHelper xm onAdRequestSuccess".logI(TAG)
            }

            override fun onAdLoadSuccess() {
                mMiRewardVideoAD?.showAd(activity, object : RewardVideoInteractionListener {
                    override fun onAdPresent() {
                        // 广告被曝光
                        "RewordVideoHelper xm onAdPresent".logI(TAG)
                    }

                    override fun onAdClick() {
                        // 广告被点击
                        "RewordVideoHelper xm onAdClick".logI(TAG)
                    }

                    override fun onAdDismissed() {
                        "RewordVideoHelper xm onAdDismissed $isComplete".logI(TAG)
                        destroy()
                        succeed(isComplete)
                        // 广告消失
                    }

                    override fun onAdFailed(message: String) {
                        "RewordVideoHelper xm onAdFailed $message".logI(TAG)
                        isComplete = true
                        // 渲染失败
                    }

                    override fun onVideoStart() {
                        //视频开始播放
                        "RewordVideoHelper xm onVideoStart".logI(TAG)
                    }

                    override fun onVideoPause() {
                        //视频暂停
                        "RewordVideoHelper xm onVideoPause".logI(TAG)
                    }

                    override fun onVideoSkip() {
                        //跳过视频播放
                        "RewordVideoHelper xm onVideoSkip".logI(TAG)
                    }

                    override fun onVideoComplete() {
                        "RewordVideoHelper xm onVideoComplete".logI(TAG)
                        isComplete = true
                        // 视频播放完成
                    }

                    override fun onPicAdEnd() {
                        "RewordVideoHelper xm onPicAdEnd".logI(TAG)
                        isComplete = true
                        //图片类型广告播放完成
                    }

                    override fun onReward() {
                        "RewordVideoHelper xm onReward".logI(TAG)
                        //激励回调
                    }
                })
            }

            override fun onAdLoadFailed(p0: Int, p1: String?) {
                "RewordVideoHelper xm onAdLoadFailed $p0 $p1".logI(TAG)
                destroy()
                loadGDTAD(activity, rewordId, succeed, failed)
            }
        })
    }

    /**
     * 展示视频广告
     */
    fun loadGDTAD(
        activity: Activity,
        rewordId: String,
        succeed: (Boolean) -> Unit = {},
        failed: () -> Unit = {}
    ) {
        mRewardVideoAD = RewardVideoAD(activity, rewordId, object : RewardVideoADListener {
            override fun onADLoad() {
                "RewordVideoHelper onADLoad".logI(TAG)
                if (DownloadConfirmHelper.USE_CUSTOM_DIALOG) {
                    "注册二次确认下载弹窗 RewordVideoHelper".logI(TAG)
                    mRewardVideoAD?.setDownloadConfirmListener(
                        DownloadConfirmHelper.DOWNLOAD_CONFIRM_LISTENER
                    )
                }
                if (mRewardVideoAD?.isValid == true) {
                    mRewardVideoAD?.showAD(activity)
                } else {
                    succeed(true)
                }
            }

            override fun onVideoCached() {
                "RewordVideoHelper onVideoCached".logI(TAG)
            }

            override fun onADShow() {
                "RewordVideoHelper onADShow".logI(TAG)
            }

            override fun onADExpose() {
                "RewordVideoHelper onADExpose".logI(TAG)
            }

            override fun onReward(p0: MutableMap<String, Any>?) {
                isComplete = true
                "RewordVideoHelper onReward $isComplete".logI(TAG)
            }

            override fun onADClick() {
                "RewordVideoHelper onADClick".logI(TAG)
            }

            override fun onVideoComplete() {
                "RewordVideoHelper onVideoComplete".logI(TAG)
            }

            override fun onADClose() {
                "RewordVideoHelper onADClose $isComplete".logI(TAG)
                destroy()
                succeed(isComplete)
            }

            override fun onError(p0: AdError?) {
                "RewordVideoHelper onNoAD ${p0?.errorCode}  ${p0?.errorMsg}".logI(TAG)
                if (null == GdtUtils.application || NetworkUtils.isConnected()) {
                    isComplete = true
                    succeed(isComplete)
                } else {
                    failed()
                }
            }
        }, true)
        mRewardVideoAD?.loadAD()
    }

    fun destroy() {
        mMiRewardVideoAD?.destroy()
        mMiRewardVideoAD = null
    }

}