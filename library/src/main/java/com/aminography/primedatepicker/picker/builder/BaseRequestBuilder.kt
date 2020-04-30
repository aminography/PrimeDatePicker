package com.aminography.primedatepicker.picker.builder

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.BaseDayPickCallback
import com.aminography.primedatepicker.picker.theme.BaseThemeFactory
import com.aminography.primedatepicker.utils.DateUtils
import java.util.*

/**
 * @author aminography
 */
abstract class BaseRequestBuilder<T : PrimeDatePicker, P : BaseDayPickCallback> internal constructor(
    private val clazz: Class<T>,
    pickType: PickType,
    initialDateCalendar: PrimeCalendar,
    private val callback: P?
) {

    protected val bundle = Bundle()

    init {
        bundle.putString("initialDateCalendar", DateUtils.storeCalendar(initialDateCalendar))
        bundle.putString("pickType", pickType.name)
    }

    fun weekStartDay(weekStartDay: Int): BaseRequestBuilder<T, P> {
        bundle.putInt("weekStartDay", weekStartDay)
        return this
    }

    fun minPossibleDate(minDate: PrimeCalendar?): BaseRequestBuilder<T, P> {
        bundle.putString("minDateCalendar", DateUtils.storeCalendar(minDate))
        return this
    }

    fun maxPossibleDate(maxDate: PrimeCalendar?): BaseRequestBuilder<T, P> {
        bundle.putString("maxDateCalendar", DateUtils.storeCalendar(maxDate))
        return this
    }

    fun disabledDays(disabledDays: List<PrimeCalendar>): BaseRequestBuilder<T, P> {
        bundle.putStringArrayList("disabledDaysList", disabledDays.map {
            DateUtils.storeCalendar(it)!!
        } as ArrayList<String>)
        return this
    }

    fun applyTheme(themeFactory: BaseThemeFactory): BaseRequestBuilder<T, P> {
        bundle.putSerializable("themeFactory", themeFactory)
        return this
    }

    fun build(): T = build(clazz)

    private fun <T : PrimeDatePicker> build(clazz: Class<T>): T {
        return clazz.getDeclaredConstructor().newInstance().also {
            it.setDayPickCallback(callback)
            if (it is DialogFragment) {
                it.arguments = bundle
            }
        }
    }

}