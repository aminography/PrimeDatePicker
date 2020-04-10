package com.aminography.primedatepicker.picker.go

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.NumberPicker
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.tools.PersianUtils
import com.aminography.primedatepicker.tools.fixInputFilter
import com.aminography.primedatepicker.tools.setDividerColor
import kotlinx.android.synthetic.main.goto_container.view.*


class GotoView(
    viewStub: ViewStub,
    direction: Direction
) : BaseLazyView(if (direction == Direction.LTR) R.layout.goto_container else R.layout.goto_container_rtl, viewStub) {

    private lateinit var monthNumberPicker: MyNumberPicker
    private lateinit var yearNumberPicker: MyNumberPicker

    var calendar: PrimeCalendar? = null
        set(value) {
            field = value
            value?.let { init(it) }
        }

    var minDateCalendar: PrimeCalendar? = null
    var maxDateCalendar: PrimeCalendar? = null

    var typeface: Typeface? = null
        set(value) {
            field = value
            MyNumberPicker.typeface = value
        }

    var onCloseClickListener: (() -> Unit)? = null

    var onGoClickListener: ((Int, Int) -> Unit)? = null
        set(value) {
            field = value
            rootView.goButton.setOnClickListener {
                val month = monthNumberPicker.value
                val year = yearNumberPicker.value
                value?.invoke(year, month)
            }
        }

    private fun init(calendar: PrimeCalendar) {
        val clone = calendar.clone()

        fun monthName(month: Int): String {
            clone.month = month
            return clone.monthName
        }

        monthNumberPicker = MyNumberPicker(rootView.context)
        rootView.monthFrameLayout.removeAllViews()
        rootView.monthFrameLayout.addView(monthNumberPicker)
        (monthNumberPicker.layoutParams as FrameLayout.LayoutParams).apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        monthNumberPicker.setDividerColor(Color.WHITE)
        monthNumberPicker.fixInputFilter()
        monthNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        monthNumberPicker.minValue = 0
        monthNumberPicker.maxValue = 11
        monthNumberPicker.value = calendar.month
        monthNumberPicker.setFormatter { value -> monthName(value) }

        yearNumberPicker = MyNumberPicker(rootView.context)
        rootView.yearFrameLayout.removeAllViews()
        rootView.yearFrameLayout.addView(yearNumberPicker)
        (yearNumberPicker.layoutParams as FrameLayout.LayoutParams).apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        yearNumberPicker.setDividerColor(Color.WHITE)
        yearNumberPicker.fixInputFilter()
        yearNumberPicker.minValue = minDateCalendar?.year ?: 0
        yearNumberPicker.maxValue = maxDateCalendar?.year ?: 10_000
        yearNumberPicker.wrapSelectorWheel = false
        yearNumberPicker.value = calendar.year
        yearNumberPicker.setFormatter(
            when (calendar) {
                is PersianCalendar -> NumberPicker.Formatter { value -> PersianUtils.convertLatinDigitsToPersian("$value") }
                is HijriCalendar -> NumberPicker.Formatter { value -> PersianUtils.convertLatinDigitsToPersian("$value") }
                else -> NumberPicker.Formatter { value -> "$value" }
            }
        )

        fun adjustMinMaxMonth(year: Int) {
            val minYear = minDateCalendar?.takeIf { it.year == year }?.year
            val maxYear = maxDateCalendar?.takeIf { it.year == year }?.year

            val minMonth = minYear?.let { minDateCalendar?.month } ?: 0
            val maxMonth = maxYear?.let { maxDateCalendar?.month } ?: 11

            monthNumberPicker.minValue = minMonth
            monthNumberPicker.maxValue = maxMonth
            monthNumberPicker.wrapSelectorWheel = (maxMonth - minMonth == 11)
        }

        adjustMinMaxMonth(calendar.year)

        yearNumberPicker.setOnValueChangedListener { _, _, new ->
            adjustMinMaxMonth(new)
        }

        rootView.closeImageView.setOnClickListener {
            onCloseClickListener?.invoke()
        }
    }

}