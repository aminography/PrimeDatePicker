package com.aminography.primedatepicker.picker.go

import android.graphics.Typeface
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.FrameLayout
import android.widget.NumberPicker
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.localizeDigits
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.common.Direction
import com.aminography.primedatepicker.picker.base.BaseLazyView
import com.aminography.primedatepicker.picker.component.ColoredNumberPicker
import kotlinx.android.synthetic.main.goto_container.view.*

/**
 * @author aminography
 */
internal class GotoView(
    viewStub: ViewStub,
    direction: Direction
) : BaseLazyView(if (direction == Direction.LTR) R.layout.goto_container else R.layout.goto_container_rtl, viewStub) {

    private lateinit var monthNumberPicker: ColoredNumberPicker
    private lateinit var yearNumberPicker: ColoredNumberPicker

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
            ColoredNumberPicker.typeface = value
        }

    var onCloseClickListener: (() -> Unit)? = null

    var onGoClickListener: ((Int, Int) -> Unit)? = null
        set(value) {
            field = value
            rootView.goButtonImageView.setOnClickListener {
                val month = monthNumberPicker.value
                val year = yearNumberPicker.value
                value?.invoke(year, month)
            }
        }

    private fun init(calendar: PrimeCalendar) {
        val clone = calendar.clone()
        clone.locale

        fun monthName(month: Int): String {
            clone.month = month
            return clone.monthName
        }

        monthNumberPicker = ColoredNumberPicker(rootView.context)
        rootView.monthFrameLayout.removeAllViews()
        rootView.monthFrameLayout.addView(monthNumberPicker)
        (monthNumberPicker.layoutParams as FrameLayout.LayoutParams).apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        monthNumberPicker.applyDividerColor()
        monthNumberPicker.fixInputFilter()
        monthNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        monthNumberPicker.minValue = 0
        monthNumberPicker.maxValue = 11
        monthNumberPicker.value = calendar.month
        monthNumberPicker.setFormatter { value -> monthName(value) }

        yearNumberPicker = ColoredNumberPicker(rootView.context)
        rootView.yearFrameLayout.removeAllViews()
        rootView.yearFrameLayout.addView(yearNumberPicker)
        (yearNumberPicker.layoutParams as FrameLayout.LayoutParams).apply {
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        yearNumberPicker.applyDividerColor()
        yearNumberPicker.fixInputFilter()
        yearNumberPicker.minValue = minDateCalendar?.year ?: 1
        yearNumberPicker.maxValue = maxDateCalendar?.year ?: 10_000
        yearNumberPicker.wrapSelectorWheel = false
        yearNumberPicker.value = calendar.year
        yearNumberPicker.setFormatter { value -> value.localizeDigits(calendar.locale) }

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

        rootView.closeButtonImageView.setOnClickListener {
            onCloseClickListener?.invoke()
        }
    }

}