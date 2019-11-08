package com.aminography.primedatepicker.sample.view

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.SeekBar
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.OnDayPickedListener
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.sample.*
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
                japaneseRadioButton.isChecked -> CalendarType.JAPANESE
                else -> CalendarType.CIVIL
            }
            endRangeRadioButton.isEnabled = false
        }

        calendarView.onDayPickedListener = this
        calendarView.calendarType = calendarType

        initTypeface()
        initCalendarTypeSection()
        initPickerTypeSection()
        initDateBoundarySection()
        initSetToSection()
        initFlingOrientationSection()
        initLocaleSection()
        initTransitionSpeedFactorSection()
        initMaxTransitionLengthSection()

        titleTextView.setOnClickListener {
            calendarView.transitionSpeedFactor = 1.3f
        }

//        calendarView.animateSelection = true
//        calendarView.animationDuration = 1000
//        calendarView.animationInterpolator = AccelerateInterpolator()
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
        calendarView.typeface = when (calendarType) {
            CalendarType.CIVIL -> Typeface.createFromAsset(assets, FONT_PATH_CIVIL)
            CalendarType.PERSIAN -> Typeface.createFromAsset(assets, FONT_PATH_PERSIAN)
            CalendarType.HIJRI -> Typeface.createFromAsset(assets, FONT_PATH_HIJRI)
            CalendarType.JAPANESE -> Typeface.createFromAsset(assets, FONT_PATH_JAPANESE)
        }
    }

    private fun initCalendarTypeSection() {
        with(navigationLayout) {
            civilRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.CIVIL
//                    calendarView.calendarType = calendarType
//                    calendarView.locale = Locale.ENGLISH
                    calendarView.goto(CalendarFactory.newInstance(calendarType), false)

                    restoreDefaults()
                }
            }
            persianRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.PERSIAN
//                    calendarView.calendarType = calendarType
//                    calendarView.locale = Locale("fa")
                    calendarView.goto(CalendarFactory.newInstance(calendarType), false)

                    restoreDefaults()
                }
            }
            hijriRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.HIJRI
//                    calendarView.calendarType = calendarType
//                    calendarView.locale = Locale("ar")
                    calendarView.goto(CalendarFactory.newInstance(calendarType), false)

                    restoreDefaults()
                }
            }
            japaneseRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarType = CalendarType.JAPANESE
//                    calendarView.calendarType = calendarType
//                    calendarView.locale = Locale("ja")
                    calendarView.goto(CalendarFactory.newInstance(calendarType), false)

                    restoreDefaults()
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
                    calendarView.pickType = PickType.RANGE_START
                }
            }
            endRangeRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.pickType = PickType.RANGE_END
                }
            }
        }
    }

    private fun initDateBoundarySection() {
        with(navigationLayout) {
            minDateCheckBox.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed) {
                    closeDrawer()
                    if (isChecked) {
                        val calendar = CalendarFactory.newInstance(calendarType)
                        calendar[Calendar.MONTH] -= 5
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
                        calendar[Calendar.MONTH] += 5
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
                calendar[Calendar.MONTH] -= 7
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
                calendar[Calendar.MONTH] += 7
                val result = calendarView.goto(calendar, true)
                if (!result) {
                    toast("Target date is out of specified feasible range!")
                }
            }
        }
    }

    private fun initFlingOrientationSection() {
        with(navigationLayout) {
            verticalFlingRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.flingOrientation = PrimeCalendarView.FlingOrientation.VERTICAL
                }
            }
            horizontalFlingRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.flingOrientation = PrimeCalendarView.FlingOrientation.HORIZONTAL
                }
            }
        }
    }

    private fun initLocaleSection() {
        with(navigationLayout) {
            calendarDefaultLocaleRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.locale = CalendarFactory.newInstance(calendarType).locale
                }
            }
            englishLocaleRadioButton.setOnCheckedChangeListener { button, isChecked ->
                if (button.isPressed && isChecked) {
                    closeDrawer()
                    calendarView.locale = Locale.ENGLISH
                }
            }
        }
    }

    private fun initTransitionSpeedFactorSection() {
        with(navigationLayout) {
            val min = resources.getString(com.aminography.primedatepicker.R.string.defaultTransitionSpeedFactor).toFloat()
            val max = 1000
            factorTextView.text = String.format("%.02f", calendarView.transitionSpeedFactor)
            factorSeekBar.max = max
            factorSeekBar.progress = (calendarView.transitionSpeedFactor / max).toInt()
            factorSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(SeekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    val p = (progress.toDouble() * (10 - min) / max).toFloat()
                    factorTextView.text = String.format("%.02f", p + min)
                    calendarView.transitionSpeedFactor = p + min
                }

                override fun onStartTrackingTouch(SeekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(SeekBar: SeekBar?) {
                }
            })
        }
    }

    private fun initMaxTransitionLengthSection() {
        with(navigationLayout) {
            val max = 10
            transitionLengthTextView.text = "${calendarView.maxTransitionLength}"
            transitionLengthSeekBar.max = max
            transitionLengthSeekBar.progress = calendarView.maxTransitionLength
            transitionLengthSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

                override fun onProgressChanged(SeekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    transitionLengthTextView.text = "$progress"
                    calendarView.maxTransitionLength = progress
                }

                override fun onStartTrackingTouch(SeekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(SeekBar: SeekBar?) {
                }
            })
        }
    }

    private fun restoreDefaults() {
        pickedTextView.visibility = View.INVISIBLE
        pickedTextView.text = ""
        with(navigationLayout) {
            minDateCheckBox.isChecked = false
            maxDateCheckBox.isChecked = false
            singleRadioButton.isChecked = false
            startRangeRadioButton.isChecked = false
            endRangeRadioButton.isChecked = false
            calendarDefaultLocaleRadioButton.isChecked = true

            factorTextView.text = String.format("%.02f", calendarView.transitionSpeedFactor)
            factorSeekBar.progress = (calendarView.transitionSpeedFactor / 1000).toInt()

            transitionLengthTextView.text = "${calendarView.maxTransitionLength}"
            transitionLengthSeekBar.progress = calendarView.maxTransitionLength

            calendarView.invalidateAfter {
                calendarView.pickedSingleDayCalendar = null
                calendarView.pickedRangeStartCalendar = null
                calendarView.pickedRangeEndCalendar = null
                calendarView.pickType = PickType.NOTHING
                calendarView.minDateCalendar = null
                calendarView.maxDateCalendar = null
            }

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
                    calendarView.pickedSingleDayCalendar?.apply {
                        pickedTextView.visibility = View.VISIBLE
                        pickedTextView.text = "Single Day: $longDateString"
                    }
                }
                PickType.RANGE_START, PickType.RANGE_END -> {
                    calendarView.pickedRangeStartCalendar?.let { start ->
                        endRangeRadioButton.isEnabled = true
                        pickedTextView.visibility = View.VISIBLE
                        var text = "Start Range Day: ${start.longDateString}"
                        calendarView.pickedRangeEndCalendar?.let { end ->
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
