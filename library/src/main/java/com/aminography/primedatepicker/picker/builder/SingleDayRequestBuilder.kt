package com.aminography.primedatepicker.picker.builder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.utils.DateUtils

/**
 * @author aminography
 */
class SingleDayRequestBuilder<T : PrimeDatePicker> internal constructor(
    clazz: Class<T>,
    initialDateCalendar: PrimeCalendar,
    callback: SingleDayPickCallback?
) : BaseRequestBuilder<T, SingleDayPickCallback>(clazz, PickType.SINGLE, initialDateCalendar, callback) {

    fun initiallyPickedSingleDay(singleDay: PrimeCalendar): SingleDayRequestBuilder<T> {
        bundle.putString("pickedSingleDayCalendar", DateUtils.storeCalendar(singleDay))
        return this
    }

}