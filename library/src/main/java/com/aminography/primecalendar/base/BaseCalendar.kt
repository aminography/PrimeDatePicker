package com.aminography.primecalendar.base

import com.aminography.primecalendar.common.IConverter
import java.util.*

/**
 * @author aminography
 */
abstract class BaseCalendar : GregorianCalendar, IConverter {

    abstract var year: Int

    abstract var month: Int

    abstract var dayOfMonth: Int

    abstract val monthName: String

    abstract val weekDayName: String

    abstract val monthLength: Int

    abstract val isLeapYear: Boolean

    val longDateString: String
        get() = "$weekDayName,  $dayOfMonth  $monthName  $year"

    @Suppress("MemberVisibilityCanBePrivate")
    val shortDateString: String
        get() = normalize(year) + delimiter + normalize(month) + delimiter + normalize(dayOfMonth)

    @Suppress("unused")
    val monthDayString: String
        get() = monthName + " " + normalize(dayOfMonth)

    open fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        set(year, month, dayOfMonth)
    }

    override fun toString(): String {
        val s = super.toString()
        return "${s.substring(0, s.length - 1)}, Date=$shortDateString]"
    }

    // ---------------------------------------------------------------------------------------------

    constructor() : super()

    constructor(zone: TimeZone) : super(zone)

    constructor(aLocale: Locale) : super(aLocale)

    constructor(zone: TimeZone, aLocale: Locale) : super(zone, aLocale)

    constructor(year: Int, month: Int, dayOfMonth: Int) : super(year, month, dayOfMonth)

    constructor(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int) : super(year, month, dayOfMonth, hourOfDay, minute)

    constructor(year: Int, month: Int, dayOfMonth: Int, hourOfDay: Int, minute: Int, second: Int) : super(year, month, dayOfMonth, hourOfDay, minute, second)

}
