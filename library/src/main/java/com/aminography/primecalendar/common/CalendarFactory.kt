package com.aminography.primecalendar.common

import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.persian.PersianCalendar

/**
 * @author aminography
 */
object CalendarFactory {

    fun <T : BaseCalendar> newInstance(clazz: Class<T>): T = clazz.getDeclaredConstructor().newInstance()

    fun newInstance(type: CalendarType): BaseCalendar {
        return when (type) {
            CalendarType.CIVIL -> CivilCalendar()
            CalendarType.PERSIAN -> PersianCalendar()
            CalendarType.HIJRI -> HijriCalendar()
        }
    }

}
