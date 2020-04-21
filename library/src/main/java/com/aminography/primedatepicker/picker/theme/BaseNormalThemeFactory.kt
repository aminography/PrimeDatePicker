package com.aminography.primedatepicker.picker.theme

import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import com.aminography.primedatepicker.R
import com.aminography.primedatepicker.calendarview.PrimeCalendarView
import com.aminography.primedatepicker.monthview.MonthLabelFormatter
import com.aminography.primedatepicker.monthview.WeekLabelFormatter

/**
 * @author aminography
 */
abstract class BaseNormalThemeFactory : BaseThemeFactory() {

    override val monthLabelTextSize: Int
        get() = getDimension(R.dimen.defaultMonthLabelTextSize)

    override val weekLabelTextSize: Int
        get() = getDimension(R.dimen.defaultWeekLabelTextSize)

    override val dayLabelTextSize: Int
        get() = getDimension(R.dimen.defaultDayLabelTextSize)

    override val monthLabelTopPadding: Int
        get() = getDimension(R.dimen.defaultMonthLabelTopPadding)

    override val monthLabelBottomPadding: Int
        get() = getDimension(R.dimen.defaultMonthLabelBottomPadding)

    override val weekLabelTopPadding: Int
        get() = getDimension(R.dimen.defaultWeekLabelTopPadding)

    override val weekLabelBottomPadding: Int
        get() = getDimension(R.dimen.defaultWeekLabelBottomPadding)

    override val dayLabelVerticalPadding: Int
        get() = getDimension(R.dimen.defaultDayLabelVerticalPadding)

    override val showTwoWeeksInLandscape: Boolean
        get() = getBoolean(R.bool.defaultShowTwoWeeksInLandscape)

    override val animateSelection: Boolean
        get() = getBoolean(R.bool.defaultAnimateSelection)

    override val animationDuration: Int
        get() = getInteger(R.integer.defaultAnimationDuration)

    override val animationInterpolator: Interpolator
        get() = OvershootInterpolator()

    override val dividerThickness: Int
        get() = getDimension(R.dimen.defaultDividerThickness)

    override val dividerInsetLeft: Int
        get() = getDimension(R.dimen.defaultDividerInsetLeft)

    override val dividerInsetRight: Int
        get() = getDimension(R.dimen.defaultDividerInsetRight)

    override val dividerInsetTop: Int
        get() = getDimension(R.dimen.defaultDividerInsetTop)

    override val dividerInsetBottom: Int
        get() = getDimension(R.dimen.defaultDividerInsetBottom)

    override val loadFactor: Int
        get() = getInteger(R.integer.defaultLoadFactor)

    override val maxTransitionLength: Int
        get() = getInteger(R.integer.defaultMaxTransitionLength)

    override val transitionSpeedFactor: Float
        get() = getFloat(R.string.defaultTransitionSpeedFactor)

    override val flingOrientation: PrimeCalendarView.FlingOrientation
        get() = PrimeCalendarView.FlingOrientation.VERTICAL

    override val typefacePath: String?
        get() = null

    override val buttonBarTextSize: Int
        get() = getDimension(R.dimen.defaultButtonTextSize)

    override val monthLabelFormatter: MonthLabelFormatter
        get() = { primeCalendar -> "${primeCalendar.monthName} ${primeCalendar.year}" }

    override val weekLabelFormatter: WeekLabelFormatter
        get() = { primeCalendar -> primeCalendar.weekDayNameShort }

    override val developerOptionsShowGuideLines: Boolean
        get() = false

    override val gotoTextSize: Int
        get() = getDimension(R.dimen.defaultGotoTextSize)

}