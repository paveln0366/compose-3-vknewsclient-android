package com.example.vknewsclient

import android.app.Application
import com.vk.id.VKID

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        VKID.init(this)
    }
}