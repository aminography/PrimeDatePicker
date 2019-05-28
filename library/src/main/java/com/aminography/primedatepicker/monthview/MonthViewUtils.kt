package com.aminography.primedatepicker.monthview

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primedatepicker.DateUtils
import com.aminography.primedatepicker.PickType


/**
 * @author aminography
 */
@Suppress("MemberVisibilityCanBePrivate")
internal object MonthViewUtils {

    fun findDayState(
            year: Int,
            month: Int,
            dayOfMonth: Int,
            pickType: PickType,
            pickedSingleDayCalendar: BaseCalendar?,
            pickedStartRangeCalendar: BaseCalendar?,
            pickedEndRangeCalendar: BaseCalendar?
    ): PrimeMonthView.PickedDayState {

        when (pickType) {
            PickType.SINGLE -> {
                pickedSingleDayCalendar?.let { single ->
                    if (DateUtils.isSame(year, month, dayOfMonth, single)) {
                        return PrimeMonthView.PickedDayState.PICKED_SINGLE
                    }
                }
            }
            PickType.START_RANGE, PickType.END_RANGE -> {
                pickedStartRangeCalendar?.let { start ->
                    if (DateUtils.isSame(year, month, dayOfMonth, start)) {
                        return if (pickedEndRangeCalendar == null) {
                            PrimeMonthView.PickedDayState.START_OF_RANGE_SINGLE
                        } else {
                            if (DateUtils.isSame(start, pickedEndRangeCalendar)) {
                                PrimeMonthView.PickedDayState.START_OF_RANGE_SINGLE
                            } else {
                                PrimeMonthView.PickedDayState.START_OF_RANGE
                            }
                        }
                    } else if (pickedEndRangeCalendar != null) {
                        if (DateUtils.isSame(year, month, dayOfMonth, pickedEndRangeCalendar)) {
                            return PrimeMonthView.PickedDayState.END_OF_RANGE
                        } else if (DateUtils.isBetweenExclusive(year, month, dayOfMonth, start, pickedEndRangeCalendar)) {
                            return PrimeMonthView.PickedDayState.IN_RANGE
                        }
                    } else {
                        return PrimeMonthView.PickedDayState.NOTHING
                    }
                }
            }
            PickType.NOTHING -> {
                return PrimeMonthView.PickedDayState.NOTHING
            }
        }
        return PrimeMonthView.PickedDayState.NOTHING
    }

}