package com.aminography.primedatepicker.sample.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aminography.primedatepicker.MyMonthView
import com.aminography.primedatepicker.sample.R
import com.aminography.primedatepicker.tools.Utils
import kotlinx.android.synthetic.main.activity_month_view.*

class MonthViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_view)

        val calendar = Utils.newCalendar()
        monthView.setDate(calendar.year, calendar.month)

        val minDateCalendar = Utils.newCalendar()
        minDateCalendar.dayOfMonth = 7
        monthView.minDateCalendar = minDateCalendar

        val maxDateCalendar = Utils.newCalendar()
        maxDateCalendar.dayOfMonth = 23
        monthView.maxDateCalendar = maxDateCalendar

        selectedDayButton.setOnClickListener {
            monthView.selectedDay = 15
        }

        selectSingleDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                monthView.selectType = MyMonthView.SelectType.SINGLE
            }
        }

        selectStartDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                monthView.selectType = MyMonthView.SelectType.START_RANGE
            }
        }

        selectEndDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                monthView.selectType = MyMonthView.SelectType.END_RANGE
            }
        }
    }
}
