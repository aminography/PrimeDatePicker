package com.aminography.primedatepicker.sample.view

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aminography.primecalendar.PrimeCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.PickType
import com.aminography.primedatepicker.picker.PrimeDatePickerBottomSheet
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback
import com.aminography.primedatepicker.picker.theme.DarkThemeFactory
import com.aminography.primedatepicker.sample.*
import kotlinx.android.synthetic.main.activity_date_picker.*
import java.util.*


class DatePickerActivity : AppCompatActivity() {

    private var datePicker: PrimeDatePickerBottomSheet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_date_picker)

        showDatePickerButton.setOnClickListener {

            val calendarType = getCalendarType()
            val pickType = getPickType()
            val minDateCalendar = getMinDateCalendar(calendarType)
            val maxDateCalendar = getMaxDateCalendar(calendarType)
            val typeface = getTypeface(calendarType)

            val theme = object : DarkThemeFactory() {
                override val typefacePath: String?
                    get() = typeface
            }

            val today = CalendarFactory.newInstance(calendarType)

            datePicker = when (pickType) {
                PickType.SINGLE -> {
                    PrimeDatePickerBottomSheet.from(today)
                        .pickSingleDay(singleDayPickCallback)
                        .minPossibleDate(minDateCalendar)
                        .maxPossibleDate(maxDateCalendar)
                        .applyTheme(theme)
                        .build()
                }
                PickType.RANGE_START -> {
                    PrimeDatePickerBottomSheet.from(today)
                        .pickRangeDays(rangeDaysPickCallback)
                        .minPossibleDate(minDateCalendar)
                        .maxPossibleDate(maxDateCalendar)
                        .applyTheme(theme)
                        .build()
                }
                PickType.MULTIPLE -> {
                    PrimeDatePickerBottomSheet.from(today)
                        .pickMultipleDays(multipleDaysPickCallback)
                        .minPossibleDate(minDateCalendar)
                        .maxPossibleDate(maxDateCalendar)
                        .applyTheme(theme)
                        .build()
                }
                else -> null
            }

            datePicker?.show(supportFragmentManager, PICKER_TAG)
        }
    }

    override fun onPause() {
        super.onPause()
        datePicker?.setDayPickCallback(null)
    }

    override fun onResume() {
        super.onResume()
        datePicker = supportFragmentManager.findFragmentByTag(PICKER_TAG) as? PrimeDatePickerBottomSheet
        when (datePicker?.pickType) {
            PickType.SINGLE -> {
                datePicker?.setDayPickCallback(singleDayPickCallback)
            }
            PickType.RANGE_START, PickType.RANGE_END -> {
                datePicker?.setDayPickCallback(rangeDaysPickCallback)
            }
            PickType.MULTIPLE -> {
                datePicker?.setDayPickCallback(multipleDaysPickCallback)
            }
            else -> {
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
