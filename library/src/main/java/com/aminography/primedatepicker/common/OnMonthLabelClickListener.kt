package com.aminography.primedatepicker.common;

import com.aminography.primecalendar.PrimeCalendar;

/**
 * @author aminography
 */
@FunctionalInterface
public interface OnMonthLabelClickListener {

    void onMonthLabelClicked(
        PrimeCalendar calendar,
        int touchedX,
        int touchedY
    );
}
