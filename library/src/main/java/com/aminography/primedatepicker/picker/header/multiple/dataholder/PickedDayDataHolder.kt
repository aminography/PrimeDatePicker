package com.aminography.primedatepicker.picker.header.multiple.dataholder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.LabelFormatter

/**
 * @author aminography
 */
data class PickedDayDataHolder(
    override val id: String,
    val calendar: PrimeCalendar,
    private val firstLabelFormatter: LabelFormatter?,
    private val secondLabelFormatter: LabelFormatter?
) : BasePickedDayDataHolder(id) {

    val firstLabel: String = firstLabelFormatter?.invoke(calendar) ?: ""
    val secondLabel: String = secondLabelFormatter?.invoke(calendar) ?: ""

}