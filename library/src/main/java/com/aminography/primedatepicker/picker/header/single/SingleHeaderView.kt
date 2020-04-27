package com.aminography.primedatepicker.picker.header.single

import android.graphics.Typeface
import android.view.ViewStub
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.LabelFormatter
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.picker.header.BaseLazyView
import com.aminography.primedatepicker.picker.header.HeaderView
import com.aminography.primedatepicker.utils.forceLocaleStrings
import com.aminography.primedatepicker.utils.localizeDigits
import kotlinx.android.synthetic.main.single_day_header.view.*
import java.util.*

/**
 * @author aminography
 */
class SingleHeaderView(
    viewStub: ViewStub
) : BaseLazyView(R.layout.single_day_header, viewStub), HeaderView {

    var locale: Locale? = null
        set(value) {
            field = value
            value?.let {
                val strings = rootView.context.forceLocaleStrings(
                    it,
                    R.string.prime_date_picker_selected_day
                )
                rootView.pickedTextView.firstLabelText = strings[0]
            }
        }

    var typeface: Typeface? = null
        set(value) {
            field = value
            rootView.pickedTextView.typeface = value
        }

    var labelFormatter: LabelFormatter? = null

    var pickedDay: PrimeCalendar? = null
        set(value) {
            field = value
            rootView.pickedTextView.secondLabelText = value?.let {
                labelFormatter?.invoke(it)?.localizeDigits(value.locale)
            } ?: ""
        }

    var onPickedDayClickListener: (() -> Unit)? = null
        set(value) {
            field = value
            rootView.setOnClickListener {
                value?.invoke()
            }
        }

}