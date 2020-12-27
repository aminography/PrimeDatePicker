package com.aminography.primedatepicker.picker.builder

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.BaseDayPickCallback
import com.aminography.primedatepicker.picker.theme.base.ThemeFactory
import com.aminography.primedatepicker.utils.DateUtils
import java.util.*

/**
 * `BaseRequestBuilder` is a class in builder mechanism of [PrimeDatePicker] which contains common
 * date picker configurations between all pick types.
 *
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
        bundle.putString("pickType", pickType.name)
        bundle.putString("initialDateCalendar", DateUtils.storeCalendar(initialDateCalendar))
    }

    /**
     * Specifies the minimum feasible date to be shown in date picker, which is selectable.
     *
     * @param minDate The [PrimeCalendar] to use as the minimum feasible date.
     *
     * @return current instance of [BaseRequestBuilder].
     */
    fun minPossibleDate(minDate: PrimeCalendar): BaseRequestBuilder<T, C> {
        bundle.putString("minDateCalendar", DateUtils.storeCalendar(minDate))
        return this
    }

    /**
     * Specifies the maximum feasible date to be shown in date picker, which is selectable.
     *
     * @param maxDate The [PrimeCalendar] to use as the maximum feasible date.
     *
     * @return current instance of [BaseRequestBuilder].
     */
    fun maxPossibleDate(maxDate: PrimeCalendar): BaseRequestBuilder<T, C> {
        bundle.putString("maxDateCalendar", DateUtils.storeCalendar(maxDate))
        return this
    }

    /**
     * Specifies the day that should be considered as start of the week.
     *
     * @param firstDayOfWeek The day to use as the start day of the week.
     * Possible values are: [Calendar.SUNDAY], [Calendar.MONDAY], etc.
     *
     * Note that if you specify firstDayOfWeek for the initialDate for example in
     * [PrimeDatePicker.bottomSheetWith], calling this function overrides it.
     *
     * @return current instance of [BaseRequestBuilder].
     */
    fun firstDayOfWeek(firstDayOfWeek: Int): BaseRequestBuilder<T, C> {
        bundle.putInt("firstDayOfWeek", firstDayOfWeek)
        return this
    }

    /**
     * Specifies the list of disabled days, which aren't selectable.
     *
     * @param disabledDays The list of [PrimeCalendar]s which aren't selectable.
     *
     * @return current instance of [BaseRequestBuilder].
     */
    fun disabledDays(disabledDays: List<PrimeCalendar>): BaseRequestBuilder<T, C> {
        bundle.putStringArrayList("disabledDaysList", disabledDays.map {
            DateUtils.storeCalendar(it)!!
        } as ArrayList<String>)
        return this
    }

    /**
     * Specifies the theme for the [PrimeDatePicker].
     *
     * @param themeFactory The [ThemeFactory] to use as the source of theme characteristics.
     *
     * @return current instance of [BaseRequestBuilder].
     */
    fun applyTheme(themeFactory: ThemeFactory): BaseRequestBuilder<T, C> {
        bundle.putSerializable("themeFactory", themeFactory)
        return this
    }

    /**
     * Builds an instance of [PrimeDatePicker] configured by the builder parameters which is ready
     * to show using [PrimeDatePicker.show].
     *
     * @return an instance of [PrimeDatePicker].
     */
    fun build(): T = clazz.getDeclaredConstructor().newInstance().also {
        it.setDayPickCallback(callback)
        if (it is DialogFragment) {
            it.arguments = bundle
        }
    }

    // ---------------------------------------------------------------------------------------------

    @Deprecated(
        level = DeprecationLevel.ERROR,
        message = "This method is removed and should not be used anymore.",
        replaceWith = ReplaceWith("applyTheme, try to override the typeface path on an instance of ThemeFactory.")
    )
    fun typefacePath(@Suppress("UNUSED_PARAMETER") typefacePath: String): BaseRequestBuilder<T, C> {
        return this
    }

    @Deprecated(
        level = DeprecationLevel.ERROR,
        message = "This method is removed and should not be used anymore.",
        replaceWith = ReplaceWith("applyTheme, try to override the animate selection on an instance of ThemeFactory.")
    )
    fun animateSelection(@Suppress("UNUSED_PARAMETER") animateSelection: Boolean): BaseRequestBuilder<T, C> {
        return this
    }

}