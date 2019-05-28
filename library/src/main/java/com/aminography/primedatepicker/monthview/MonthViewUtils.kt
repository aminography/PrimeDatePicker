package com.aminography.primedatepicker.monthview

import com.aminography.primecalendar.base.BaseCalendar


/**
 * @author aminography
 */
internal object MonthViewUtils {

    fun pickedDayState(
            year: Int,
            month: Int,
            dayOfMonth: Int,
            pickType: PrimeMonthView.PickType,
            pickedSingleCalendar: BaseCalendar?,
            pickedStartRangeCalendar: BaseCalendar?,
            pickedEndRangeCalendar: BaseCalendar?
    ): PrimeMonthView.PickedDayState {
        when (pickType) {
            PrimeMonthView.PickType.SINGLE -> {
                pickedSingleCalendar?.let { single ->
                    if (isSameDay(year, month, dayOfMonth, single)) {
                        return PrimeMonthView.PickedDayState.PICKED_SINGLE
                    }
                }
            }
            PrimeMonthView.PickType.START_RANGE, PrimeMonthView.PickType.END_RANGE -> {
                pickedStartRangeCalendar?.let { start ->
                    if (isSameDay(year, month, dayOfMonth, start)) {
                        return if (pickedEndRangeCalendar == null) {
                            PrimeMonthView.PickedDayState.START_OF_RANGE_SINGLE
                        } else {
                            if (isSameDay(start, pickedEndRangeCalendar)) {
                                PrimeMonthView.PickedDayState.START_OF_RANGE_SINGLE
                            } else {
                                PrimeMonthView.PickedDayState.START_OF_RANGE
                            }
                        }
                    } else if (pickedEndRangeCalendar != null) {
                        if (isSameDay(year, month, dayOfMonth, pickedEndRangeCalendar)) {
                            return PrimeMonthView.PickedDayState.END_OF_RANGE
                        } else if (isBetween(year, month, dayOfMonth, start, pickedEndRangeCalendar)) {
                            return PrimeMonthView.PickedDayState.IN_RANGE
                        }
                    } else {
                        return PrimeMonthView.PickedDayState.NOTHING
                    }
                }
            }
        }
        return PrimeMonthView.PickedDayState.NOTHING
    }

    private fun isSameDay(year: Int, month: Int, dayOfMonth: Int, calendar: BaseCalendar) =
            year == calendar.year && month == calendar.month && dayOfMonth == calendar.dayOfMonth

    private fun isSameDay(start: BaseCalendar, end: BaseCalendar) =
            start.year == end.year && start.month == end.month && start.dayOfMonth == end.dayOfMonth

    private fun isBetween(year: Int, month: Int, dayOfMonth: Int, start: BaseCalendar, end: BaseCalendar): Boolean {
        val offset = year * 12 + month
        val startOffset = start.year * 12 + start.month
        val endOffset = end.year * 12 + end.month

        return if (offset == startOffset && (startOffset == endOffset)) {
            dayOfMonth > start.dayOfMonth && dayOfMonth < end.dayOfMonth
        } else when (offset) {
            startOffset -> dayOfMonth > start.dayOfMonth
            endOffset -> dayOfMonth < end.dayOfMonth
            else -> offset in (startOffset + 1) until endOffset
        }
    }

    fun isBefore(year: Int, month: Int, dayOfMonth: Int, calendar: BaseCalendar?): Boolean =
            calendar?.let { min ->
                year < min.year ||
                        (year == min.year && month < min.month) ||
                        (year == min.year && month == min.month && dayOfMonth < min.dayOfMonth)
            } ?: false

    fun isAfter(year: Int, month: Int, dayOfMonth: Int, calendar: BaseCalendar?): Boolean =
            calendar?.let { max ->
                year > max.year ||
                        (year == max.year && month > max.month) ||
                        (year == max.year && month == max.month && dayOfMonth > max.dayOfMonth)
            } ?: false


}