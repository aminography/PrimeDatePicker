package com.aminography.primedatepicker.calendarview.other

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

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