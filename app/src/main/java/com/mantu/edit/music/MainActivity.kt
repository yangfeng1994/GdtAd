package com.mantu.edit.music

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.mantu.edit.music.databinding.ActivityMainBinding
import com.mantu.gdt.ad.InterstitialADHelper
import com.mantu.gdt.ad.RewordVideoHelper

class MainActivity : ComponentActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mInterstitial.setOnClickListener {
            val interstitialADHelper = InterstitialADHelper()
            interstitialADHelper.loadAD(this, "")
        }
        binding.mRewordVideo.setOnClickListener {
            val rewordVideoHelper = RewordVideoHelper()
            rewordVideoHelper.loadAD(this, "", {}, {})
        }
    }
}
