package com.aminography.primedatepicker.common;

import androidx.annotation.Nullable;

import com.aminography.primecalendar.PrimeCalendar;

import java.util.List;

/**
 * @author aminography
 */
@FunctionalInterface
public interface OnDayPickedListener {

    /**
     * Called when one day is clicked by the user.
     *
     * @param pickType     Specifies type of the picked day based on [PickType] which is set to the [PrimeMonthView] or [PrimeCalendarView].
     * @param singleDay    Specifies the picked day if the [pickType] is [PickType.SINGLE].
     * @param startDay     Specifies the start day of picked range if the [pickType] is [PickType.RANGE_START].
     * @param endDay       Specifies the end day of picked range if the [pickType] is [PickType.RANGE_END].
     * @param multipleDays Specifies the list of picked days if the [pickType] is [PickType.MULTIPLE].
     */
    void onDayPicked(
        PickType pickType,
        @Nullable PrimeCalendar singleDay,
        @Nullable PrimeCalendar startDay,
        @Nullable PrimeCalendar endDay,
        List<PrimeCalendar> multipleDays
    );
}
