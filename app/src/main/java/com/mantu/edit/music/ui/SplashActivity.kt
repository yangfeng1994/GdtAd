package com.mantu.edit.music.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mantu.edit.music.MainActivity
import com.mantu.edit.music.R
import com.mantu.edit.music.databinding.ActivitySplashBinding
import com.mantu.gdt.ad.GdtUtils

class SplashActivity : ComponentActivity() {

    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mSplashAD.loadAD("", succeedInvoke = {
            if (binding.mSplashAD.isClick) return@loadAD
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }

    override fun onResume() {
        super.onResume()
        if (binding.mSplashAD.isClick) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}