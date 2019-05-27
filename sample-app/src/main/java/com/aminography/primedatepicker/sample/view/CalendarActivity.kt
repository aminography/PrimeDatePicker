package com.aminography.primedatepicker.sample.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.aminography.primedatepicker.sample.R
import com.aminography.primedatepicker.tools.Utils
import kotlinx.android.synthetic.main.activity_calendar.*

class CalendarActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)

        nowButton.setOnClickListener {
            val calendar = Utils.newCalendar()
//            calendarView.goto(calendar.year, 1, true)
            calendarView.goto(calendar.year, calendar.month, true)
        }

        gotoPastButton.setOnClickListener {
//            calendarView.goto(2018, 11, true)
            calendarView.goto(2014, 9, true)
        }
        gotoFutureButton.setOnClickListener {
//            calendarView.goto(2019, 3, true)
            calendarView.goto(2022, 1, true)
        }

    }
}
