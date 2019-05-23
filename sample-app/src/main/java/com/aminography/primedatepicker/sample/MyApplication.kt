package com.aminography.primedatepicker.sample

import android.app.Application
import android.support.v7.app.AppCompatDelegate

/**
 * Created by aminography on 7/14/2018.
 */
@Suppress("unused")
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

}