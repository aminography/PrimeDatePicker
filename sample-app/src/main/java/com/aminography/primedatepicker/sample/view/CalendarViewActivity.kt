package com.aminography.primedatepicker.sample.view

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.sample.FONT_PATH_ARABIC
import com.aminography.primedatepicker.sample.FONT_PATH_PERSIAN
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_calendar_view.*
import kotlinx.android.synthetic.main.nav_drawer_calendar.view.*
import org.jetbrains.anko.toast
import java.util.*

@SuppressLint("SetTextI18n")
class CalendarViewActivity : AppCompatActivity(), OnDayPickedListener {

    private lateinit var navigationLayout: View
    private var calendarType = CalendarType.CIVIL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_view)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        openDrawer()

        navigationLayout = navigationView.getHeaderView(0)
        with(navigationLayout) {
            calendarType = when {
                civilRadioButton.isChecked -> CalendarType.CIVIL
                persianRadioButton.isChecked -> CalendarType.PERSIAN
                hijriRadioButton.isChecked -> CalendarType.HIJRI
                else -> CalendarType.CIVIL
            }
            endRangeRadioButton.isEnabled = false
        }

        calendarView.onDayPickedListener = this
        calendarView.calendarType = calendarType

        initCalendarTypeSection()
        initPickerTypeSection()
        initDateBoundarySection()
        initSetToSection()
    }

    private fun initCalendarTypeSection() {
        with(navigationLayout) {
            civilRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.CIVIL
                    calendarView.calendarType = calendarType

                    restoreDefaults(calendarType)
                }
            }
            persianRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.PERSIAN
                    calendarView.calendarType = calendarType

                    restoreDefaults(calendarType)
                }
            }
            hijriRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.HIJRI
                    calendarView.calendarType = calendarType

                    restoreDefaults(calendarType)
                }
            }
        }
    }

    private fun initPickerTypeSection() {
        with(navigationLayout) {
            singleRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.pickType = PickType.SINGLE
                }
            }
            startRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.pickType = PickType.START_RANGE
                }
            }
            endRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.pickType = PickType.END_RANGE
                }
            }
        }
    }

    private fun initDateBoundarySection() {
        with(navigationLayout) {
            minDateCheckBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    closeDrawer()
//                    if (isChecked) {
//                        calendarView.flingOrientation = PrimeCalendarView.FlingOrientation.HORIZONTAL
//                    }else{
//                        calendarView.flingOrientation = PrimeCalendarView.FlingOrientation.VERTICAL
//                    }
                    if (isChecked) {
                        val calendar = CalendarFactory.newInstance(calendarType)
                        calendar.add(Calendar.MONTH, -5)
                        calendarView.minDateCalendar = calendar
                    } else {
                        calendarView.minDateCalendar = null
                    }
                }
            }
            maxDateCheckBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    closeDrawer()
                    if (isChecked) {
                        val calendar = CalendarFactory.newInstance(calendarType)
                        calendar.add(Calendar.MONTH, 5)
                        calendarView.maxDateCalendar = calendar
                    } else {
                        calendarView.maxDateCalendar = null
                    }
                }
            }
        }
    }

    private fun initSetToSection() {
        with(navigationLayout) {
            gotoPastTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar.add(Calendar.MONTH, -7)
                val result = calendarView.goto(calendar, true)
                if (!result) {
                    toast("Target date is out of specified feasible range!")
                }
            }
            gotoNowTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                val result = calendarView.goto(calendar, true)
                if (!result) {
                    toast("Target date is out of specified feasible range!")
                }
            }
            gotoFutureTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar.add(Calendar.MONTH, 7)
                val result = calendarView.goto(calendar, true)
                if (!result) {
                    toast("Target date is out of specified feasible range!")
                }
            }
        }
    }

    private fun restoreDefaults(calendarType: CalendarType) {
        pickedTextView.visibility = View.INVISIBLE
        pickedTextView.text = ""
        with(navigationLayout) {
            minDateCheckBox.isChecked = false
            maxDateCheckBox.isChecked = false
            singleRadioButton.isChecked = false
            startRangeRadioButton.isChecked = false
            endRangeRadioButton.isChecked = false
            val today = CalendarFactory.newInstance(calendarType)
            minDateCheckBox.text = "Min Date: ${today.monthName} 5"
            maxDateCheckBox.text = "Max Date: ${today.monthName} 25"

            endRangeRadioButton.isEnabled = false

            calendarView.pickedSingleDayCalendar = null
            calendarView.pickedStartRangeCalendar = null
            calendarView.pickedEndRangeCalendar = null
            calendarView.pickType = PickType.NOTHING
            calendarView.minDateCalendar = null
            calendarView.maxDateCalendar = null

            calendarView.fontTypeface = when (calendarType) {
                CalendarType.CIVIL -> null
                CalendarType.PERSIAN -> Typeface.createFromAsset(assets, FONT_PATH_PERSIAN)
                CalendarType.HIJRI -> Typeface.createFromAsset(assets, FONT_PATH_ARABIC)
            }
        }
    }

    private fun openDrawer() = drawerLayout.openDrawer(GravityCompat.START)

    private fun closeDrawer() = drawerLayout.closeDrawer(GravityCompat.START)

    override fun onDayPicked(pickType: PickType, singleDay: BaseCalendar?, startDay: BaseCalendar?, endDay: BaseCalendar?) {
        with(navigationLayout) {
            endRangeRadioButton.isEnabled = false
            pickedTextView.text = ""
            when (pickType) {
                PickType.SINGLE -> {
                    calendarView.pickedSingleDayCalendar?.apply {
                        pickedTextView.visibility = View.VISIBLE
                        pickedTextView.text = "Single Day: $longDateString"
                    }
                }
                PickType.START_RANGE, PickType.END_RANGE -> {
                    calendarView.pickedStartRangeCalendar?.let { start ->
                        endRangeRadioButton.isEnabled = true
                        pickedTextView.visibility = View.VISIBLE
                        var text = "Start Range Day: ${start.longDateString}"
                        calendarView.pickedEndRangeCalendar?.let { end ->
                            text += "\n"
                            text += "End Range Day: ${end.longDateString}"
                        }
                        pickedTextView.text = text
                    }
                }
                PickType.NOTHING -> {
                    pickedTextView.visibility = View.INVISIBLE
                }
            }
        }
    }

}
