package com.aminography.primedatepicker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.ListView
import com.aminography.primecalendar.common.CalendarFactory
import com.aminography.primecalendar.common.CalendarType
import com.aminography.primedatepicker.tools.CurrentCalendarType
import com.aminography.primedatepicker.tools.PersianUtils
import com.aminography.primedatepicker.tools.Utils

/**
 * This displays a list of months in a calendar format with selectable days.
 */
abstract class DayPickerView : ListView, OnScrollListener, DateCalendarPickerBottomSheetDialogFragment.OnDateChangedListener {

    // These affect the scroll speed and feel
    private var mFriction = 1.0f

    private var mContext: Context? = null
    protected var mHandler: Handler? = null

    // highlighted time
    private var mSelectedDay = MonthAdapter.CalendarDay()
    private var mAdapter: MonthAdapter? = null

    private var mTempDay = MonthAdapter.CalendarDay()

    // When the week starts; numbered like Time.<WEEKDAY> (e.g. SUNDAY=0).
    protected var mFirstDayOfWeek: Int = 0

    // The last name announced by accessibility
    protected var mPrevMonthName: CharSequence? = null

    // which month should be displayed/highlighted [0-11]
    private var mCurrentMonthDisplayed: Int = 0

    // used for tracking during a scroll
    private var mPreviousScrollPosition: Long = 0

    // used for tracking what state listview is in
    protected var mPreviousScrollState = OnScrollListener.SCROLL_STATE_IDLE

    // used for tracking what state listview is in
    protected var mCurrentScrollState = OnScrollListener.SCROLL_STATE_IDLE
    private var mScrollStateChangedRunnable = ScrollStateRunnable()
    private var mController: DatePickerController? = null
    private var mPerformingScroll: Boolean = false
    @ColorInt
    protected var mainColor: Int? = null

