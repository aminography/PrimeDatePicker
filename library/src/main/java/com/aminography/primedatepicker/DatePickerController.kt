package com.aminography.primedatepicker

import com.aminography.primecalendar.base.BaseCalendar

/**
 * Controller class to communicate among the various components of the date picker dialog.
 */
interface DatePickerController {

    val selectedDay: MonthAdapter.CalendarDay

    val highlightedDays: Array<BaseCalendar>?

    val selectableDays: Array<BaseCalendar>?

    val firstDayOfWeek: Int

    val minYear: Int

    val maxYear: Int

    val minDate: BaseCalendar?

    val maxDate: BaseCalendar?

    var typeface: String?

    fun onYearSelected(year: Int)

    fun onDayOfMonthSelected(year: Int, month: Int, day: Int)

    fun registerOnDateChangedListener(listener: DateCalendarPickerBottomSheetDialogFragment.OnDateChangedListener)

    fun unregisterOnDateChangedListener(listener: DateCalendarPickerBottomSheetDialogFragment.OnDateChangedListener)

}
