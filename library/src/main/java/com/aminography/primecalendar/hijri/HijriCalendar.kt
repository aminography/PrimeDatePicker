package com.aminography.primecalendar.hijri

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.common.DateHolder
import com.aminography.primecalendar.common.convertHijriToCivil
import com.aminography.primecalendar.common.convertHijriToPersian
import com.aminography.primecalendar.persian.PersianCalendar
import java.util.*

/**
 * @author aminography
 */
class HijriCalendar : BaseCalendar(TimeZone.getDefault(), Locale.getDefault()) {

    private var hijriYear: Int = 0
    private var hijriMonth: Int = 0
    private var hijriDayOfMonth: Int = 0

    override var year: Int = hijriYear
        get() = hijriYear
        set(value) {
            field = value
            setDate(value, month, dayOfMonth)
        }

    override var month: Int = hijriMonth
        get() = hijriMonth
        set(value) {
            field = value
            setDate(year, value, dayOfMonth)
        }

    override var dayOfMonth: Int = hijriDayOfMonth
        get() = hijriDayOfMonth
        set(value) {
            field = value
            setDate(year, month, value)
        }

    override val monthName: String
        get() = HijriCalendarUtils.monthNames[hijriMonth]

    override val weekDayName: String
        get() = when (get(DAY_OF_WEEK)) {
            SATURDAY -> HijriCalendarUtils.hijriWeekDays[0]
            SUNDAY -> HijriCalendarUtils.hijriWeekDays[1]
            MONDAY -> HijriCalendarUtils.hijriWeekDays[2]
            TUESDAY -> HijriCalendarUtils.hijriWeekDays[3]
            WEDNESDAY -> HijriCalendarUtils.hijriWeekDays[4]
            THURSDAY -> HijriCalendarUtils.hijriWeekDays[5]
            else -> HijriCalendarUtils.hijriWeekDays[6]
        }

    override val monthLength: Int
        get() = HijriCalendarUtils.getMonthLength(year, month)

    override val isLeapYear: Boolean
        get() = HijriCalendarUtils.isHijriLeapYear(year)

    // ---------------------------------------------------------------------------------------------

    override fun setDate(year: Int, month: Int, dayOfMonth: Int) {
        hijriYear = year
        hijriMonth = month
        hijriDayOfMonth = dayOfMonth
        val gregorianYearMonthDay = HijriCalendarUtils.hijriToGregorian(DateHolder(hijriYear, hijriMonth, hijriDayOfMonth))
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
            YEAR -> setDate(hijriYear + amount, hijriMonth, hijriDayOfMonth)
            MONTH -> setDate(hijriYear + (hijriMonth + amount) / 12, (hijriMonth + amount) % 12, hijriDayOfMonth)
            else -> {
                super.add(field, amount)
                recalculate()
            }
        }
    }

/*    override fun get(field: Int): Int {
        return when (field) {
            YEAR -> hijriYear
            MONTH -> hijriMonth
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
        val hijriYearMonthDay = HijriCalendarUtils.gregorianToHijri(
                DateHolder(
                        super.get(YEAR),
                        super.get(MONTH),
                        super.get(DAY_OF_MONTH)
                )
        )
        hijriYear = hijriYearMonthDay.year
        hijriMonth = hijriYearMonthDay.month
        hijriDayOfMonth = hijriYearMonthDay.day
    }

    // ---------------------------------------------------------------------------------------------

    override fun toCivil(): CivilCalendar = convertHijriToCivil(this)

    override fun toPersian(): PersianCalendar = convertHijriToPersian(this)

    override fun toHijri(): HijriCalendar = this

}
