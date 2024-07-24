package com.mantu.gdt.ad

import android.app.Activity
import com.mantu.gdt.ad.GdtUtils.TAG
import com.mantu.gdt.ad.utils.NetworkUtils
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.comm.util.AdError


class RewordVideoHelper {


    private var mRewardVideoAD: RewardVideoAD? = null

    var isComplete: Boolean = false

    /**
     * 展示视频广告
     */
    fun loadAD(
        activity: Activity,
        rewordId: String,
        succeed: (Boolean) -> Unit = {},
        failed: () -> Unit = {}
    ) {
        isComplete = false
        if (activity.isFinishing) return
        if (!GdtUtils.isShowVideo) {
            succeed(true)
            return
        }
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

}