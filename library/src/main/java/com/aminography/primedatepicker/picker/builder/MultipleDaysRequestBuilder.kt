package com.aminography.primedatepicker.picker.builder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.utils.DateUtils
import java.util.*

/**
 * @author aminography
 */
class MultipleDaysRequestBuilder<T : PrimeDatePicker> internal constructor(
    clazz: Class<T>,
    initialDateCalendar: PrimeCalendar,
    callback: MultipleDaysPickCallback?
) : BaseRequestBuilder<T, MultipleDaysPickCallback>(clazz, PickType.MULTIPLE, initialDateCalendar, callback) {

    fun initiallyPickedMultipleDays(multipleDays: List<PrimeCalendar>): MultipleDaysRequestBuilder<T> {
        bundle.putStringArrayList("pickedMultipleDaysList", multipleDays.map {
            DateUtils.storeCalendar(it)!!
        } as ArrayList<String>)
        return this
    }

}