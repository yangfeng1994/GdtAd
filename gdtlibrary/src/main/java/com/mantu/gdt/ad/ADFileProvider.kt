package com.mantu.gdt.ad

import android.app.Application
import androidx.core.content.FileProvider

class ADFileProvider : FileProvider() {

    override fun onCreate(): Boolean {
        context?.let {
            GdtUtils.application = it.applicationContext as Application
        }
        return super.onCreate()
    }
}