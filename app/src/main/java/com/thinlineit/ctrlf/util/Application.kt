package com.thinlineit.ctrlf.util

import android.app.Application

class Application : Application() {

    override fun onCreate() {
        preferenceUtil = PreferenceUtil(applicationContext)
        super.onCreate()
    }

    companion object {
        lateinit var preferenceUtil: PreferenceUtil
    }
}
