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
        @StyleRes defStyleRes: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {

    private val snapVelocity: Float = 30f
    var touchEnabled = true

    private val smoothScroller: RecyclerView.SmoothScroller = object : LinearSmoothScroller(context) {
        override fun getVerticalSnapPreference(): Int = LinearSmoothScroller.SNAP_TO_START

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float =
                snapVelocity / displayMetrics.densityDpi
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(e: MotionEvent?): Boolean {
        return touchEnabled && super.onTouchEvent(e)
    }

    override fun onInterceptTouchEvent(e: MotionEvent?): Boolean {
        return touchEnabled && super.onInterceptTouchEvent(e)
    }

}