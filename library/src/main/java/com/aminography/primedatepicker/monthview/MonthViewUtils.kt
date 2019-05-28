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

    fun pickedSingleState(year: Int, month: Int, pickedSingleCalendar: BaseCalendar?): PrimeMonthView.PickedSingleState {
        val offset = year * 12 + month
        var pickedOffset: Int? = null
        pickedSingleCalendar?.let { day ->
            pickedOffset = day.year * 12 + day.month
        }

        return when (offset) {
            (pickedOffset ?: Int.MAX_VALUE) -> PrimeMonthView.PickedSingleState.CONTAINS
            else -> PrimeMonthView.PickedSingleState.NOT_CONTAINS
        }
    }

    fun pickedRangeState(year: Int, month: Int, pickedStartRangeCalendar: BaseCalendar?, pickedEndRangeCalendar: BaseCalendar?): PrimeMonthView.PickedRangeState {
        val offset = year * 12 + month
        var startOffset: Int? = null
        var endOffset: Int? = null
        pickedStartRangeCalendar?.let { day ->
            startOffset = day.year * 12 + day.month
        }
        pickedEndRangeCalendar?.let { day ->
            endOffset = day.year * 12 + day.month
        }

        return when {
            offset == (startOffset ?: Int.MIN_VALUE) && offset == (endOffset
                    ?: Int.MAX_VALUE) -> PrimeMonthView.PickedRangeState.CONTAINS_START_END
            offset == (startOffset
                    ?: Int.MIN_VALUE) -> PrimeMonthView.PickedRangeState.CONTAINS_START
            offset == (endOffset ?: Int.MAX_VALUE) -> PrimeMonthView.PickedRangeState.CONTAINS_END
            offset > (startOffset ?: Int.MIN_VALUE) && offset < (endOffset
                    ?: Int.MAX_VALUE) -> PrimeMonthView.PickedRangeState.FULLY_IN_RANGE
            offset < (startOffset ?: Int.MIN_VALUE) -> PrimeMonthView.PickedRangeState.BEFORE_START
            offset > (endOffset ?: Int.MAX_VALUE) -> PrimeMonthView.PickedRangeState.AFTER_END
            else -> PrimeMonthView.PickedRangeState.NO_RANGE
        }

    }

}