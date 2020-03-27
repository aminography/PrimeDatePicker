package com.aminography.primedatepicker.fragment.header.single

import android.graphics.Typeface
import android.view.ViewStub
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.fragment.header.BaseHeaderView
import kotlinx.android.synthetic.main.single_day_header.view.*

/**
 * @author aminography
 */
class SingleHeaderView(
    viewStub: ViewStub
) : BaseHeaderView(R.layout.single_day_header, viewStub) {

    var typeface: Typeface? = null
        set(value) {
            field = value
            rootView.pickedTextView.typeface = value
        }

    var pickedDay: PrimeCalendar? = null
        set(value) {
            field = value
            rootView.pickedTextView.secondLabelText = value?.shortDateString ?: ""
        }

    var onPickedDayClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.setOnClickListener {
                value?.invoke()
            }
        }

}