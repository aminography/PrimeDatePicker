package com.aminography.primedatepicker.fragment.callback;

import com.aminography.primecalendar.PrimeCalendar;

@FunctionalInterface
public interface SingleDayPickCallback extends BaseDayPickCallback {

    void onSingleDayPicked(PrimeCalendar singleDay);
}
