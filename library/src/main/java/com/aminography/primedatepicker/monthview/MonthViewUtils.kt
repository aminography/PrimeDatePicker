package com.aminography.primedatepicker.monthview

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.monthview.PrimeMonthView.PickedDayState.*
import com.aminography.primedatepicker.utils.DateUtils


/**
 * @author aminography
 */

internal fun findPickedDayState(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    pickType: PickType,
    pickedSingleDayCalendar: PrimeCalendar?,
    pickedRangeStartCalendar: PrimeCalendar?,
    pickedRangeEndCalendar: PrimeCalendar?,
    pickedMultipleDaysMap: LinkedHashMap<String, PrimeCalendar>?
): PrimeMonthView.PickedDayState {
    when (pickType) {
        PickType.SINGLE -> {
            pickedSingleDayCalendar?.let { single ->
                if (DateUtils.isSame(year, month, dayOfMonth, single)) {
                    return PICKED_SINGLE
                }
            }
        }
        PickType.RANGE_START, PickType.RANGE_END -> {
            pickedRangeStartCalendar?.let { start ->
                if (DateUtils.isSame(year, month, dayOfMonth, start)) {
                    return if (pickedRangeEndCalendar == null) {
                        START_OF_RANGE_SINGLE
                    } else {
                        if (DateUtils.isSame(start, pickedRangeEndCalendar)) {
                            START_OF_RANGE_SINGLE
                        } else {
                            START_OF_RANGE
                        }
                    }
                } else if (pickedRangeEndCalendar != null) {
                    if (DateUtils.isSame(year, month, dayOfMonth, pickedRangeEndCalendar)) {
                        return END_OF_RANGE
                    } else if (DateUtils.isBetweenExclusive(year, month, dayOfMonth, start, pickedRangeEndCalendar)) {
                        return IN_RANGE
                    }
                } else {
                    return NOTHING
                }
            }
        }
        PickType.MULTIPLE -> {
            pickedMultipleDaysMap?.apply {
                if (containsKey(DateUtils.dateString(year, month, dayOfMonth))) {
                    return PICKED_SINGLE
                }
            }
        }
        PickType.NOTHING -> {
            return NOTHING
        }
    }
    return NOTHING
}

internal fun isDayDisabled(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    minDateCalendar: PrimeCalendar?,
    maxDateCalendar: PrimeCalendar?,
    disabledDaysSet: MutableSet<String>? = null
): Boolean =
    disabledDaysSet?.contains(
        DateUtils.dateString(year, month, dayOfMonth)
    ) == true || DateUtils.isOutOfRange(year, month, dayOfMonth, minDateCalendar, maxDateCalendar)
