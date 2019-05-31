package com.aminography.primedatepicker.sample.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        // TODO
        val calendarType = CalendarType.CIVIL
        calendarView.calendarType = calendarType
//        val calendar = CalendarFactory.newInstance(calendarType)
//        calendarView.goto(calendar.year, calendar.month, true)



        nowButton.setOnClickListener {
            val calendar = CalendarFactory.newInstance(calendarType)
            calendarView.goto(calendar.year, calendar.month, true)
        }

        gotoPastButton.setOnClickListener {
            val calendar = CalendarFactory.newInstance(calendarType)
            calendar.month = 1
            calendarView.goto(calendar.year, calendar.month, true)
        }
        gotoFutureButton.setOnClickListener {
            val calendar = CalendarFactory.newInstance(calendarType)
            calendar.month = 11
            calendarView.goto(calendar.year, calendar.month, true)
        }

        setMinDateButton.setOnClickListener {
            val minDateCalendar = CalendarFactory.newInstance(calendarType)
            minDateCalendar.dayOfMonth = 10
            minDateCalendar.add(Calendar.MONTH, -3)
            calendarView.minDateCalendar = minDateCalendar
        }

        setMaxDateButton.setOnClickListener {
            val maxDateCalendar = CalendarFactory.newInstance(calendarType)
            maxDateCalendar.dayOfMonth = 20
            maxDateCalendar.add(Calendar.MONTH, 3)
            calendarView.maxDateCalendar = maxDateCalendar
        }

        selectSingleDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                calendarView.pickType = PickType.SINGLE
            }
        }

        selectStartDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                calendarView.pickType = PickType.START_RANGE
            }
        }

        selectEndDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                calendarView.pickType = PickType.END_RANGE
            }
        }

    }
}
