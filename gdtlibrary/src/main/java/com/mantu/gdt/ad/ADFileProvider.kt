package com.mantu.gdt.ad

import androidx.core.content.FileProvider
import com.mantu.gdt.ad.GdtUtils.TAG

class ADFileProvider : FileProvider() {

    override fun onCreate(): Boolean {
        "ADFileProvider xm onCreate ${context}".logI(TAG)
        return super.onCreate()
    }
}