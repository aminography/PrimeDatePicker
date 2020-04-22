package com.aminography.primedatepicker.picker.theme

import android.graphics.Color
import com.aminography.primedatepicker.R

/**
 * @author aminography
 */
open class DarkThemeFactory : BaseNormalThemeFactory() {

    override val backgroundColor: Int
        get() = Color.parseColor("#2A2B2F")

    override val monthLabelTextColor: Int
        get() = getColor(R.color.blueGray300)

    override val weekLabelTextColor: Int
        get() = Color.parseColor("#1565c0")

    override val dayLabelTextColor: Int
        get() = Color.WHITE

    override val todayLabelTextColor: Int
        get() = Color.parseColor("#ef6c00")

    override val pickedDayLabelTextColor: Int
        get() = Color.WHITE

    override val pickedDayCircleColor: Int
        get() = Color.parseColor("#00796b")

    override val disabledDayLabelTextColor: Int
        get() = getColor(R.color.defaultDisabledDayLabelTextColor)

    override val dividerColor: Int
        get() = getColor(R.color.defaultDividerColor)

    override val buttonBarBackgroundColor: Int
        get() = Color.parseColor("#2A2B2F")

    override val buttonBarTodayTextColor: Int
        get() = Color.WHITE

    override val buttonBarNegativeTextColor: Int
        get() = getColor(R.color.textColorRed)

    override val buttonBarPositiveTextColor: Int
        get() = getColor(R.color.textColorGreen)

    override val gotoBackgroundColor: Int
        get() = Color.parseColor("#E57373")

    override val selectionBarBackgroundColor: Int
        get() = Color.parseColor("#37474f")

    override val gotoTextColor: Int
        get() = Color.WHITE

    override val gotoDividerColor: Int
        get() = Color.WHITE

    override val multipleDaysItemFirstLabelTextColor: Int
        get() = Color.WHITE

    override val multipleDaysItemSecondLabelTextColor: Int
        get() = Color.WHITE

    override val singleDayItemFirstLabelTextColor: Int
        get() = getColor(R.color.transWhiteAA)

    override val singleDayItemSecondLabelTextColor: Int
        get() = Color.WHITE

    override val rangeDaysItemFirstLabelTextColor: Int
        get() = getColor(R.color.transWhiteAA)

    override val rangeDaysItemSecondLabelTextColor: Int
        get() = Color.WHITE

}