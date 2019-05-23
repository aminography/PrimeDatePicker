package com.aminography.primedatepicker

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.ColorInt
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView.LayoutParams
import android.widget.BaseAdapter
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primedatepicker.tools.CurrentCalendarType
import java.util.*

/**
 * An adapter for a list of [MonthView] items.
 */
abstract class MonthAdapter(
        private val context: Context,
        protected val controller: DatePickerController,
        @ColorInt val mainColor: Int? = null
) : BaseAdapter(), MonthView.OnDayClickListener {

    private var mSelectedDay: CalendarDay? = null

    /**
     * Updates the selected day and related parameters.
     *
     * @param day The day to highlight
     */
    var selectedDay: CalendarDay?
        get() = mSelectedDay
        set(day) {
            mSelectedDay = day
            notifyDataSetChanged()
        }

    init {
        init()
        selectedDay = controller.selectedDay
    }

    /**
     * Set up the gesture detector and selected time
     */
    protected fun init() {
        mSelectedDay = CalendarDay(System.currentTimeMillis())
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
        val v: MonthView
        var drawingParams: HashMap<String, Int>? = null
        if (convertView != null) {
            v = convertView as MonthView
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
            selectedDay = mSelectedDay!!.day
        }

        // Invokes requestLayout() to ensure that the recycled view is set with the appropriate
        // height/number of weeks before being displayed.
        v.reuse()

        drawingParams[MonthView.VIEW_PARAMS_SELECTED_DAY] = selectedDay
        drawingParams[MonthView.VIEW_PARAMS_YEAR] = year
        drawingParams[MonthView.VIEW_PARAMS_MONTH] = month
        drawingParams[MonthView.VIEW_PARAMS_WEEK_START] = controller.firstDayOfWeek
        v.setMonthParams(drawingParams)
        v.invalidate()
        return v
    }

    abstract fun createMonthView(context: Context): MonthView

    private fun isSelectedDayInMonth(year: Int, month: Int): Boolean {
        return mSelectedDay!!.year == year && mSelectedDay!!.month == month
    }

    override fun onDayClick(view: MonthView, day: CalendarDay) {
        onDayTapped(day)
    }

    /**
     * Maintains the same hour/min/sec but moves the day to the tapped day.
     *
     * @param day The day that was tapped
     */
    private fun onDayTapped(day: CalendarDay) {
        controller.onDayOfMonthSelected(day.year, day.month, day.day)
        selectedDay = day
    }

    /**
     * A convenience class to represent a specific date.
     */
    class CalendarDay {
        var year: Int = 0
        var month: Int = 0
        var day: Int = 0
        private var baseCalendar: BaseCalendar? = null

        constructor() {
            setTime(System.currentTimeMillis())
        }

        constructor(timeInMillis: Long) {
            setTime(timeInMillis)
        }

        constructor(calendar: BaseCalendar) {
            year = calendar.year
            month = calendar.month
            day = calendar.dayOfMonth
        }

        constructor(year: Int, month: Int, day: Int) {
            setDay(year, month, day)
        }

        fun set(date: CalendarDay) {
            year = date.year
            month = date.month
            day = date.day
        }

        fun setDay(year: Int, month: Int, day: Int) {
            this.year = year
            this.month = month
            this.day = day
        }

        private fun setTime(timeInMillis: Long) {
            if (baseCalendar == null) {
                baseCalendar = CalendarFactory.newInstance(CurrentCalendarType.type)
            }
            baseCalendar!!.timeInMillis = timeInMillis
            year = baseCalendar!!.year
            month = baseCalendar!!.month
            day = baseCalendar!!.dayOfMonth
        }
    }

    companion object {
        const val MONTHS_IN_YEAR = 12
        protected const val WEEK_7_OVERHANG_HEIGHT = 7
    }

}
