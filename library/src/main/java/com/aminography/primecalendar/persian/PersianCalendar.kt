package com.aminography.primecalendar.persian

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.common.DateHolder
import com.aminography.primecalendar.common.convertPersianToCivil
import com.aminography.primecalendar.common.convertPersianToHijri
import com.aminography.primecalendar.hijri.HijriCalendar
import java.util.*

/**
 * @author aminography
 */
class PersianCalendar : BaseCalendar(TimeZone.getDefault(), Locale.getDefault()) {

    private var persianYear: Int = 0
    private var persianMonth: Int = 0
    private var persianDay: Int = 0

    override val year: Int
        get() = persianYear

    override val month: Int
        get() = persianMonth

    override val dayOfMonth: Int
        get() = persianDay

    override val monthName: String
        get() = PersianCalendarUtils.persianMonthNames[persianMonth]

    override val weekDayName: String
        get() = when (get(DAY_OF_WEEK)) {
            SATURDAY -> PersianCalendarUtils.persianWeekDays[0]
            SUNDAY -> PersianCalendarUtils.persianWeekDays[1]
            MONDAY -> PersianCalendarUtils.persianWeekDays[2]
            TUESDAY -> PersianCalendarUtils.persianWeekDays[3]
            WEDNESDAY -> PersianCalendarUtils.persianWeekDays[4]
            THURSDAY -> PersianCalendarUtils.persianWeekDays[5]
            else -> PersianCalendarUtils.persianWeekDays[6]
        }

    override val monthLength: Int
        get() = PersianCalendarUtils.getMonthLength(year, month)

    override val isLeapYear: Boolean
        get() = PersianCalendarUtils.isPersianLeapYear(year)

    // ---------------------------------------------------------------------------------------------

    override fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        persianYear = year
        persianMonth = month
        persianDay = dayOfMonth
        val gregorianYearMonthDay = PersianCalendarUtils.persianToGregorian(DateHolder(persianYear, persianMonth, persianDay))
        super.setDate(gregorianYearMonthDay.year, gregorianYearMonthDay.month, gregorianYearMonthDay.day)
    }

    override fun add(field: Int, amount: Int) {
        if (amount == 0) {
            return
        }
        if (field < 0 || field >= ZONE_OFFSET) {
            throw IllegalArgumentException()
        }

        when (field) {
            YEAR -> setDate(persianYear + amount, persianMonth, persianDay)
            MONTH -> setDate(persianYear + (persianMonth + amount) / 12, (persianMonth + amount) % 12, persianDay)
            else -> {
                super.add(field, amount)
                recalculate()
            }
        }
    }

/*    override fun get(field: Int): Int {
        return when (field) {
            YEAR -> persianYear
            MONTH -> persianMonth
            DAY_OF_MONTH -> dayOfMonth
            WEEK_OF_YEAR -> throw NotImplementedError("WEEK_OF_YEAR is not implemented yet!")
            DAY_OF_YEAR -> throw NotImplementedError("DAY_OF_YEAR is not implemented yet!")
            DAY_OF_WEEK -> throw NotImplementedError("DAY_OF_WEEK is not implemented yet!")
            DAY_OF_WEEK_IN_MONTH -> throw NotImplementedError("DAY_OF_WEEK_IN_MONTH is not implemented yet!")
            else -> super.get(field)
        }
    }*/

    override fun set(field: Int, value: Int) {
        super.set(field, value)
        recalculate()
    }

    override fun setTimeInMillis(millis: Long) {
        super.setTimeInMillis(millis)
        recalculate()
    }

    override fun setTimeZone(zone: TimeZone) {
        super.setTimeZone(zone)
        recalculate()
    }

    private fun recalculate() {
        val persianYearMonthDay = PersianCalendarUtils.gregorianToPersian(
                DateHolder(
                        super.get(YEAR),
                        super.get(MONTH),
                        super.get(DAY_OF_MONTH)
                )
        )
        persianYear = persianYearMonthDay.year
        persianMonth = persianYearMonthDay.month
        persianDay = persianYearMonthDay.day
    }

    // ---------------------------------------------------------------------------------------------

    override fun toCivil(): CivilCalendar = convertPersianToCivil(this)

    override fun toPersian(): PersianCalendar = this

    override fun toHijri(): HijriCalendar = convertPersianToHijri(this)

}