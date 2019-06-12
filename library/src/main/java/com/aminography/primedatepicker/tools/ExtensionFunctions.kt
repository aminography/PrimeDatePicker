package com.aminography.primedatepicker.tools

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.view.Display
import android.view.WindowManager
import com.aminography.primecalendar.PrimeCalendar

/**
 * @author aminography
 */

fun PrimeCalendar.monthOffset(): Int =
        year * 12 + month

fun Context.isDisplayLandscape(): Boolean = (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)

fun Context.screenSize(): Point {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display: Display? = windowManager.defaultDisplay
    val point = Point()
    display?.getSize(point)
    return point
}