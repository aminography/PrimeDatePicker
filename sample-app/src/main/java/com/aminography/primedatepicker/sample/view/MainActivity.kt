package com.aminography.primedatepicker.sample.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        datePickerButton.setOnClickListener {
            startActivity(Intent(this, DatePickerActivity::class.java))
        }

        monthViewButton.setOnClickListener {
            startActivity(Intent(this, MonthViewActivity::class.java))
        }

        calendarViewButton.setOnClickListener {
            startActivity(Intent(this, CalendarViewActivity::class.java))
        }
    }

}
