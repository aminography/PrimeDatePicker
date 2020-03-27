package com.aminography.primedatepicker.fragment.header.range

import android.graphics.Typeface
import android.view.ViewStub
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.fragment.header.BaseHeaderView
import kotlinx.android.synthetic.main.range_days_header.view.*

/**
 * @author aminography
 */
class RangeHeaderView(
    viewStub: ViewStub
) : BaseHeaderView(R.layout.range_days_header, viewStub) {

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