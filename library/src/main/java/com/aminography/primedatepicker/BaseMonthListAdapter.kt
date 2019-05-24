package com.aminography.primedatepicker

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.LayoutParams
import android.widget.BaseAdapter
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.civil.CivilCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primecalendar.hijri.HijriCalendar
import com.aminography.primecalendar.persian.PersianCalendar
import com.aminography.primedatepicker.tools.CurrentCalendarType
import org.jetbrains.anko.dip
import java.util.*

/**
 * An adapter for a list of [MonthView] items.
 */
abstract class BaseMonthListAdapter(
        private val context: Context,
        protected val controller: DatePickerController
) : BaseAdapter(), MonthView.OnDayClickListener {

    var selectedDay: BaseCalendar? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    init {
        init()
        selectedDay = controller.selectedDay
    }

    /**
     * Set up the gesture detector and selected time
     */
    private fun init() {
        selectedDay = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> CivilCalendar()
            CalendarType.PERSIAN -> PersianCalendar()
            CalendarType.HIJRI -> HijriCalendar()
        }
    }

    override fun getCount(): Int {
        // Added by Amin ---------------------------------------------------------------------------
        val min = controller.minDate
        val max = controller.maxDate
        if (min != null && max != null) {
            var count = 1
            val calendar = CalendarFactory.newInstance(CurrentCalendarType.type)
            calendar.timeInMillis = min.timeInMillis
            while (calendar.timeInMillis < max.timeInMillis) {
                calendar.add(Calendar.MONTH, 1)
                count++
            }
            return count
        }
        // Added by Amin ---------------------------------------------------------------------------

        return (controller.maxYear - controller.minYear + 1) * MONTHS_IN_YEAR
    }

    override fun getItem(position: Int): Any? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

    @SuppressLint("NewApi")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val monthView: MonthView
        if (convertView != null) {
            monthView = convertView as MonthView
            @Suppress("UNCHECKED_CAST")
            (monthView.tag as HashMap<String, Int>).clear()
        } else {
            monthView = createMonthView(context).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
                isClickable = true
                setOnDayClickListener(this@BaseMonthListAdapter)
            }
        }

        var month = position % MONTHS_IN_YEAR
        var year = position / MONTHS_IN_YEAR + controller.minYear

        // Added by Amin ---------------------------------------------------------------------------
        val min = controller.minDate
        val max = controller.maxDate
        if (min != null && max != null) {
            val index = position + min.month
            month = index % MONTHS_IN_YEAR
            year = index / MONTHS_IN_YEAR + controller.minYear
        }
        // Added by Amin ---------------------------------------------------------------------------

        var selectedDay = -1
        if (isSelectedDayInMonth(year, month)) {
            selectedDay = this.selectedDay!!.dayOfMonth
        }

        // Invokes requestLayout() to ensure that the recycled view is set with the appropriate
        // height/number of weeks before being displayed.
        monthView.reuse()

        val drawingParams = HashMap<String, Int>()
        drawingParams[MonthView.VIEW_PARAMS_SELECTED_DAY] = selectedDay
        drawingParams[MonthView.VIEW_PARAMS_YEAR] = year
        drawingParams[MonthView.VIEW_PARAMS_MONTH] = month
        monthView.setMonthParams(drawingParams)
        monthView.invalidate()
        return monthView
    }

    abstract fun createMonthView(context: Context): MonthView

    private fun isSelectedDayInMonth(year: Int, month: Int): Boolean {
        return selectedDay!!.year == year && selectedDay!!.month == month
    }

    override fun onDayClick(view: MonthView, day: BaseCalendar) {
        controller.onDayOfMonthSelected(day.year, day.month, day.dayOfMonth)
        selectedDay = day
    }

    companion object {
        const val MONTHS_IN_YEAR = 12
    }

}
