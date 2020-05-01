package com.aminography.primedatepicker.picker.builder

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.BaseDayPickCallback
import com.aminography.primedatepicker.picker.theme.ThemeFactory
import com.aminography.primedatepicker.utils.DateUtils
import java.util.*

/**
 * @author aminography
 */
abstract class BaseRequestBuilder<T : PrimeDatePicker, C : BaseDayPickCallback> internal constructor(
    private val clazz: Class<T>,
    pickType: PickType,
    initialDateCalendar: PrimeCalendar,
    private val callback: C?
) {

    protected val bundle = Bundle()

    init {
        bundle.putString("initialDateCalendar", DateUtils.storeCalendar(initialDateCalendar))
        bundle.putString("pickType", pickType.name)
    }

    fun weekStartDay(weekStartDay: Int): BaseRequestBuilder<T, C> {
        bundle.putInt("weekStartDay", weekStartDay)
        return this
    }

    fun minPossibleDate(minDate: PrimeCalendar?): BaseRequestBuilder<T, C> {
        bundle.putString("minDateCalendar", DateUtils.storeCalendar(minDate))
        return this
    }

    fun maxPossibleDate(maxDate: PrimeCalendar?): BaseRequestBuilder<T, C> {
        bundle.putString("maxDateCalendar", DateUtils.storeCalendar(maxDate))
        return this
    }

    fun disabledDays(disabledDays: List<PrimeCalendar>): BaseRequestBuilder<T, C> {
        bundle.putStringArrayList("disabledDaysList", disabledDays.map {
            DateUtils.storeCalendar(it)!!
        } as ArrayList<String>)
        return this
    }

    fun applyTheme(themeFactory: ThemeFactory): BaseRequestBuilder<T, C> {
        bundle.putSerializable("themeFactory", themeFactory)
        return this
    }

    @Deprecated(
        level = DeprecationLevel.ERROR,
        message = "This method is removed and should not be used anymore.",
        replaceWith = ReplaceWith("applyTheme, try to override the typeface path on an instance of ThemeFactory.")
    )
    fun typefacePath(typefacePath: String): BaseRequestBuilder<T, C> {
        return this
    }

    @Deprecated(
        level = DeprecationLevel.ERROR,
        message = "This method is removed and should not be used anymore.",
        replaceWith = ReplaceWith("applyTheme, try to override the animate selection on an instance of ThemeFactory.")
    )
    fun animateSelection(animateSelection: Boolean): BaseRequestBuilder<T, C> {
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