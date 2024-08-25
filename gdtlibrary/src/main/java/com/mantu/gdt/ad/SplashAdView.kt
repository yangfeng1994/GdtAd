package com.mantu.gdt.ad

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.mantu.gdt.ad.GdtUtils.TAG
import com.mantu.gdt.ad.databinding.SplashAdViewBinding
import com.mantu.gdt.ad.utils.RomUtils
import com.miui.zeus.mimo.sdk.SplashAd
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.util.AdError


class SplashADView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var splashAD: SplashAD? = null
    private var mMiSplashAD: SplashAd? = null
    var isClick = false
    var binding: SplashAdViewBinding

    init {
        binding = SplashAdViewBinding.inflate(LayoutInflater.from(context), this, true)
        GdtUtils.splashBackground?.run {
            binding.ivBg.setImageResource(this)
        }
    }

    fun loadAD(
        postId: String,
        miId: String,
        fetchDelay: Int = 4000,
        succeedInvoke: (Boolean) -> Unit
    ) {
        if (!GdtUtils.isLoadSplash) {
            succeedInvoke(false)
            return
        }
        if (RomUtils.checkIsMiuiRom()) {
            "SplashADView xm loadMiAd".logI(TAG)
            loadMiAd(postId, miId, fetchDelay, succeedInvoke)
        } else {
            "SplashADView xm loadGdtAD".logI(TAG)
            loadGdtAD(postId, fetchDelay, succeedInvoke)
        }
    }

    fun loadMiAd(
        postId: String,
        miId: String,
        fetchDelay: Int = 4000,
        succeedInvoke: (Boolean) -> Unit
    ) {
        mMiSplashAD = SplashAd()
        mMiSplashAD?.loadAndShow(binding.mSplashContainer, miId, object :
            SplashAd.SplashAdListener {
            override fun onAdLoaded() {
                "SplashADView xm onAdLoaded".logI(TAG)
            }

            override fun onAdLoadFailed(p0: Int, p1: String?) {
                "SplashADView xm onAdLoadFailed $p0 $p1".logI(TAG)
                destroy()
                loadGdtAD(postId, fetchDelay, succeedInvoke)
            }

            override fun onAdShow() {
                "SplashADView xm onAdShow".logI(TAG)
            }

            override fun onAdClick() {
                "SplashADView xm onAdClick".logI(TAG)
                isClick = true
            }

            override fun onAdDismissed() {
                "SplashADView xm onAdDismissed".logI(TAG)
                destroy()
                succeedInvoke(true)
            }

            override fun onAdRenderFailed() {
                "SplashADView xm onAdRenderFailed".logI(TAG)
            }
        })
    }


    /**
     *
     */
    open fun loadGdtAD(
        postId: String,
        fetchDelay: Int = 4000,
        succeedInvoke: (Boolean) -> Unit
    ) {
        splashAD = SplashAD(context, postId, object : SplashADListener {
            override fun onADDismissed() {
                "SplashADView onADDismissed".logI(TAG)
                succeedInvoke(true)
            }

            override fun onNoAD(p0: AdError?) {
                "SplashADView onNoAD ${p0?.errorCode}  ${p0?.errorMsg}".logI(TAG)
                succeedInvoke(false)
            }

            override fun onADPresent() {
                "SplashADView onADPresent".logI(TAG)
            }

            override fun onADClicked() {
                "SplashADView onADClicked".logI(TAG)
                isClick = true
            }

            override fun onADTick(p0: Long) {
                "SplashADView onADTick $p0".logI(TAG)
            }

            override fun onADExposure() {
                "SplashADView onADExposure".logI(TAG)
            }

            override fun onADLoaded(p0: Long) {
                "SplashADView onADLoaded  $p0".logI(TAG)
                if (DownloadConfirmHelper.USE_CUSTOM_DIALOG) {
                    "注册二次确认下载弹窗 SplashADView".logI(TAG)
                    splashAD?.setDownloadConfirmListener(DownloadConfirmHelper.DOWNLOAD_CONFIRM_LISTENER)
                }
                splashAD?.showFullScreenAd(binding.mSplashContainer)
            }
        }, fetchDelay)
        splashAD?.fetchFullScreenAdOnly()
    }

    fun destroy() {
        mMiSplashAD?.destroy()
    }

}