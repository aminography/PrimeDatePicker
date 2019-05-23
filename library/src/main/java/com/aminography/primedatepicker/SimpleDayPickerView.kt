package com.aminography.primedatepicker

import android.content.Context
import android.support.annotation.ColorInt
import android.util.AttributeSet

/**
 * A DayPickerView customized for [SimpleMonthAdapter]
 */
class SimpleDayPickerView : DayPickerView {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, controller: DatePickerController, @ColorInt mainColor: Int?) : super(context, controller, mainColor)

    override fun createMonthAdapter(context: Context, controller: DatePickerController): MonthAdapter {
        return SimpleMonthAdapter(context, controller, mainColor)
    }

}
