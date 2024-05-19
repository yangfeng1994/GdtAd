package com.mantu.gdt.ad

import android.util.Log

val isDebug = true

const val M_TAG = "LOG_TAG"

inline fun String.logE(TAG: String = M_TAG) {
    if (isDebug) {
        Log.e(TAG, this)
    }
}


inline fun String.logI(TAG: String = M_TAG) {
    Log.i(TAG, this)
}


inline fun String.logI1(TAG: String = M_TAG) {
    Log.i(TAG, this)
}

inline fun String.logD(TAG: String = M_TAG) {
    if (isDebug) {
        Log.d(TAG, this)
    }
}


inline fun String.logW(TAG: String = M_TAG) {
    if (isDebug) {
        Log.w(TAG, this)
    }
}


inline fun String.logV(TAG: String = M_TAG) {
    if (isDebug) {
        Log.v(TAG, this)
    }
}