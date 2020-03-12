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

/**
 * Calculates month offset of the current calendar.
 *
 * @return month offset of the current calendar.
 */
fun PrimeCalendar.monthOffset(): Int =
        year * 12 + month

/**
 * Checks the orientation of the screen.
 *
 * @return true if the orientation of the screen is landscape, false if the orientation is portrait.
 */
fun Context.isDisplayLandscape(): Boolean =
        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

/**
 * Returns screen size of the device in pixels.
 *
 * @return screen size of the device as a [Point] object.
 */
fun Context.screenSize(): Point {
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val display: Display? = windowManager.defaultDisplay
    val point = Point()
    display?.getSize(point)
    return point
}

/**
 * Returns dimension value in pixels.
 *
 * @param dp the input value in dp
 * @return dimension value in pixels
 */
fun Context.dp2px(dp: Float): Int =
        (dp * resources.displayMetrics.density).toInt()