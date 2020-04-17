package com.aminography.primedatepicker.picker.theme

import android.view.animation.Interpolator
import com.aminography.primedatepicker.calendarview.PrimeCalendarView

/**
 * @author aminography
 */
abstract class BaseThemeFactory {

    abstract val backgroundColor: Int

    abstract val monthLabelTextColor: Int

    abstract val weekLabelTextColor: Int

    abstract val dayLabelTextColor: Int

    abstract val todayLabelTextColor: Int

    abstract val pickedDayLabelTextColor: Int

    abstract val pickedDayCircleColor: Int

    abstract val disabledDayLabelTextColor: Int

    abstract val monthLabelTextSize: Int

    abstract val weekLabelTextSize: Int

    abstract val dayLabelTextSize: Int

    abstract val monthLabelTopPadding: Int

    abstract val monthLabelBottomPadding: Int

    abstract val weekLabelTopPadding: Int

    abstract val weekLabelBottomPadding: Int

    abstract val dayLabelVerticalPadding: Int

    abstract val showTwoWeeksInLandscape: Boolean

    abstract val animationDuration: Int

    abstract val animationInterpolator: Interpolator

    abstract val dividerColor: Int

    abstract val dividerThickness: Int

    abstract val dividerInsetLeft: Int

    abstract val dividerInsetRight: Int

    abstract val dividerInsetTop: Int

    abstract val dividerInsetBottom: Int

    abstract val loadFactor: Int

    abstract val maxTransitionLength: Int

    abstract val transitionSpeedFactor: Float

    abstract val flingOrientation: PrimeCalendarView.FlingOrientation

}