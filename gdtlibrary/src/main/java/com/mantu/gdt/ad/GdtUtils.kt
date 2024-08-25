package com.mantu.gdt.ad

import android.app.Application
import android.util.Log
import com.mantu.gdt.ad.utils.RomUtils
import com.miui.zeus.mimo.sdk.BuildConfig
import com.miui.zeus.mimo.sdk.MimoSdk
import com.qq.e.comm.managers.GDTAdSdk
import com.qq.e.comm.managers.GDTAdSdk.OnStartListener
import com.qq.e.comm.managers.setting.GlobalSetting


object GdtUtils {

    val TAG = "GdtAD"

    //是否加载开屏广告
    var isLoadSplash = false

    //是否展示激励视频广告
    var isShowVideo = false

    //是否展示差屏广告
    var isInterstitial = false

    var application: Application? = null

    /**
     * 设置开屏的默认加载图片
     */
    var splashBackground: Int? = null

    //初始化
    fun init(
        context: Application,
        appId: String,
        extraUserData: MutableMap<String, String> = HashMap(),
        callInvoke: (Boolean) -> Unit = {}
    ) {
        this.application = context
        if (RomUtils.checkIsMiuiRom()) {
            initMi(context, appId, extraUserData, callInvoke)
        } else {
            initGdt(context, appId, extraUserData, callInvoke)
        }
    }

    private fun initMi(
        context: Application,
        appId: String,
        extraUserData: MutableMap<String, String> = HashMap(),
        callInvoke: (Boolean) -> Unit = {}
    ) {
        MimoSdk.init(context, object : MimoSdk.InitCallback {
            override fun success() {
                "GdtUtils xm success".logI(TAG)
                GDTAdSdk.initWithoutStart(context, appId)
                GlobalSetting.setExtraUserData(extraUserData)
                GDTAdSdk.start(object : OnStartListener {
                    override fun onStartSuccess() {
                        "GdtUtils xm Success gdt Success".logI(TAG)
                        callInvoke(true)
                    }

                    override fun onStartFailed(e: Exception) {
                        "GdtUtils xm Success gdt Failed ${e}".logI(TAG)
                        callInvoke(true)
                    }
                })
            }

            override fun fail(p0: Int, p1: String?) {
                GDTAdSdk.initWithoutStart(context, appId)
                GlobalSetting.setExtraUserData(extraUserData)
                GDTAdSdk.start(object : OnStartListener {
                    override fun onStartSuccess() {
                        "GdtUtils xm Failed $p0 $p1 gdt Success".logI(TAG)
                        callInvoke(true)
                    }

                    override fun onStartFailed(e: Exception) {
                        "GdtUtils xm Failed gdt Failed ${e}".logI(TAG)
                        Log.e("gdt onStartFailed:", e.toString())
                        callInvoke(false)
                    }
                })
            }
        })
        MimoSdk.setDebugOn(BuildConfig.DEBUG)
    }

    private fun initGdt(
        context: Application,
        appId: String,
        extraUserData: MutableMap<String, String> = HashMap(),
        callInvoke: (Boolean) -> Unit = {}
    ) {
        GDTAdSdk.initWithoutStart(context, appId)
        GlobalSetting.setExtraUserData(extraUserData)
        GDTAdSdk.start(object : OnStartListener {
            override fun onStartSuccess() {
                "GdtUtils gdt Success".logI(TAG)
                callInvoke(true)
            }

            override fun onStartFailed(e: Exception) {
                "GdtUtils gdt Success ${e}".logI(TAG)
                callInvoke(false)
            }
        })
    }

    //设置渠道号
    fun setChannel(channel: Int) {
        GlobalSetting.setChannel(channel)
    }

    fun setPersonalizedState(state: Int) {
        GlobalSetting.setPersonalizedState(state)
    }


}