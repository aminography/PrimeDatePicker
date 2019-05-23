package com.aminography.primedatepicker

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.ColorInt
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
import java.util.*

/**
 * An adapter for a list of [BaseMonthView] items.
 */
abstract class BaseMonthListAdapter(
        private val context: Context,
        protected val controller: DatePickerController,
        @ColorInt val mainColor: Int? = null
) : BaseAdapter(), BaseMonthView.OnDayClickListener {

    /**
     * Updates the selected day and related parameters.
     *
     * @param day The day to highlight
     */
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

    override fun getItem(position: Int): Any? {
        return null
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    @SuppressLint("NewApi")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: BaseMonthView
        var drawingParams: HashMap<String, Int>? = null
        if (convertView != null) {
            v = convertView as BaseMonthView
            // We store the drawing parameters in the view so it can be recycled
            @Suppress("UNCHECKED_CAST")
            drawingParams = v.tag as HashMap<String, Int>
        } else {
            v = createMonthView(context)
            // Set up the new view
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            v.layoutParams = params
            v.isClickable = true
            v.setOnDayClickListener(this)
        }
        if (drawingParams == null) {
            drawingParams = HashMap()
        }
        drawingParams.clear()

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
        v.reuse()

        drawingParams[BaseMonthView.VIEW_PARAMS_SELECTED_DAY] = selectedDay
        drawingParams[BaseMonthView.VIEW_PARAMS_YEAR] = year
        drawingParams[BaseMonthView.VIEW_PARAMS_MONTH] = month
        drawingParams[BaseMonthView.VIEW_PARAMS_WEEK_START] = controller.firstDayOfWeek
        v.setMonthParams(drawingParams)
        v.invalidate()
        return v
    }

    abstract fun createMonthView(context: Context): BaseMonthView

    private fun isSelectedDayInMonth(year: Int, month: Int): Boolean {
        return selectedDay!!.year == year && selectedDay!!.month == month
    }

    override fun onDayClick(view: BaseMonthView, day: BaseCalendar) {
        controller.onDayOfMonthSelected(day.year, day.month, day.dayOfMonth)
        selectedDay = day
    }

    companion object {
        const val MONTHS_IN_YEAR = 12
    }

}
