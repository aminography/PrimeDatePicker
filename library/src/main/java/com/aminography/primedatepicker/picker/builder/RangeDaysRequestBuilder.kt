package com.aminography.primedatepicker.picker.builder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.utils.DateUtils

/**
 * @author aminography
 */
class RangeDaysRequestBuilder<T : PrimeDatePicker> internal constructor(
    clazz: Class<T>,
    initialDateCalendar: PrimeCalendar,
    callback: RangeDaysPickCallback?
) : BaseRequestBuilder<T, RangeDaysPickCallback>(clazz, PickType.RANGE_START, initialDateCalendar, callback) {

    fun initiallyPickedRangeDays(startDay: PrimeCalendar, endDay: PrimeCalendar): RangeDaysRequestBuilder<T> {
        bundle.putString("pickedRangeStartCalendar", DateUtils.storeCalendar(startDay))
        bundle.putString("pickedRangeEndCalendar", DateUtils.storeCalendar(endDay))
        return this
    }

}