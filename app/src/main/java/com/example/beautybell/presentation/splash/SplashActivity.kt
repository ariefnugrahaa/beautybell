package com.example.beautybell.presentation.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.beautybell.R
import com.example.beautybell.service.NavigationService

class SplashActivity : AppCompatActivity() {

    private val navigation by lazy { NavigationService() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initSplash()
    }

    private fun initSplash(){
        val r: Runnable = Runnable { navigation.startLoginActivity(this) }
        Handler().postDelayed(r, 3000)
    }

    override fun onBackPressed() = Unit

}