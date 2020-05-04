package com.aminography.primedatepicker.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.os.Build
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
val PrimeCalendar.monthOffset: Int
    get() = year * 12 + month

/**
 * Checks the orientation of the screen.
 *
 * @return true if the orientation of the screen is landscape, false if the orientation is portrait.
 */
val Context.isDisplayLandscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

/**
 * Returns screen size of the device in pixels.
 *
 * @return screen size of the device as a [Point] object.
 */
val Context.screenSize: Point
    get() = Point().also {
        (getSystemService(Context.WINDOW_SERVICE) as? WindowManager)?.defaultDisplay?.getSize(it)
    }

/**
 * Returns dimension value in pixels.
 *
 * @param dp the input value in dp
 * @return dimension value in pixels
 */
fun Context.dp2px(dp: Float): Int =
    (dp * resources.displayMetrics.density).toInt()

/**
 * Returns a list of strings containing string values in target locale.
 *
 * @param locale the locale that you want to get string values in it
 * @return list of string values
 */
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
