package com.aminography.primedatepicker

import android.content.Context
import android.support.annotation.ColorInt

/**
 * An adapter for a list of [SimpleMonthView] items.
 */
class SimpleMonthListAdapter(context: Context, controller: DatePickerController, @ColorInt mainColor: Int? = null) : BaseMonthListAdapter(context, controller, mainColor) {

    override fun createMonthView(context: Context): BaseMonthView {
        return SimpleMonthView(context, null, controller, mainColor)
    }

}
