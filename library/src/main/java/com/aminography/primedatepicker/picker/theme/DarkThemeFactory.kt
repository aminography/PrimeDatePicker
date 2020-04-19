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

    override val gotoBackgroundColor: Int
        get() = Color.parseColor("#E57373")

    override val selectionBarBackgroundColor: Int
        get() = Color.parseColor("#37474f")

}