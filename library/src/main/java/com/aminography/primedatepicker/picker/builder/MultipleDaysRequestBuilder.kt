package com.aminography.primedatepicker.picker.builder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.utils.DateUtils
import java.util.*

/**
 * `MultipleDaysRequestBuilder` is a class in builder mechanism of [PrimeDatePicker] which contains
 * date picker configurations when the pick type is multiple days.
 *
 * @author aminography
 */
class MultipleDaysRequestBuilder<T : PrimeDatePicker> internal constructor(
    clazz: Class<T>,
    initialDateCalendar: PrimeCalendar,
    callback: MultipleDaysPickCallback?
) : BaseRequestBuilder<T, MultipleDaysPickCallback>(clazz, PickType.MULTIPLE, initialDateCalendar, callback) {

    /**
     * Specifies initially picked multiple days when the date picker has just shown.
     *
     * @param pickedDays The list of [PrimeCalendar]s to use as the picked dates.
     *
     * @return current instance of [MultipleDaysRequestBuilder].
     */
    fun initiallyPickedMultipleDays(pickedDays: List<PrimeCalendar>): MultipleDaysRequestBuilder<T> {
        bundle.putStringArrayList("pickedMultipleDaysList", pickedDays.map {
            DateUtils.storeCalendar(it)!!
        } as ArrayList<String>)
        return this
    }

//    /**
//     * Specifies minimum number of days that can be picked.
//     *
//     * @param count The minimum number of days that can be picked.
//     *
//     * @return current instance of [MultipleDaysRequestBuilder].
//     */
//    fun minPickedDaysCount(count: Int): MultipleDaysRequestBuilder<T> {
//        bundle.putInt("minPickedDaysCount", count)
//        return this
//    }
//
//    /**
//     * Specifies maximum number of days that can be picked.
//     *
//     * @param count The maximum number of days that can be picked.
//     *
//     * @return current instance of [MultipleDaysRequestBuilder].
//     */
//    fun maxPickedDaysCount(count: Int): MultipleDaysRequestBuilder<T> {
//        bundle.putInt("maxPickedDaysCount", count)
//        return this
//    }

}