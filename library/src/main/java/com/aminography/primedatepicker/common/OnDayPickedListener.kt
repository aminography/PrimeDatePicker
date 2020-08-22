package com.aminography.primedatepicker.common;

import androidx.annotation.Nullable;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primedatepicker.calendarview.PrimeCalendarView;
import com.aminography.primedatepicker.monthview.PrimeMonthView;

import java.util.List;

/**
 * @author aminography
 */
@FunctionalInterface
public interface OnDayPickedListener {

    /**
     * Called when one day is picked by the user.
     *
     * @param pickType     Specifies type of the picked day based on {@param PickType} which is
     *                     set to the {@link PrimeMonthView} or {@link PrimeCalendarView}.
     * @param singleDay    Specifies the picked day if the {@param PickType}
     *                     is {@link PickType#SINGLE}.
     * @param startDay     Specifies the start day of picked range if the {@param PickType}
     *                     is {@link PickType#RANGE_START}.
     * @param endDay       Specifies the end day of picked range if the {@param PickType}
     *                     is {@link PickType#RANGE_END}.
     * @param multipleDays Specifies the list of picked days if the {@param PickType}
     *                     is {@link PickType#MULTIPLE}.
     */
    void onDayPicked(
        PickType pickType,
        @Nullable PrimeCalendar singleDay,
        @Nullable PrimeCalendar startDay,
        @Nullable PrimeCalendar endDay,
        List<PrimeCalendar> multipleDays
    );
}
