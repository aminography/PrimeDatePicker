package com.aminography.primedatepicker.old

import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.persian.PersianCalendar

object OldUtils {

    fun newCalendar(): BaseCalendar {
        return when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> CivilCalendar()
            CalendarType.PERSIAN -> PersianCalendar()
            CalendarType.HIJRI -> HijriCalendar()
        }
    }
}