package com.aminography.primedatepicker.picker.builder

import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback

/**
 * `RequestBuilder` is a mediator class in builder mechanism of [PrimeDatePicker]. Actually, it
 * specifies date picking type.
 *
 * @author aminography
 */
class RequestBuilder<T : PrimeDatePicker>(
    private val clazz: Class<T>,
    private val initialDateCalendar: PrimeCalendar
) {

    /**
     * Specifies date pick type as single day selection, also sets a callback to be invoked when
     * the select button is clicked and the picked day is a valid day to pick.
     *
     * @param callback The [SingleDayPickCallback] to use which is optional to pass.
     *
     * @return an instance of [SingleDayRequestBuilder] which contains date picker configurations
     * to be specified in builder mechanism.
     */
    @JvmOverloads
    fun pickSingleDay(callback: SingleDayPickCallback? = null): SingleDayRequestBuilder<T> {
        return SingleDayRequestBuilder(clazz, initialDateCalendar, callback)
    }

    /**
     * Specifies date pick type as range of days selection, also sets a callback to be invoked when
     * the select button is clicked and the picked days are valid days to pick.
     *
     * @param callback The [RangeDaysPickCallback] to use which is optional to pass.
     *
     * @return an instance of [RangeDaysPickCallback] which contains date picker configurations
     * to be specified in builder mechanism.
     */
    @JvmOverloads
    fun pickRangeDays(callback: RangeDaysPickCallback? = null): RangeDaysRequestBuilder<T> {
        return RangeDaysRequestBuilder(clazz, initialDateCalendar, callback)
    }

    /**
     * Specifies date pick type as multiple days selection, also sets a callback to be invoked when
     * the select button is clicked and the picked days are valid days to pick.
     *
     * @param callback The [MultipleDaysPickCallback] to use which is optional to pass.
     *
     * @return an instance of [MultipleDaysPickCallback] which contains date picker configurations
     * to be specified in builder mechanism.
     */
    @JvmOverloads
    fun pickMultipleDays(callback: MultipleDaysPickCallback? = null): MultipleDaysRequestBuilder<T> {
        return MultipleDaysRequestBuilder(clazz, initialDateCalendar, callback)
    }

}