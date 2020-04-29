package com.aminography.primedatepicker.picker.callback;

import com.aminography.primecalendar.PrimeCalendar;

/**
 * @author aminography
 */
@FunctionalInterface
public interface RangeDaysPickCallback extends BaseDayPickCallback {

    void onRangeDaysPicked(PrimeCalendar startDay, PrimeCalendar endDay);
}
