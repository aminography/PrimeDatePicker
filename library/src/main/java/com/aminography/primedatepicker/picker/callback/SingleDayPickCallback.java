package com.aminography.primedatepicker.picker.callback;

import com.aminography.primecalendar.PrimeCalendar;

/**
 * @author aminography
 */
@FunctionalInterface
public interface SingleDayPickCallback extends BaseDayPickCallback {

    void onSingleDayPicked(PrimeCalendar singleDay);
}
