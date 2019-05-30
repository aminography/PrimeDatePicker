package com.aminography.primedatepicker.old

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewConfiguration
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.ListView
import com.aminography.primecalendar.base.BaseCalendar
import com.aminography.primedatepicker.fragment.DateCalendarPickerBottomSheetDialogFragment

/**
 * This displays a list of months in a calendar format with selectable days.
 */
abstract class BaseMonthListView @JvmOverloads constructor(
        context: Context,
        attributeSet: AttributeSet? = null,
        private var controller: DatePickerController? = null
) : ListView(context, attributeSet),
        OnScrollListener,
        DateCalendarPickerBottomSheetDialogFragment.OnDateChangedListener {

    // These affect the scroll speed and feel
    private var scrollingFriction = 1.0f

    protected var mHandler: Handler? = null

    // highlighted time
    private var selectedDay: BaseCalendar? = OldUtils.newCalendar()
    private var mAdapter: BaseMonthListAdapter? = null

    private var tempDay: BaseCalendar? = OldUtils.newCalendar()

    // which month should be displayed/highlighted [0-11]
    private var currentMonthDisplayed: Int = 0

    // used for tracking during a scroll
    private var previousScrollPosition: Long = 0

    // used for tracking what state listview is in
    protected var previousScrollState = OnScrollListener.SCROLL_STATE_IDLE

    // used for tracking what state listview is in
    protected var currentScrollState = OnScrollListener.SCROLL_STATE_IDLE
    private var scrollStateChangedRunnable = ScrollStateRunnable()

    init {
        init()
        setController()
    }

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

    private fun setController() {
        controller?.apply {
            registerOnDateChangedListener(this@BaseMonthListView)
            refreshAdapter()
            onDateChanged()
        }
    }

    private fun init() {
        mHandler = Handler()
        layoutParams = AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT)
        setDrawSelectorOnTop(false)
        setUpListView()
    }

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
        setFriction(ViewConfiguration.getScrollFriction() * scrollingFriction)
    }

    fun onChange() {
        refreshAdapter()
    }

    /**
     * Creates a new mAdapter if necessary and sets up its parameters. Override
     * this method to provide a custom mAdapter.
     */
    private fun refreshAdapter() {
        if (mAdapter == null) {
            mAdapter = createMonthAdapter(context, controller!!)
        } else {
            mAdapter?.selectedDay = selectedDay
        }
        // refresh the view with the new parameters
        adapter = mAdapter
    }

    abstract fun createMonthAdapter(context: Context, controller: DatePickerController): BaseMonthListAdapter

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
    fun goTo(day: BaseCalendar, animate: Boolean, setSelected: Boolean, forceScroll: Boolean): Boolean {

        // Set the selected day
        if (setSelected) {
            selectedDay?.timeInMillis = day.timeInMillis
        }

        tempDay?.timeInMillis = day.timeInMillis
        var position = (day.year - controller!!.minYear) * BaseMonthListAdapter.MONTHS_IN_YEAR + day.month

        // Added by Amin ---------------------------------------------------------------------------
        controller?.minDate?.apply {
            position -= month
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
            mAdapter!!.selectedDay = selectedDay
        }

        // Check if the selected day is now outside of our visible range
        // and if so scroll to the month that contains it
        if (position != selectedPosition || forceScroll) {
            setMonthDisplayed(tempDay!!)
            previousScrollState = OnScrollListener.SCROLL_STATE_FLING
            if (animate) {
                smoothScrollToPositionFromTop(position, LIST_TOP_OFFSET, GOTO_SCROLL_DURATION)
                return true
            } else {
                postSetSelection(position)
            }
        } else if (setSelected) {
            setMonthDisplayed(selectedDay!!)
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
        if (view.getChildAt(0) == null) return
        val child = view.getChildAt(0) as MonthView

        // Figure out where we are
        previousScrollPosition = (view.firstVisiblePosition * child.height - child.bottom).toLong()
        previousScrollState = currentScrollState
    }

    /**
     * Sets the month displayed at the top of this view based on time. Override
     * to add custom events when the title is changed.
     */
    private fun setMonthDisplayed(date: BaseCalendar) {
        currentMonthDisplayed = date.month
        invalidateViews()
    }

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {
        // use a post to prevent re-entering onScrollStateChanged before it exits
        scrollStateChangedRunnable.doScrollStateChange(scrollState)
    }

    override fun onDateChanged() {
        controller?.apply {
            goTo(selectedDay, false, true, true)
        }
    }

    protected inner class ScrollStateRunnable : Runnable {

        private var newState: Int = 0

        /**
         * Sets up the runnable with a short delay in case the scroll state
         * immediately changes again.
         *
         * @param scrollState The new state it changed to
         */
        fun doScrollStateChange(scrollState: Int) {
            mHandler?.removeCallbacks(this)
            newState = scrollState
            mHandler?.postDelayed(this, SCROLL_CHANGE_DELAY.toLong())
        }

        override fun run() {
            currentScrollState = newState
            if (Log.isLoggable(TAG, Log.DEBUG)) {
                Log.d(TAG, "new scroll state: $newState old state: $previousScrollState")
            }
            // Fix the position after a scroll or a fling ends
            if (newState == OnScrollListener.SCROLL_STATE_IDLE
                    && previousScrollState != OnScrollListener.SCROLL_STATE_IDLE
                    && previousScrollState != OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                previousScrollState = newState
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
                previousScrollState = newState
            }
        }
    }

    companion object {

        // How long the GoTo fling animation should last
        protected const val GOTO_SCROLL_DURATION = 250

        // How long to wait after receiving an onScrollStateChanged notification before acting on it
        protected const val SCROLL_CHANGE_DELAY = 40

        private const val TAG = "MonthFragment"
        var LIST_TOP_OFFSET = -1 // so that the top line will be
    }
}
