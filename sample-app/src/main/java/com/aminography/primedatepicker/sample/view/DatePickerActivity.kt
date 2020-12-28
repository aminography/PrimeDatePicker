package com.aminography.primedatepicker.sample.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.common.PickType
import com.aminography.primedatepicker.picker.PrimeDatePicker
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.DarkThemeFactory
import com.aminography.primedatepicker.picker.theme.LightThemeFactory
import com.aminography.primedatepicker.picker.theme.base.ThemeFactory
import com.aminography.primedatepicker.sample.*
import kotlinx.android.synthetic.main.activity_date_picker.*
import java.util.*

/**
 * @author aminography
 */
class DatePickerActivity : AppCompatActivity() {

    private var datePicker: PrimeDatePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)

        showDatePickerButton.setOnClickListener {

            val calendarType = getCalendarType()
            val locale = getLocale(calendarType)
            val pickType = getPickType()
            val minDateCalendar = getMinDateCalendar(calendarType)
            val maxDateCalendar = getMaxDateCalendar(calendarType)
            val typeface = getTypeface(calendarType)
            val theme = getDefaultTheme(typeface)

            val today = CalendarFactory.newInstance(calendarType, locale)

            datePicker = if (isBottomSheet()) {
                PrimeDatePicker.bottomSheetWith(today)
            } else {
                PrimeDatePicker.dialogWith(today)
            }.let {
                when (pickType) {
                    PickType.SINGLE -> {
                        it.pickSingleDay(singleDayPickCallback)
                    }
                    PickType.RANGE_START -> {
                        it.pickRangeDays(rangeDaysPickCallback)
                    }
                    PickType.MULTIPLE -> {
                        it.pickMultipleDays(multipleDaysPickCallback)
                    }
                    else -> null
                }
            }?.let {
                minDateCalendar?.let { minDate -> it.minPossibleDate(minDate) }
                maxDateCalendar?.let { maxDate -> it.maxPossibleDate(maxDate) }
                it.applyTheme(theme)
                it.build()
            }

            datePicker?.show(supportFragmentManager, PICKER_TAG)

            datePicker?.setOnDismissListener { datePicker = null }
        }
    }

    override fun onPause() {
        super.onPause()
        datePicker?.setDayPickCallback(null)
    }

    override fun onResume() {
        super.onResume()
        datePicker = supportFragmentManager.findFragmentByTag(PICKER_TAG) as? PrimeDatePicker
        datePicker?.let {
            when (it.pickType) {
                PickType.SINGLE -> {
                    it.setDayPickCallback(singleDayPickCallback)
                }
                PickType.RANGE_START, PickType.RANGE_END -> {
                    it.setDayPickCallback(rangeDaysPickCallback)
                }
                PickType.MULTIPLE -> {
                    it.setDayPickCallback(multipleDaysPickCallback)
                }
                else -> {
                }
            }
        }
    }

    private val singleDayPickCallback = SingleDayPickCallback { singleDay ->
        longToast(singleDay.longDateString)
    }

    private val rangeDaysPickCallback = RangeDaysPickCallback { startDay, endDay ->
        longToast("From: ${startDay.longDateString}\nTo: ${endDay.longDateString}")
    }

    private val multipleDaysPickCallback = MultipleDaysPickCallback { multipleDays ->
        longToast(multipleDays.joinToString(" -\n") { it.longDateString })
    }

    private fun getCalendarType(): CalendarType {
        return when {
            civilRadioButton.isChecked -> CalendarType.CIVIL
            persianRadioButton.isChecked -> CalendarType.PERSIAN
            hijriRadioButton.isChecked -> CalendarType.HIJRI
            japaneseRadioButton.isChecked -> CalendarType.JAPANESE
            else -> CalendarType.CIVIL
        }
    }

    private fun getPickType(): PickType {
        return when {
            singleRadioButton.isChecked -> PickType.SINGLE
            rangeRadioButton.isChecked -> PickType.RANGE_START
            multipleRadioButton.isChecked -> PickType.MULTIPLE
            else -> PickType.SINGLE
        }
    }

    private fun getLocale(calendarType: CalendarType): Locale {
        return when {
            calendarDefaultLocaleRadioButton.isChecked -> CalendarFactory.newInstance(calendarType).locale
            else -> Locale.ENGLISH
        }
    }

    private fun getDefaultTheme(typeface: String): ThemeFactory {
        return when {
            lightThemeRadioButton.isChecked -> object : LightThemeFactory() {
                override val typefacePath: String
                    get() = typeface

//                override val pickedDayBackgroundShapeType: BackgroundShapeType
//                    get() = BackgroundShapeType.ROUND_SQUARE
            }
            else -> object : DarkThemeFactory() {
                override val typefacePath: String
                    get() = typeface

//                override val calendarViewShowAdjacentMonthDays: Boolean
//                    get() = true
            }
        }
    }

    private fun getMinDateCalendar(calendarType: CalendarType): PrimeCalendar? {
        val minDateCalendar: PrimeCalendar?
        if (minDateCheckBox.isChecked) {
            minDateCalendar = CalendarFactory.newInstance(calendarType)
            minDateCalendar[Calendar.MONTH] -= 5
        } else {
            minDateCalendar = null
        }
        return minDateCalendar
    }

    private fun getMaxDateCalendar(calendarType: CalendarType): PrimeCalendar? {
        val maxDateCalendar: PrimeCalendar?
        if (maxDateCheckBox.isChecked) {
            maxDateCalendar = CalendarFactory.newInstance(calendarType)
            maxDateCalendar[Calendar.MONTH] += 5
        } else {
            maxDateCalendar = null
        }
        return maxDateCalendar
    }

    private fun isBottomSheet(): Boolean {
        return bottomSheetRadioButton.isChecked
    }

    private fun getTypeface(calendarType: CalendarType): String {
        return when (calendarType) {
            CalendarType.CIVIL -> FONT_PATH_CIVIL
            CalendarType.PERSIAN -> FONT_PATH_PERSIAN
            CalendarType.HIJRI -> FONT_PATH_HIJRI
            CalendarType.JAPANESE -> FONT_PATH_JAPANESE
        }
    }

    private fun longToast(text: String) =
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()

    companion object {
        const val PICKER_TAG = "PrimeDatePickerBottomSheet"
    }

}
