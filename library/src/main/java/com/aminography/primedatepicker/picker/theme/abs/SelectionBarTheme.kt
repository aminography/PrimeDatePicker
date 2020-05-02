package com.aminography.primedatepicker.picker.theme.abs

import com.aminography.primedatepicker.common.LabelFormatter

/**
 * @author aminography
 */
interface SelectionBarTheme : GeneralTheme {

    // ------- General

    val selectionBarBackgroundColor: Int

    // ------- Single Day

    val selectionBarSingleDayItemTopLabelTextSize: Int

    val selectionBarSingleDayItemTopLabelTextColor: Int

    val selectionBarSingleDayItemBottomLabelTextSize: Int

    val selectionBarSingleDayItemBottomLabelTextColor: Int

    val selectionBarSingleDayItemGapBetweenLines: Int

    val selectionBarSingleDayLabelFormatter: LabelFormatter

    // ------- Range Days

    val selectionBarRangeDaysItemBackgroundColor: Int

    val selectionBarRangeDaysItemTopLabelTextSize: Int

    val selectionBarRangeDaysItemTopLabelTextColor: Int

    val selectionBarRangeDaysItemBottomLabelTextSize: Int

    val selectionBarRangeDaysItemBottomLabelTextColor: Int

    val selectionBarRangeDaysItemGapBetweenLines: Int

    val selectionBarRangeDaysLabelFormatter: LabelFormatter

    // ------- Multiple Days

    val selectionBarMultipleDaysItemBackgroundColor: Int

    val selectionBarMultipleDaysItemTopLabelTextSize: Int

    val selectionBarMultipleDaysItemTopLabelTextColor: Int

    val selectionBarMultipleDaysItemBottomLabelTextSize: Int

    val selectionBarMultipleDaysItemBottomLabelTextColor: Int

    val selectionBarMultipleDaysItemGapBetweenLines: Int

    val selectionBarMultipleDaysItemTopLabelFormatter: LabelFormatter

    val selectionBarMultipleDaysItemBottomLabelFormatter: LabelFormatter
}