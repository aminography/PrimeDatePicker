package com.aminography.primedatepicker.sample.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.sample.R
import com.aminography.primedatepicker.tools.Utils
import kotlinx.android.synthetic.main.activity_calendar.*
import java.util.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        nowButton.setOnClickListener {
            val calendar = Utils.newCalendar()
            calendarView.goto(calendar.year, calendar.month, true)
        }

        gotoPastButton.setOnClickListener {
            val calendar = Utils.newCalendar()
            calendar.month = 1
            calendarView.goto(calendar.year, calendar.month, true)
        }
        gotoFutureButton.setOnClickListener {
            val calendar = Utils.newCalendar()
            calendar.month = 11
            calendarView.goto(calendar.year, calendar.month, true)
        }

        setMinDateButton.setOnClickListener {
            val minDateCalendar = Utils.newCalendar()
            minDateCalendar.dayOfMonth = 10
            minDateCalendar.add(Calendar.MONTH, -3)
            calendarView.minDateCalendar = minDateCalendar
        }

        setMaxDateButton.setOnClickListener {
            val maxDateCalendar = Utils.newCalendar()
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
