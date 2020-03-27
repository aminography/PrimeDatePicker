package com.aminography.primedatepicker.tools

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.view.Display
import android.view.WindowManager
import android.widget.EditText
import android.widget.NumberPicker
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

internal fun NumberPicker.setDividerColor(color: Int) {
    val pickerFields = NumberPicker::class.java.declaredFields
    for (pf in pickerFields) {
        if (pf.name == "mSelectionDivider") {
            pf.isAccessible = true
            try {
                val colorDrawable = ColorDrawable(color)
                pf.set(this, colorDrawable)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: Resources.NotFoundException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
            break
        }
    }
}

internal fun NumberPicker.fixInputFilter() {
    val field = NumberPicker::class.java.getDeclaredField("mInputText")
    field.isAccessible = true
    (field.get(this) as EditText).filters = arrayOfNulls(0)
}