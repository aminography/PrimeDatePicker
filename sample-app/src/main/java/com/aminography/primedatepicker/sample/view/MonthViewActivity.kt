package com.aminography.primedatepicker.sample.view

import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.sample.FONT_PATH_ARABIC
import com.aminography.primedatepicker.sample.FONT_PATH_PERSIAN
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_month_view.*
import kotlinx.android.synthetic.main.nav_drawer_month.view.*
import java.util.*

class MonthViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_view)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        openDrawer()

        var calendarType = CalendarType.CIVIL
        monthView.setDate(CalendarFactory.newInstance(calendarType))

        navigationView.getHeaderView(0)?.apply {

            civilRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.CIVIL
                    monthView.calendarType = calendarType

//                    minDateCheckBox.isChecked = false
//                    maxDateCheckBox.isChecked = false
//                    singleRadioButton.isChecked = false
//                    startRangeRadioButton.isChecked = false
//                    endRangeRadioButton.isChecked = false
//                    val today = CalendarFactory.newInstance(calendarType)
//                    minDateCheckBox.text = "Min Date: ${today.monthName} 5"
//                    maxDateCheckBox.text = "Max Date: ${today.monthName} 25"
                }
            }
            persianRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.PERSIAN
                    monthView.calendarType = calendarType

//                    minDateCheckBox.isChecked = false
//                    maxDateCheckBox.isChecked = false
//                    singleRadioButton.isChecked = false
//                    startRangeRadioButton.isChecked = false
//                    endRangeRadioButton.isChecked = false
//                    val today = CalendarFactory.newInstance(calendarType)
//                    minDateCheckBox.text = "Min Date: ${today.monthName} 5"
//                    maxDateCheckBox.text = "Max Date: ${today.monthName} 25"
                }
            }
            hijriRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.HIJRI
                    monthView.calendarType = calendarType

//                    minDateCheckBox.isChecked = false
//                    maxDateCheckBox.isChecked = false
//                    singleRadioButton.isChecked = false
//                    startRangeRadioButton.isChecked = false
//                    endRangeRadioButton.isChecked = false
//                    val today = CalendarFactory.newInstance(calendarType)
//                    minDateCheckBox.text = "Min Date: ${today.monthName} 5"
//                    maxDateCheckBox.text = "Max Date: ${today.monthName} 25"
                }
            }
            //--------------------------------------------------------------------------------------
            singleRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    monthView.pickType = PickType.SINGLE
                }
            }
            startRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    monthView.pickType = PickType.START_RANGE
                }
            }
            endRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    monthView.pickType = PickType.END_RANGE
                }
            }
            //--------------------------------------------------------------------------------------
            val today = CalendarFactory.newInstance(calendarType)
            minDateCheckBox.text = "Min Date: ${today.monthName} 5"
            minDateCheckBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    closeDrawer()
                    if (isChecked) {
                        val calendar = CalendarFactory.newInstance(calendarType)
                        calendar.dayOfMonth = 5
                        monthView.minDateCalendar = calendar
                    } else {
                        monthView.minDateCalendar = null
                    }
                }
            }
            maxDateCheckBox.text = "Max Date: ${today.monthName} 25"
            maxDateCheckBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    closeDrawer()
                    if (isChecked) {
                        val calendar = CalendarFactory.newInstance(calendarType)
                        calendar.dayOfMonth = 25
                        monthView.maxDateCalendar = calendar
                    } else {
                        monthView.maxDateCalendar = null
                    }
                }
            }
            //--------------------------------------------------------------------------------------
            setToPastTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar.add(Calendar.MONTH, -7)
                monthView.setDate(calendar)
            }
            setToNowTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                monthView.setDate(calendar)
            }
            setToFutureTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar.add(Calendar.MONTH, 7)
                monthView.setDate(calendar)
            }
        }

        val typeface: Typeface? = when (calendarType) {
            CalendarType.CIVIL -> null
            CalendarType.PERSIAN -> Typeface.createFromAsset(assets, FONT_PATH_PERSIAN)
            CalendarType.HIJRI -> Typeface.createFromAsset(assets, FONT_PATH_ARABIC)
        }

        monthView.fontTypeface = typeface
    }

    private fun openDrawer() = drawerLayout.openDrawer(GravityCompat.START)

    private fun closeDrawer() = drawerLayout.closeDrawer(GravityCompat.START)

}

