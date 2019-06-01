package com.aminography.primedatepicker.sample.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.fragment.DatePickerBottomSheetDialogFragment
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        civilButton.setOnClickListener {
            val today = CalendarFactory.newInstance(CalendarType.CIVIL)
            DatePickerBottomSheetDialogFragment.newInstance(
                    today,
                    pickType = PickType.SINGLE,
                    pickedSingleDayCalendar = today
            ).apply {
                registerOnDateSetListener(object : DatePickerBottomSheetDialogFragment.OnDateSetListener {

                    override fun onDateSet(calendar: BaseCalendar) {
                        longToast(calendar.longDateString)
                    }
                })
                show(supportFragmentManager)
            }
        }

        persianButton.setOnClickListener {
            val today = CalendarFactory.newInstance(CalendarType.PERSIAN)
            DatePickerBottomSheetDialogFragment.newInstance(
                    today,
                    pickType = PickType.SINGLE,
                    pickedSingleDayCalendar = today
            ).apply {
                registerOnDateSetListener(object : DatePickerBottomSheetDialogFragment.OnDateSetListener {

                    override fun onDateSet(calendar: BaseCalendar) {
                        longToast(calendar.longDateString)
                    }
                })
                show(supportFragmentManager)
            }
        }

        hijriButton.setOnClickListener {
            val today = CalendarFactory.newInstance(CalendarType.HIJRI)
            DatePickerBottomSheetDialogFragment.newInstance(
                    today,
                    pickType = PickType.SINGLE,
                    pickedSingleDayCalendar = today
            ).apply {
                registerOnDateSetListener(object : DatePickerBottomSheetDialogFragment.OnDateSetListener {

                    override fun onDateSet(calendar: BaseCalendar) {
                        longToast(calendar.longDateString)
                    }
                })
                show(supportFragmentManager)
            }
        }

        monthViewButton.setOnClickListener {
            startActivity(Intent(this, MonthViewActivity::class.java))
        }

        calendarViewButton.setOnClickListener {
            startActivity(Intent(this, CalendarViewActivity::class.java))
        }

//        calendarViewButton.performClick()
//        monthViewButton.performClick()
    }

}
