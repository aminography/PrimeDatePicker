package com.aminography.primedatepicker.picker.callback;

import com.aminography.primecalendar.PrimeCalendar;

@FunctionalInterface
public interface RangeDaysPickCallback extends BaseDayPickCallback {

    void onRangeDaysPicked(PrimeCalendar startDay, PrimeCalendar endDay);
}
