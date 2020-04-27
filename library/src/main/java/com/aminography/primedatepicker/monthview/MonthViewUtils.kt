package com.aminography.primedatepicker.monthview

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.utils.DateUtils


/**
 * @author aminography
 */
internal object MonthViewUtils {

    internal fun findDayState(
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
                        return PrimeMonthView.PickedDayState.PICKED_SINGLE
                    }
                }
            }
            PickType.RANGE_START, PickType.RANGE_END -> {
                pickedRangeStartCalendar?.let { start ->
                    if (DateUtils.isSame(year, month, dayOfMonth, start)) {
                        return if (pickedRangeEndCalendar == null) {
                            PrimeMonthView.PickedDayState.START_OF_RANGE_SINGLE
                        } else {
                            if (DateUtils.isSame(start, pickedRangeEndCalendar)) {
                                PrimeMonthView.PickedDayState.START_OF_RANGE_SINGLE
                            } else {
                                PrimeMonthView.PickedDayState.START_OF_RANGE
                            }
                        }
                    } else if (pickedRangeEndCalendar != null) {
                        if (DateUtils.isSame(year, month, dayOfMonth, pickedRangeEndCalendar)) {
                            return PrimeMonthView.PickedDayState.END_OF_RANGE
                        } else if (DateUtils.isBetweenExclusive(year, month, dayOfMonth, start, pickedRangeEndCalendar)) {
                            return PrimeMonthView.PickedDayState.IN_RANGE
                        }
                    } else {
                        return PrimeMonthView.PickedDayState.NOTHING
                    }
                }
            }
            PickType.MULTIPLE -> {
                pickedMultipleDaysMap?.apply {
                    if (containsKey(DateUtils.dateString(year, month, dayOfMonth))) {
                        return PrimeMonthView.PickedDayState.PICKED_SINGLE
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
