package com.aminography.primedatepicker.picker.builder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback

/**
 * @author aminography
 */
class RequestBuilder<T : PrimeDatePicker>(
    private val clazz: Class<T>,
    private val initialDateCalendar: PrimeCalendar
) {

    @JvmOverloads
    fun pickSingleDay(callback: SingleDayPickCallback? = null): SingleDayRequestBuilder<T> {
        return SingleDayRequestBuilder(clazz, initialDateCalendar, callback)
    }

    @JvmOverloads
    fun pickRangeDays(callback: RangeDaysPickCallback? = null): RangeDaysRequestBuilder<T> {
        return RangeDaysRequestBuilder(clazz, initialDateCalendar, callback)
    }

    @JvmOverloads
    fun pickMultipleDays(callback: MultipleDaysPickCallback? = null): MultipleDaysRequestBuilder<T> {
        return MultipleDaysRequestBuilder(clazz, initialDateCalendar, callback)
    }

}