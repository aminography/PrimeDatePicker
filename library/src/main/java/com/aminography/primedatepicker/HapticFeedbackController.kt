package com.aminography.primedatepicker

import android.content.Context
import android.database.ContentObserver
import android.provider.Settings

/**
 * A simple utility class to handle haptic feedback.
 */
class HapticFeedbackController(private val mContext: Context) {

    private val mContentObserver: ContentObserver

    private var mIsGloballyEnabled: Boolean = false

    private fun checkGlobalSetting(context: Context): Boolean {
        return Settings.System.getInt(context.contentResolver, Settings.System.HAPTIC_FEEDBACK_ENABLED, 0) == 1
    }

    init {
        mContentObserver = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                mIsGloballyEnabled = checkGlobalSetting(mContext)
            }
        }
    }

    fun start() {
        // Setup a listener for changes in haptic feedback settings
        mIsGloballyEnabled = checkGlobalSetting(mContext)
        val uri = Settings.System.getUriFor(Settings.System.HAPTIC_FEEDBACK_ENABLED)
        mContext.contentResolver.registerContentObserver(uri, false, mContentObserver)
    }

    fun stop() {
        mContext.contentResolver.unregisterContentObserver(mContentObserver)
    }

}
