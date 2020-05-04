package com.aminography.primedatepicker.picker.callback;

import com.aminography.primecalendar.PrimeCalendar;

import java.util.List;

/**
 * @author aminography
 */
@FunctionalInterface
public interface MultipleDaysPickCallback extends BaseDayPickCallback {

    void onMultipleDaysPicked(List<PrimeCalendar> multipleDays);
}
