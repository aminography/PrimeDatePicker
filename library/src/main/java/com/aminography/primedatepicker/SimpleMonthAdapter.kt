package com.aminography.primedatepicker

import android.content.Context
import android.support.annotation.ColorInt

/**
 * An adapter for a list of [SimpleMonthView] items.
 */
class SimpleMonthAdapter(context: Context, controller: DatePickerController, @ColorInt mainColor: Int? = null) : MonthAdapter(context, controller, mainColor) {

    override fun createMonthView(context: Context): MonthView {
        return SimpleMonthView(context, null, controller, mainColor)
    }

}
