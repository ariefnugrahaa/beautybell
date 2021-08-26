package com.example.beautybell.service

import android.app.Activity
import android.content.Intent
import androidx.navigation.findNavController
import com.example.beautybell.R
import com.example.beautybell.presentation.MainActivity
import com.example.beautybell.presentation.auth.LoginActivity
import com.example.beautybell.presentation.detail.DetailActivity

class NavigationService {

    fun startDetailArtisanActivity(activity: Activity, id: String) {
        activity.run {
            startActivity(Intent(this, DetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            })
            finish()
        }
    }

    fun startArtisanActivity(activity: Activity) {
        activity.run {
            startActivity(Intent(this, MainActivity::class.java).apply{})
            finish()
        }
    }

    fun startLoginActivity(activity: Activity) {
        activity.run {
            startActivity(Intent(this, LoginActivity::class.java).apply{})
            finish()
        }
    }

    companion object {
        const val EXTRA_ID = "intent.id"
    }
}
