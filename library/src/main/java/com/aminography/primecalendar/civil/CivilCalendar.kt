package com.aminography.primecalendar.civil

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.convertCivilToHijri
import com.aminography.primecalendar.common.convertCivilToPersian
import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import java.util.*

/**
 * @author aminography
 */
class CivilCalendar : BaseCalendar() {

    override var year: Int = get(Calendar.YEAR)
        get() = get(Calendar.YEAR)
        set(value) {
            field = value
            set(Calendar.YEAR, value)
        }

    override var month: Int = get(Calendar.MONTH)
        get() = get(Calendar.MONTH)
        set(value) {
            field = value
            set(Calendar.MONTH, value)
        }

    override var dayOfMonth: Int = get(Calendar.DAY_OF_MONTH)
        get() = get(Calendar.DAY_OF_MONTH)
        set(value) {
            field = value
            set(Calendar.DAY_OF_MONTH, value)
        }

    override val monthName: String
        get() = getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH)

    override val weekDayName: String
        get() = getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH)

    override val monthLength: Int
        get() = CivilCalendarUtils.getMonthLength(year, month)

    override val isLeapYear: Boolean
        get() = CivilCalendarUtils.isGregorianLeapYear(year)

    // ---------------------------------------------------------------------------------------------

    override fun toCivil(): CivilCalendar = this

    override fun toPersian(): PersianCalendar = convertCivilToPersian(this)

    override fun toHijri(): HijriCalendar = convertCivilToHijri(this)

}
