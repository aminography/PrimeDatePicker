package com.aminography.primedatepicker.sample.view

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.sample.FONT_PATH_CIVIL
import com.aminography.primedatepicker.sample.FONT_PATH_HIJRI
import com.aminography.primedatepicker.sample.FONT_PATH_PERSIAN
import com.aminography.primedatepicker.sample.R
import kotlinx.android.synthetic.main.activity_month_view.*
import kotlinx.android.synthetic.main.nav_drawer_month.view.*
import org.jetbrains.anko.dip
import java.util.*

@SuppressLint("SetTextI18n")
class MonthViewActivity : AppCompatActivity(), OnDayPickedListener {

    private lateinit var navigationLayout: View
    private var calendarType = CalendarType.CIVIL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_month_view)
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

        monthView.onDayPickedListener = this
        monthView.goto(CalendarFactory.newInstance(calendarType))

        initTypeface()
        initCalendarTypeSection()
        initPickerTypeSection()
        initDateBoundarySection()
        initSetToSection()
        initLocaleSection()

        titleTextView.setOnClickListener {
            monthView.weekLabelTextSize = dip(24)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.apply {
            putInt("CALENDAR_TYPE", calendarType.ordinal)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState?.apply {
            calendarType = CalendarType.values()[getInt("CALENDAR_TYPE")]
        }
        initTypeface()
    }

    private fun initTypeface() {
        monthView.typeface = when (calendarType) {
            CalendarType.CIVIL -> Typeface.createFromAsset(assets, FONT_PATH_CIVIL)
            CalendarType.PERSIAN -> Typeface.createFromAsset(assets, FONT_PATH_PERSIAN)
            CalendarType.HIJRI -> Typeface.createFromAsset(assets, FONT_PATH_HIJRI)
        }
    }

    private fun initCalendarTypeSection() {
        with(navigationLayout) {
            civilRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.CIVIL
                    monthView.goto(CalendarFactory.newInstance(calendarType))

                    restoreDefaults(calendarType)
                }
            }
            persianRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.PERSIAN
                    monthView.goto(CalendarFactory.newInstance(calendarType))

                    restoreDefaults(calendarType)
                }
            }
            hijriRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.HIJRI
                    monthView.goto(CalendarFactory.newInstance(calendarType))

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
                    monthView.pickType = PickType.SINGLE
                }
            }
            startRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    monthView.pickType = PickType.RANGE_START
                }
            }
            endRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    monthView.pickType = PickType.RANGE_END
                }
            }
        }
    }

    private fun initDateBoundarySection() {
        with(navigationLayout) {
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
        }
    }

    private fun initSetToSection() {
        with(navigationLayout) {
            setToPastTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar[Calendar.MONTH] -= 7
                monthView.goto(calendar)
            }
            setToNowTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                monthView.goto(calendar)
            }
            setToFutureTextView.setOnClickListener {
                closeDrawer()
                val calendar = CalendarFactory.newInstance(calendarType)
                calendar[Calendar.MONTH] += 7
                monthView.goto(calendar)
            }
        }
    }

    private fun initLocaleSection() {
        with(navigationLayout) {
            calendarDefaultLocaleRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    monthView.locale = CalendarFactory.newInstance(calendarType).locale
                }
            }
            englishLocaleRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    monthView.locale = Locale.ENGLISH
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
            calendarDefaultLocaleRadioButton.isChecked = true

            endRangeRadioButton.isEnabled = false

            monthView.doNotInvalidate {
                monthView.pickedSingleDayCalendar = null
                monthView.pickedRangeStartCalendar = null
                monthView.pickedRangeEndCalendar = null
                monthView.pickType = PickType.NOTHING
                monthView.minDateCalendar = null
                monthView.maxDateCalendar = null
            }
            monthView.invalidate()

            initTypeface()
        }
    }

    private fun openDrawer() = drawerLayout.openDrawer(GravityCompat.START)

    private fun closeDrawer() = drawerLayout.closeDrawer(GravityCompat.START)

    override fun onDayPicked(pickType: PickType, singleDay: PrimeCalendar?, startDay: PrimeCalendar?, endDay: PrimeCalendar?) {
        with(navigationLayout) {
            endRangeRadioButton.isEnabled = false
            pickedTextView.text = ""
            when (pickType) {
                PickType.SINGLE -> {
                    monthView.pickedSingleDayCalendar?.apply {
                        pickedTextView.visibility = View.VISIBLE
                        pickedTextView.text = "Single Day: $longDateString"
                    }
                }
                PickType.RANGE_START, PickType.RANGE_END -> {
                    monthView.pickedRangeStartCalendar?.let { start ->
                        endRangeRadioButton.isEnabled = true
                        pickedTextView.visibility = View.VISIBLE
                        var text = "Start Range Day: ${start.longDateString}"
                        monthView.pickedRangeEndCalendar?.let { end ->
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

