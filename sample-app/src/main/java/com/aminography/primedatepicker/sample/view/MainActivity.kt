package com.aminography.primedatepicker.sample.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.fragment.DatePickerBottomSheetDialogFragment
import com.aminography.primedatepicker.sample.FONT_PATH_CIVIL
import com.aminography.primedatepicker.sample.FONT_PATH_HIJRI
import com.aminography.primedatepicker.sample.FONT_PATH_PERSIAN
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.longToast
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        civilButton.setOnClickListener {
//            val today = CalendarFactory.newInstance(CalendarType.CIVIL)
//            DatePickerBottomSheetDialogFragment.newInstance(
//                    today,
//                    pickType = PickType.SINGLE,
//                    pickedSingleDayCalendar = today
//            ).apply {
//                registerOnDateSetListener(object : DatePickerBottomSheetDialogFragment.OnDayPickedListener {
//
//                    override fun onSingleDayPicked(calendar: BaseCalendar) {
//                        longToast(calendar.longDateString)
//                    }
//                })
//                show(supportFragmentManager)
//            }
//        }
//
//        persianButton.setOnClickListener {
//            val today = CalendarFactory.newInstance(CalendarType.PERSIAN)
//            DatePickerBottomSheetDialogFragment.newInstance(
//                    today,
//                    pickType = PickType.SINGLE,
//                    pickedSingleDayCalendar = today
//            ).apply {
//                registerOnDateSetListener(object : DatePickerBottomSheetDialogFragment.OnDayPickedListener {
//
//                    override fun onSingleDayPicked(calendar: BaseCalendar) {
//                        longToast(calendar.longDateString)
//                    }
//                })
//                show(supportFragmentManager)
//            }
//        }
//
//        hijriButton.setOnClickListener {
//            val today = CalendarFactory.newInstance(CalendarType.HIJRI)
//            DatePickerBottomSheetDialogFragment.newInstance(
//                    today,
//                    pickType = PickType.SINGLE,
//                    pickedSingleDayCalendar = today
//            ).apply {
//                registerOnDateSetListener(object : DatePickerBottomSheetDialogFragment.OnDayPickedListener {
//
//                    override fun onSingleDayPicked(calendar: BaseCalendar) {
//                        longToast(calendar.longDateString)
//                    }
//                })
//                show(supportFragmentManager)
//            }
//        }

        showDatePickerButton.setOnClickListener {
            val calendarType = when {
                civilRadioButton.isChecked -> CalendarType.CIVIL
                persianRadioButton.isChecked -> CalendarType.PERSIAN
                hijriRadioButton.isChecked -> CalendarType.HIJRI
                else -> CalendarType.CIVIL
            }

            val pickType = when {
                singleRadioButton.isChecked -> PickType.SINGLE
                rangeRadioButton.isChecked -> PickType.START_RANGE
                else -> PickType.SINGLE
            }

            val minDateCalendar: BaseCalendar?
            if (minDateCheckBox.isChecked) {
                minDateCalendar = CalendarFactory.newInstance(calendarType)
                minDateCalendar.add(Calendar.MONTH, -5)
            } else {
                minDateCalendar = null
            }

            val maxDateCalendar: BaseCalendar?
            if (maxDateCheckBox.isChecked) {
                maxDateCalendar = CalendarFactory.newInstance(calendarType)
                maxDateCalendar.add(Calendar.MONTH, +5)
            } else {
                maxDateCalendar = null
            }

            val typeface = when (calendarType) {
                CalendarType.CIVIL -> FONT_PATH_CIVIL
                CalendarType.PERSIAN -> FONT_PATH_PERSIAN
                CalendarType.HIJRI -> FONT_PATH_HIJRI
            }

            val today = CalendarFactory.newInstance(calendarType)

            DatePickerBottomSheetDialogFragment.newInstance(
                    currentDateCalendar = today,
                    minDateCalendar = minDateCalendar,
                    maxDateCalendar = maxDateCalendar,
                    pickType = pickType,
                    typefacePath = typeface
            ).apply {
                registerOnDateSetListener(object : DatePickerBottomSheetDialogFragment.OnDayPickedListener {
                    override fun onSingleDayPicked(singleDay: BaseCalendar) {
                        longToast(singleDay.longDateString)
                    }

                    override fun onRangeDaysPicked(startDay: BaseCalendar, endDay: BaseCalendar) {
                        longToast("From: ${startDay.longDateString}\nTo: ${endDay.longDateString}")
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
