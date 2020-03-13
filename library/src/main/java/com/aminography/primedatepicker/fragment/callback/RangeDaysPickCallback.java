package com.aminography.primedatepicker.fragment.callback;

import com.aminography.primecalendar.PrimeCalendar;

@FunctionalInterface
public interface RangeDaysPickCallback extends BaseDayPickCallback {

    void onRangeDaysPicked(PrimeCalendar startDay, PrimeCalendar endDay);
}
