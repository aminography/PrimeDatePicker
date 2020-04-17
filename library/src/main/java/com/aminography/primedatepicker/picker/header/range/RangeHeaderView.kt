package com.aminography.primedatepicker.picker.header.range

import android.graphics.Typeface
import android.view.ViewStub
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.Direction
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.picker.header.HeaderView
import com.aminography.primedatepicker.tools.forceLocaleStrings
import kotlinx.android.synthetic.main.range_days_header.view.*
import java.util.*

/**
 * @author aminography
 */
class RangeHeaderView(
    viewStub: ViewStub,
    direction: Direction
) : BaseLazyView(if (direction == Direction.LTR) R.layout.range_days_header else R.layout.range_days_header_rtl, viewStub), HeaderView {

    var locale: Locale? = null
        set(value) {
            field = value
            value?.let {
                val strings = rootView.context.forceLocaleStrings(
                    it,
                    R.string.prime_date_picker_from,
                    R.string.prime_date_picker_to
                )
                rootView.rangeStartTextView.firstLabelText = strings[0]
                rootView.rangeEndTextView.firstLabelText = strings[1]
            }
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            rootView.rangeStartTextView.typeface = value
            rootView.rangeEndTextView.typeface = value
        }

    var pickType: PickType? = null
        set(value) {
            field = value
            when (value) {
                PickType.RANGE_START -> {
                    rootView.rangeStartBackView.isSelected = true
                    rootView.rangeEndBackView.isSelected = false
                }
                PickType.RANGE_END -> {
                    rootView.rangeStartBackView.isSelected = false
                    rootView.rangeEndBackView.isSelected = true
                }
                else -> {
                }
            }
        }

    var pickedRangeStartDay: PrimeCalendar? = null
        set(value) {
            field = value
            rootView.rangeStartTextView.secondLabelText = value?.shortDateString ?: ""
        }

    var pickedRangeEndDay: PrimeCalendar? = null
        set(value) {
            field = value
            rootView.rangeEndTextView.secondLabelText = value?.shortDateString ?: ""
        }

    var onRangeStartClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.rangeStartBackView.setOnClickListener {
                rootView.rangeStartBackView.isSelected = true
                rootView.rangeEndBackView.isSelected = false
                value?.invoke()
            }
        }

    var onRangeEndClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.rangeEndBackView.setOnClickListener {
                rootView.rangeStartBackView.isSelected = false
                rootView.rangeEndBackView.isSelected = true
                value?.invoke()
            }
        }

}