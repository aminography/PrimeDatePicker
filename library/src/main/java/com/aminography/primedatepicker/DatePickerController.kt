package com.aminography.primedatepicker

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primedatepicker.fragment.DateCalendarPickerBottomSheetDialogFragment

/**
 * Controller class to communicate among the various components of the date picker dialog.
 */
interface DatePickerController {

    val selectedDay: BaseCalendar

    val selectableDays: Set<BaseCalendar>?

    val minYear: Int

    val maxYear: Int

    val minDate: BaseCalendar?

    val maxDate: BaseCalendar?

    var typeface: String?

    fun onDayOfMonthSelected(year: Int, month: Int, day: Int)

    fun registerOnDateChangedListener(listener: DateCalendarPickerBottomSheetDialogFragment.OnDateChangedListener)

    fun unregisterOnDateChangedListener(listener: DateCalendarPickerBottomSheetDialogFragment.OnDateChangedListener)

}
