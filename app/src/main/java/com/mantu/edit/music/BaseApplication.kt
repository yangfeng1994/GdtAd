package com.mantu.edit.music

import androidx.multidex.MultiDexApplication
import com.mantu.gdt.ad.DownloadConfirmHelper
import com.mantu.gdt.ad.GdtUtils

class BaseApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        val extraUserData: MutableMap<String, String> = HashMap()
        extraUserData["shakable"] = "0" // 屏蔽开屏摇一摇广告
        GdtUtils.init(this, "", extraUserData)
        GdtUtils.isLoadSplash = true
        GdtUtils.isShowVideo = true
        GdtUtils.isInterstitial = true
        GdtUtils.setPersonalizedState(1)
        DownloadConfirmHelper.USE_CUSTOM_DIALOG = true
    }
}