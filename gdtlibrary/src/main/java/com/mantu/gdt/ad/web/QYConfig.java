package com.mantu.gdt.ad.web;


import android.webkit.WebSettings;

public class QYConfig {

    //字体大小
    private static WebSettings.TextSize sTextSize = WebSettings.TextSize.NORMAL;
    //是否夜间模式
    private static boolean sIsNightMode = false;

    public static WebSettings.TextSize getTextSize() {
        return sTextSize;
    }

    public static void setTextSize(WebSettings.TextSize sTextSize) {
        QYConfig.sTextSize = sTextSize;
    }

    public static boolean isIsNightMode() {
        return sIsNightMode;
    }

    public static void setIsNightMode(boolean sIsNightMode) {
        QYConfig.sIsNightMode = sIsNightMode;
    }
}
