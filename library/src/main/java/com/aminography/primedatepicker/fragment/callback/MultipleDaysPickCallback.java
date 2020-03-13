package com.aminography.primedatepicker.fragment.callback;

import com.aminography.primecalendar.PrimeCalendar;

import java.util.List;

@FunctionalInterface
public interface MultipleDaysPickCallback extends BaseDayPickCallback {

    void onMultipleDaysPicked(List<PrimeCalendar> multipleDays);
}
