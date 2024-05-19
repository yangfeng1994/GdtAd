package com.mantu.gdt.ad.web;

import android.text.TextUtils;
import android.util.Log;

public class Logger {

    private String mClassSimpleName;

    public static String mLogTag = "YR_QY";
    // true for debug, false for release
    private static boolean mIsLoggable = false;
    private static final int PT_LEVEL = 7;
    private static long OldTime = 0;

    public Logger(Class clazz) {
        mClassSimpleName = clazz.getSimpleName();
    }

    /*
     * 如果logTag为空，默认为"YR_QY"
     */
    public Logger(String classSimpleName, String logTag) {
        mClassSimpleName = classSimpleName;
        if (!TextUtils.isEmpty(logTag)) {
            mLogTag = logTag;
        }
    }

    private void log(String content, int level) {
        String logContent = mClassSimpleName + "::" + content;
        printLog(logContent, level);
    }

    public void printLog(String logContent, int level) {
        if (mIsLoggable) {
            switch (level) {
                case Log.VERBOSE:
                    Log.v(mLogTag, logContent);
                    break;
                case Log.DEBUG:
                    Log.d(mLogTag, logContent);
                    break;
                case Log.INFO:
                    Log.i(mLogTag, logContent);
                    break;
                case Log.WARN:
                    Log.w(mLogTag, logContent);
                    break;
                case Log.ERROR:
                    Log.e(mLogTag, logContent);
                    break;
                case PT_LEVEL:
                    long newTime = System.currentTimeMillis();
                    Log.d(mLogTag, logContent + "spend time is:" + String.valueOf((float) (newTime - OldTime) / 1000));
                    OldTime = newTime;
                    break;
                default:
                    break;
            }
        }
    }

    public void verboseLog(String content) {
        log(content, Log.VERBOSE);
    }

    public void debugLog(String content) {
        log(content, Log.DEBUG);
    }

    public void infoLog(String content) {
        log(content, Log.INFO);
    }

    public void warnLog(String content) {
        log(content, Log.WARN);
    }

    public void errorLog(String content) {
        log(content, Log.ERROR);
    }

    public void ptLog(String content) {
        log(content, PT_LEVEL);
    }

    public void printStackTrace(Throwable e) {
        if (mIsLoggable) {
            e.printStackTrace();
        }
    }

    /**
     * 是否打印log
     *
     * @return
     */
    public boolean isLoggable() {
        return mIsLoggable;
    }

    /**
     * 设置log打印开关
     *
     * @param isLoggable
     */
    public static void setLoggable(boolean isLoggable) {
        mIsLoggable = isLoggable;
    }

    /**
     * 获取logTag
     *
     * @return
     */
    public static String getLogTag() {
        return mLogTag;
    }
}
