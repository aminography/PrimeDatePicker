package com.aminography.primedatepicker.sample.view

import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.sample.FONT_PATH_ARABIC
import com.aminography.primedatepicker.sample.FONT_PATH_PERSIAN
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_month_view.*

class MonthViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_view)

        // TODO
        val calendarType = CalendarType.CIVIL

        val calendar = CalendarFactory.newInstance(calendarType)
        monthView.setDate(calendar)

        val typeface: Typeface? = when (calendarType) {
            CalendarType.CIVIL -> null
            CalendarType.PERSIAN -> Typeface.createFromAsset(assets, FONT_PATH_PERSIAN)
            CalendarType.HIJRI -> Typeface.createFromAsset(assets, FONT_PATH_ARABIC)
        }

        monthView.fontTypeface = typeface

        val minDateCalendar = CalendarFactory.newInstance(calendarType)
        minDateCalendar.dayOfMonth = 4
        monthView.minDateCalendar = minDateCalendar

        val maxDateCalendar = CalendarFactory.newInstance(calendarType)
        maxDateCalendar.dayOfMonth = 28
        monthView.maxDateCalendar = maxDateCalendar

        selectSingleDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                monthView.pickType = PickType.SINGLE
            }
        }

        selectStartDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                monthView.pickType = PickType.START_RANGE
            }
        }

        selectEndDateRadioButton.setOnCheckedChangeListener { button, isChecked ->
            if (button.isPressed && isChecked) {
                monthView.pickType = PickType.END_RANGE
            }
        }
    }

}

