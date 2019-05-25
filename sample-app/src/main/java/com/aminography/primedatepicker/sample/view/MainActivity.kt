package com.aminography.primedatepicker.sample.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.fragment.DateCalendarPickerBottomSheetDialogFragment
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        civilButton.setOnClickListener {
            DateCalendarPickerBottomSheetDialogFragment.newInstance(object : DateCalendarPickerBottomSheetDialogFragment.OnDateSetListener {

                override fun onDateSet(calendar: BaseCalendar) {
                    longToast(calendar.longDateString)
                }
            }, CalendarFactory.newInstance(CalendarType.CIVIL)).show(supportFragmentManager)
        }

        persianButton.setOnClickListener {
            DateCalendarPickerBottomSheetDialogFragment.newInstance(object : DateCalendarPickerBottomSheetDialogFragment.OnDateSetListener {

                override fun onDateSet(calendar: BaseCalendar) {
                    longToast(calendar.longDateString)
                }
            }, CalendarFactory.newInstance(CalendarType.PERSIAN)).show(supportFragmentManager)
        }

        hijriButton.setOnClickListener {
            DateCalendarPickerBottomSheetDialogFragment.newInstance(object : DateCalendarPickerBottomSheetDialogFragment.OnDateSetListener {

                override fun onDateSet(calendar: BaseCalendar) {
                    longToast(calendar.longDateString)
                }
            }, CalendarFactory.newInstance(CalendarType.HIJRI)).show(supportFragmentManager)
        }

        monthViewButton.setOnClickListener {
            startActivity(Intent(this, MonthViewActivity::class.java))
        }

        calendarViewButton.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }

        calendarViewButton.performClick()
//        monthViewButton.performClick()
    }

}
