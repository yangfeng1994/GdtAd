package com.mantu.gdt.ad

import android.app.Application
import android.util.Log
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
        appId: String,
        extraUserData: MutableMap<String, String> = HashMap(),
        callInvoke: (Boolean) -> Unit = {}
    ) {
        GDTAdSdk.initWithoutStart(application, appId)
        GlobalSetting.setExtraUserData(extraUserData)
        GDTAdSdk.start(object : OnStartListener {
            override fun onStartSuccess() {
                callInvoke(true)
            }

            override fun onStartFailed(e: Exception) {
                Log.e("gdt onStartFailed:", e.toString())
                callInvoke(false)
            }
        })
        application?.let { initMi(it) }
    }

    private fun initMi(context: Application) {
        MimoSdk.init(context, object : MimoSdk.InitCallback {
            override fun success() {

            }

            override fun fail(p0: Int, p1: String?) {

            }

        })
        MimoSdk.setDebugOn(BuildConfig.DEBUG)
    }

    //设置渠道号
    fun setChannel(channel: Int) {
        GlobalSetting.setChannel(channel)
    }

    fun setPersonalizedState(state: Int) {
        GlobalSetting.setPersonalizedState(state)
    }


}