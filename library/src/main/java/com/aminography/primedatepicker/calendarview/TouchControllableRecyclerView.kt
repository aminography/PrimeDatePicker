package com.aminography.primedatepicker.calendarview

import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearSmoothScroller
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent

/**
 * @author aminography
 */
class TouchControllableRecyclerView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @Suppress("UNUSED_PARAMETER") @StyleRes defStyleRes: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    var touchEnabled = true

    private var smoothScroller = createSmoothScroller(DEFAULT_TRANSITION_SPEED_FACTOR)

    internal var speedFactor: Float = DEFAULT_TRANSITION_SPEED_FACTOR
        set(value) {
            field = value
            smoothScroller = createSmoothScroller(value)
        }

    private fun createSmoothScroller(speedFactor: Float) =
            object : LinearSmoothScroller(context) {
                override fun getVerticalSnapPreference(): Int = SNAP_TO_START

                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float =
                        super.calculateSpeedPerPixel(displayMetrics) * speedFactor * 2
            }

    fun fastScrollTo(position: Int) {
        if (layoutManager is LinearLayoutManager) {
            (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
        }
    }

    fun smoothScrollTo(position: Int) {
        smoothScroller.targetPosition = position
        layoutManager?.startSmoothScroll(smoothScroller)
    }

    fun setHeight(height: Int) {
        layoutParams.height = height
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return touchEnabled && super.onTouchEvent(e)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return touchEnabled && super.onInterceptTouchEvent(e)
    }

    companion object {
        internal const val DEFAULT_TRANSITION_SPEED_FACTOR = 1f
    }

}