package com.aminography.primedatepicker.picker.theme

import android.graphics.Color

/**
 * @author aminography
 */
class DarkThemeFactory : BaseNormalThemeFactory() {

    override val backgroundColor: Int
        get() = Color.parseColor("#2A2B2F")

    override val monthLabelTextColor: Int
        get() = TODO("Not yet implemented")

    override val weekLabelTextColor: Int
        get() = TODO("Not yet implemented")

    override val dayLabelTextColor: Int
        get() = Color.WHITE

    override val todayLabelTextColor: Int
        get() = TODO("Not yet implemented")

    override val pickedDayLabelTextColor: Int
        get() = TODO("Not yet implemented")

    override val pickedDayCircleColor: Int
        get() = TODO("Not yet implemented")

    override val disabledDayLabelTextColor: Int
        get() = TODO("Not yet implemented")

    override val dividerColor: Int
        get() = TODO("Not yet implemented")

    override val buttonBarBackgroundColor: Int
        get() = Color.parseColor("#2A2B2F")

    override val buttonBarTodayTextColor: Int
        get() = Color.WHITE

    override val gotoBackgroundColor: Int
        get() = Color.parseColor("#E57373")

    override val selectionBarBackgroundColor: Int
        get() = Color.parseColor("#607D8B")

}