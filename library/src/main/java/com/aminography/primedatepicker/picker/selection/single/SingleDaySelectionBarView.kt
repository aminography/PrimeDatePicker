package com.aminography.primedatepicker.picker.selection.single

import android.graphics.Typeface
import android.view.ViewStub
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.localizeDigits
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.common.LabelFormatter
import com.aminography.primedatepicker.picker.base.BaseLazyView
import com.aminography.primedatepicker.picker.selection.SelectionBarView
import com.aminography.primedatepicker.utils.forceLocaleStrings
import kotlinx.android.synthetic.main.selection_bar_single_day_container.view.*
import java.util.*

/**
 * @author aminography
 */
internal class SingleDaySelectionBarView(
    viewStub: ViewStub
) : BaseLazyView(
    R.layout.selection_bar_single_day_container, viewStub
), SelectionBarView {

    var locale: Locale? = null
        set(value) {
            field = value
            value?.let {
                val strings = rootView.context.forceLocaleStrings(
                    it,
                    R.string.prime_date_picker_selected_day
                )
                rootView.pickedTextView.topLabelText = strings[0]
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
            rootView.pickedTextView.bottomLabelText = value?.let {
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