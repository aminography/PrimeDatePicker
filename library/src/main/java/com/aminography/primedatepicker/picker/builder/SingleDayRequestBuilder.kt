package com.aminography.primedatepicker.picker.builder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.utils.DateUtils

/**
 * `SingleDayRequestBuilder` is a class in builder mechanism of [PrimeDatePicker] which contains
 * date picker configurations when the pick type is single day.
 *
 * @author aminography
 */
class SingleDayRequestBuilder<T : PrimeDatePicker> internal constructor(
    clazz: Class<T>,
    initialDateCalendar: PrimeCalendar,
    callback: SingleDayPickCallback?
) : BaseRequestBuilder<T, SingleDayPickCallback>(clazz, PickType.SINGLE, initialDateCalendar, callback) {

    /**
     * Specifies initially picked day when the date picker has just shown.
     *
     * @param pickedDay The [PrimeCalendar] to use as the picked date.
     *
     * @return current instance of [SingleDayRequestBuilder].
     */
    fun initiallyPickedSingleDay(pickedDay: PrimeCalendar): SingleDayRequestBuilder<T> {
        bundle.putString("pickedSingleDayCalendar", DateUtils.storeCalendar(pickedDay))
        return this
    }

}