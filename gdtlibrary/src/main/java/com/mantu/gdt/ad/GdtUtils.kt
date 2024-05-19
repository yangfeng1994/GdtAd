package com.mantu.gdt.ad

import android.app.Application
import android.content.Context
import android.util.Log
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
        GDTAdSdk.initWithoutStart(context, appId)
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
    }

    //设置渠道号
    fun setChannel(channel: Int) {
        GlobalSetting.setChannel(channel)
    }

    fun setPersonalizedState(state: Int) {
        GlobalSetting.setPersonalizedState(state)
    }


}