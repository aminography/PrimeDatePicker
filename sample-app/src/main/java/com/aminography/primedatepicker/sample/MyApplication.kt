package com.aminography.primedatepicker.sample

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

/**
 * @author aminography
 */
@Suppress("unused")
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
}

const val FONT_PATH_CIVIL = "fonts/Roboto-Regular.ttf"
const val FONT_PATH_PERSIAN = "fonts/IRANSans.ttf"
const val FONT_PATH_HIJRI = "fonts/Amiri-Regular.ttf"
const val FONT_PATH_JAPANESE = "fonts/Roboto-Regular.ttf"
