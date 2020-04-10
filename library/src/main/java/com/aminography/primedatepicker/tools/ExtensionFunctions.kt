package com.aminography.primedatepicker.tools

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
import android.view.Display
import android.view.WindowManager
import androidx.annotation.StringRes
import com.aminography.primecalendar.PrimeCalendar
import java.util.*

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

@Suppress("DEPRECATION")
fun Context.forceLocaleStrings(locale: Locale, @StringRes vararg resourceIds: Int): List<String> {
    var result: List<String>
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        createConfigurationContext(config).run {
            result = resourceIds.map { getText(it).toString() }
        }
    } else { // support older android versions
        val conf = resources.configuration
        val savedLocale = conf.locale
        conf.locale = locale
        resources.updateConfiguration(conf, null)
        // retrieve resources from desired locale
        result = resourceIds.map { resources.getString(it) }
        // restore original locale
        conf.locale = savedLocale
        resources.updateConfiguration(conf, null)
    }
    return result
}