    /**
     * Gets the position of the view that is most prominently displayed within the list view.
     */
    val mostVisiblePosition: Int
        get() {
            val firstPosition = firstVisiblePosition
            val height = height

            var maxDisplayedHeight = 0
            var mostVisibleIndex = 0
            var i = 0
            var bottom = 0
            while (bottom < height) {
                val child = getChildAt(i) ?: break
                bottom = child.bottom
                val displayedHeight = Math.min(bottom, height) - Math.max(0, child.top)
                if (displayedHeight > maxDisplayedHeight) {
                    mostVisibleIndex = i
                    maxDisplayedHeight = displayedHeight
                }
                i++
            }
            return firstPosition + mostVisibleIndex
        }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, controller: DatePickerController, @ColorInt mainColor: Int?) : super(context) {
        this.mainColor = mainColor
        init(context)
        setController(controller)
    }

    private fun setController(controller: DatePickerController) {
        mController = controller
        mController!!.registerOnDateChangedListener(this)
        refreshAdapter()
        onDateChanged()
    }

    fun init(context: Context) {
        mHandler = Handler()
        layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT)
        setDrawSelectorOnTop(false)

        mContext = context
        setUpListView()
    }

    fun onChange() {
        refreshAdapter()
    }

    /**
     * Creates a new adapter if necessary and sets up its parameters. Override
     * this method to provide a custom adapter.
     */
    private fun refreshAdapter() {
        if (mAdapter == null) {
            mAdapter = createMonthAdapter(context, mController!!)
        } else {
            mAdapter!!.selectedDay = mSelectedDay
        }
        // refresh the view with the new parameters
        adapter = mAdapter
    }

    abstract fun createMonthAdapter(context: Context, controller: DatePickerController): MonthAdapter

    /*
     * Sets all the required fields for the list view. Override this method to
     * set a different list view behavior.
     */
    private fun setUpListView() {
        // Transparent background on scroll
        cacheColorHint = 0
        // No dividers
        divider = null
        // Items are clickable
        itemsCanFocus = true
        // The thumb gets in the way, so disable it
        isFastScrollEnabled = false
        isVerticalScrollBarEnabled = false
        setOnScrollListener(this)
        setFadingEdgeLength(0)
        // Make the scrolling behavior nicer
        setFriction(ViewConfiguration.getScrollFriction() * mFriction)
    }

    /**
     * This moves to the specified time in the view. If the time is not already
     * in range it will move the list so that the first of the month containing
     * the time is at the top of the view. If the new time is already in view
     * the list will not be scrolled unless forceScroll is true. This time may
     * optionally be highlighted as selected as well.
     *
     * @param day         The day to move to
     * @param animate     Whether to scroll to the given time or just redraw at the
     * new location
     * @param setSelected Whether to set the given time as selected
     * @param forceScroll Whether to recenter even if the time is already
     * visible
     * @return Whether or not the view animated to the new location
     */
    fun goTo(day: MonthAdapter.CalendarDay, animate: Boolean, setSelected: Boolean, forceScroll: Boolean): Boolean {

        // Set the selected day
        if (setSelected) {
            mSelectedDay.set(day)
        }

        mTempDay.set(day)
        var position = (day.year - mController!!.minYear) * MonthAdapter.MONTHS_IN_YEAR + day.month

        // Added by Amin ---------------------------------------------------------------------------
        val min = mController!!.minDate
        if (min != null) {
            position -= min.month
            if (position < 0) {
                position = 0
            }
        }
        // Added by Amin ---------------------------------------------------------------------------

        var child: View?
        var i = 0
        var top = 0
        // Find a child that's completely in the view
        do {
            child = getChildAt(i++)
            if (child == null) {
                break
            }
            top = child.top
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "child at " + (i - 1) + " has top " + top)
            }
        } while (top < 0)

        // Compute the first and last position visible
        val selectedPosition: Int
        selectedPosition = if (child != null) {
            getPositionForView(child)
        } else {
            0
        }

        if (setSelected) {
            mAdapter!!.selectedDay = mSelectedDay
        }

        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "GoTo position $position")
        }
        // Check if the selected day is now outside of our visible range
        // and if so scroll to the month that contains it
        if (position != selectedPosition || forceScroll) {
            setMonthDisplayed(mTempDay)
            mPreviousScrollState = OnScrollListener.SCROLL_STATE_FLING
            if (animate) {
                smoothScrollToPositionFromTop(position, LIST_TOP_OFFSET, GOTO_SCROLL_DURATION)
                return true
            } else {
                postSetSelection(position)
            }
        } else if (setSelected) {
            setMonthDisplayed(mSelectedDay)
        }
        return false
    }

    private fun postSetSelection(position: Int) {
        clearFocus()
        post { setSelection(position) }
        onScrollStateChanged(this, OnScrollListener.SCROLL_STATE_IDLE)
    }

    /**
     * Updates the title and selected month if the view has moved to a new
     * month.
     */
    override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (view.getChildAt(0) == null) return //TODO
        val child = view.getChildAt(0) as MonthView

        // Figure out where we are
        mPreviousScrollPosition = (view.firstVisiblePosition * child.height - child.bottom).toLong()
        mPreviousScrollState = mCurrentScrollState
    }

    /**
     * Sets the month displayed at the top of this view based on time. Override
     * to add custom events when the title is changed.
     */
    private fun setMonthDisplayed(date: MonthAdapter.CalendarDay) {
        mCurrentMonthDisplayed = date.month
        invalidateViews()
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        // use a post to prevent re-entering onScrollStateChanged before it exits
        mScrollStateChangedRunnable.doScrollStateChange(scrollState)
    }

    override fun onDateChanged() {
        goTo(mController!!.selectedDay, false, true, true)
    }

    /**
     * Attempts to return the date that has accessibility focus.
     *
     * @return The date that has accessibility focus, or `null` if no date
     * has focus.
     */
    private fun findAccessibilityFocus(): MonthAdapter.CalendarDay? {
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is MonthView) {
                val focus = child.accessibilityFocus
                if (focus != null) {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        // Clear focus to avoid ListView bug in Jelly Bean MR1.
                        child.clearAccessibilityFocus()
                    }
                    return focus
                }
            }
        }

        return null
    }

    /**
     * Attempts to restore accessibility focus to a given date. No-op if
     * `day` is `null`.
     *
     * @param day The date that should receive accessibility focus
     * @return `true` if focus was restored
     */
    private fun restoreAccessibilityFocus(day: MonthAdapter.CalendarDay?): Boolean {
        if (day == null) {
            return false
        }

        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child is MonthView) {
                if (child.restoreAccessibilityFocus(day)) {
                    return true
                }
            }
        }

        return false
    }

    override fun layoutChildren() {
        val focusedDay = findAccessibilityFocus()
        super.layoutChildren()
        if (mPerformingScroll) {
            mPerformingScroll = false
        } else {
            restoreAccessibilityFocus(focusedDay)
        }
    }

    override fun onInitializeAccessibilityEvent(event: AccessibilityEvent) {
        super.onInitializeAccessibilityEvent(event)
        event.itemCount = -1
    }

    /**
     * Necessary for accessibility, to ensure we support "scrolling" forward and backward
     * in the month list.
     */
    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        if (Build.VERSION.SDK_INT >= 21) {
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_BACKWARD)
            info.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SCROLL_FORWARD)
        } else {
            info.addAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD)
            info.addAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD)
        }
    }

    /**
     * When scroll forward/backward events are received, announce the newly scrolled-to month.
     */
    @SuppressLint("NewApi")
    override fun performAccessibilityAction(action: Int, arguments: Bundle): Boolean {
        if (action != AccessibilityNodeInfo.ACTION_SCROLL_FORWARD && action != AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD) {
            return super.performAccessibilityAction(action, arguments)
        }

        // Figure out what month is showing.
        val firstVisiblePosition = firstVisiblePosition
        val month = firstVisiblePosition % 12
        val year = firstVisiblePosition / 12 + mController!!.minYear

        //        // Added by Amin ---------------------------------------------------------------------------
        //        PersianCalendar min = controller.getMinDate();
        //        PersianCalendar max = controller.getMaxDate();
        //        if (min != null && max != null) {
        //            int index = firstVisiblePosition + min.getMonth();
        //            month = index % MonthAdapter.MONTHS_IN_YEAR;
        //            year = index / MonthAdapter.MONTHS_IN_YEAR + controller.getMinYear();
        //        }
        //        // Added by Amin ---------------------------------------------------------------------------

        val day = MonthAdapter.CalendarDay(year, month, 1)

        // Scroll either forward or backward one month.
        if (action == AccessibilityNodeInfo.ACTION_SCROLL_FORWARD) {
            day.month = day.month + 1
            if (day.month == 12) {
                day.month = 0
                day.year = day.year + 1
            }
        } else if (action == AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD) {
            val firstVisibleView = getChildAt(0)
            // If the view is fully visible, jump one month back. Otherwise, we'll just jump
            // to the first day of first visible month.
            if (firstVisibleView != null && firstVisibleView.top >= -1) {
                // There's an off-by-one somewhere, so the top of the first visible item will
                // actually be -1 when it's at the exact top.
                day.month = day.month - 1
                if (day.month == -1) {
                    day.month = 11
                    day.year = day.year - 1
                }
            }
        }

        // Go to that month.
        val date = when (CurrentCalendarType.type) {
            CalendarType.CIVIL -> getMonthAndYearString(day)
            CalendarType.PERSIAN -> PersianUtils.convertLatinDigitsToPersian(getMonthAndYearString(day))
            CalendarType.HIJRI -> PersianUtils.convertLatinDigitsToPersian(getMonthAndYearString(day))
        }
        Utils.tryAccessibilityAnnounce(this, date)
        goTo(day, true, false, true)
        mPerformingScroll = true
        return true
    }

    protected inner class ScrollStateRunnable : Runnable {
        private var mNewState: Int = 0

        /**
         * Sets up the runnable with a short delay in case the scroll state
         * immediately changes again.
         *
         * @param scrollState The new state it changed to
         */
        fun doScrollStateChange(scrollState: Int) {
            mHandler?.removeCallbacks(this)
            mNewState = scrollState
            mHandler?.postDelayed(this, SCROLL_CHANGE_DELAY.toLong())
        }

        override fun run() {
            mCurrentScrollState = mNewState
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG,
                        "new scroll state: $mNewState old state: $mPreviousScrollState")
            }
            // Fix the position after a scroll or a fling ends
            if (mNewState == OnScrollListener.SCROLL_STATE_IDLE
                    && mPreviousScrollState != OnScrollListener.SCROLL_STATE_IDLE
                    && mPreviousScrollState != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                mPreviousScrollState = mNewState
                var i = 0
                var child: View? = getChildAt(i)
                while (child != null && child.bottom <= 0) {
                    child = getChildAt(++i)
                }
                if (child == null) {
                    // The view is no longer visible, just return
                    return
                }
                val firstPosition = firstVisiblePosition
                val lastPosition = lastVisiblePosition
                val scroll = firstPosition != 0 && lastPosition != count - 1
                val top = child.top
                val bottom = child.bottom
                val midpoint = height / 2
                if (scroll && top < LIST_TOP_OFFSET) {
                    if (bottom > midpoint) {
                        smoothScrollBy(top, GOTO_SCROLL_DURATION)
                    } else {
                        smoothScrollBy(bottom, GOTO_SCROLL_DURATION)
                    }
                }
            } else {
                mPreviousScrollState = mNewState
            }
        }
    }

    companion object {

        // The number of days to display in each week
        val DAYS_PER_WEEK = 7
        // Affects when the month selection will change while scrolling up
        protected val SCROLL_HYST_WEEKS = 2
        // How long the GoTo fling animation should last
        protected const val GOTO_SCROLL_DURATION = 250
        // How long to wait after receiving an onScrollStateChanged notification
        // before acting on it
        protected const val SCROLL_CHANGE_DELAY = 40
        private const val TAG = "MonthFragment"
        var LIST_TOP_OFFSET = -1 // so that the top line will be

        private fun getMonthAndYearString(day: MonthAdapter.CalendarDay): String {
            val calendar = CalendarFactory.newInstance(CurrentCalendarType.type)
            calendar.setDate(day.year, day.month, day.day)
            return "${calendar.monthName} ${calendar.year}"
        }
    }
}
