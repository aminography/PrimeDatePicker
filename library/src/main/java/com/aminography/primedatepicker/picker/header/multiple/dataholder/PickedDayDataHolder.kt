package com.aminography.primedatepicker.picker.header.multiple.dataholder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.LabelFormatter
import com.aminography.primedatepicker.utils.localizeDigits

/**
 * @author aminography
 */
data class PickedDayDataHolder(
    override val id: String,
    val calendar: PrimeCalendar,
    private val firstLabelFormatter: LabelFormatter?,
    private val secondLabelFormatter: LabelFormatter?
) : BasePickedDayDataHolder(id) {

    val firstLabel: String = firstLabelFormatter?.invoke(calendar)?.localizeDigits(calendar.locale) ?: ""
    val secondLabel: String = secondLabelFormatter?.invoke(calendar)?.localizeDigits(calendar.locale) ?: ""

}