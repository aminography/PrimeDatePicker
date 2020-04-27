package com.aminography.primedatepicker.sample.view;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.aminography.primecalendar.PrimeCalendar;
import com.aminography.primecalendar.common.CalendarFactory;
import com.aminography.primecalendar.common.CalendarType;
import com.aminography.primedatepicker.PickType;
import com.aminography.primedatepicker.picker.PrimeDatePickerBottomSheet;
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback;
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.BaseThemeFactory;
import com.aminography.primedatepicker.picker.theme.DarkThemeFactory;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.aminography.primedatepicker.sample.R;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.aminography.primedatepicker.sample.MyApplication.FONT_PATH_CIVIL;
import static com.aminography.primedatepicker.sample.MyApplication.FONT_PATH_HIJRI;
import static com.aminography.primedatepicker.sample.MyApplication.FONT_PATH_JAPANESE;
import static com.aminography.primedatepicker.sample.MyApplication.FONT_PATH_PERSIAN;

/**
 * @author aminography
 */
public class DatePickerActivity extends AppCompatActivity {

    private static final String PICKER_TAG = "PrimeDatePickerBottomSheet";
    private PrimeDatePickerBottomSheet datePicker = null;

    private RadioButton civilRadioButton;
    private RadioButton persianRadioButton;
    private RadioButton hijriRadioButton;
    private RadioButton japaneseRadioButton;

    private RadioButton singleRadioButton;
    private RadioButton rangeRadioButton;
    private RadioButton multipleRadioButton;

    private RadioButton calendarDefaultLocaleRadioButton;
    private RadioButton englishLocaleRadioButton;

    private RadioButton lightThemeRadioButton;
    private RadioButton darkThemeRadioButton;

    private CheckBox minDateCheckBox;
    private CheckBox maxDateCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_picker);

        civilRadioButton = findViewById(R.id.civilRadioButton);
        persianRadioButton = findViewById(R.id.persianRadioButton);
        hijriRadioButton = findViewById(R.id.hijriRadioButton);
        japaneseRadioButton = findViewById(R.id.japaneseRadioButton);

        singleRadioButton = findViewById(R.id.singleRadioButton);
        rangeRadioButton = findViewById(R.id.rangeRadioButton);
        multipleRadioButton = findViewById(R.id.multipleRadioButton);

        calendarDefaultLocaleRadioButton = findViewById(R.id.calendarDefaultLocaleRadioButton);
        englishLocaleRadioButton = findViewById(R.id.englishLocaleRadioButton);

        lightThemeRadioButton = findViewById(R.id.lightThemeRadioButton);
        darkThemeRadioButton = findViewById(R.id.darkThemeRadioButton);

        minDateCheckBox = findViewById(R.id.minDateCheckBox);
        maxDateCheckBox = findViewById(R.id.maxDateCheckBox);

        Button showDatePickerButton = findViewById(R.id.showDatePickerButton);
        showDatePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CalendarType calendarType = getCalendarType();
                Locale locale = getLocale(calendarType);
                PickType pickType = getPickType();
                PrimeCalendar minDateCalendar = getMinDateCalendar(calendarType);
                PrimeCalendar maxDateCalendar = getMaxDateCalendar(calendarType);
                final String typeface = getTypeface(calendarType);
                BaseThemeFactory theme = getDefaultTheme(typeface);

                PrimeCalendar today = CalendarFactory.newInstance(calendarType, locale);

                switch (pickType) {
                    case SINGLE:
                        datePicker = PrimeDatePickerBottomSheet.from(today)
                            .pickSingleDay(singleDayPickCallback)
                            .minPossibleDate(minDateCalendar)
                            .maxPossibleDate(maxDateCalendar)
                            .applyTheme(theme)
                            .build();
                        break;
                    case RANGE_START:
                        datePicker = PrimeDatePickerBottomSheet.from(today)
                            .pickRangeDays(rangeDaysPickCallback)
                            .minPossibleDate(minDateCalendar)
                            .maxPossibleDate(maxDateCalendar)
                            .applyTheme(theme)
                            .build();
                        break;
                    case MULTIPLE:
                        datePicker = PrimeDatePickerBottomSheet.from(today)
                            .pickMultipleDays(multipleDaysPickCallback)
                            .minPossibleDate(minDateCalendar)
                            .maxPossibleDate(maxDateCalendar)
                            .applyTheme(theme)
                            .build();
                        break;
                }

                datePicker.show(getSupportFragmentManager(), PICKER_TAG);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (datePicker != null) {
            datePicker.setDayPickCallback(null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(PICKER_TAG);
        if (fragment != null) {
            datePicker = (PrimeDatePickerBottomSheet) fragment;

            switch (datePicker.getPickType()) {
                case SINGLE:
                    datePicker.setDayPickCallback(singleDayPickCallback);
                    break;
                case RANGE_START:
                case RANGE_END:
                    datePicker.setDayPickCallback(rangeDaysPickCallback);
                    break;
                case MULTIPLE:
                    datePicker.setDayPickCallback(multipleDaysPickCallback);
                    break;
                default:
            }
        }
    }

    private SingleDayPickCallback singleDayPickCallback = new SingleDayPickCallback() {
        @Override
        public void onSingleDayPicked(PrimeCalendar singleDay) {
            longToast(singleDay.getLongDateString());
        }
    };

    private RangeDaysPickCallback rangeDaysPickCallback = new RangeDaysPickCallback() {
        @Override
        public void onRangeDaysPicked(PrimeCalendar startDay, PrimeCalendar endDay) {
            longToast(String.format("From: %s\nTo: %s", startDay.getLongDateString(), endDay.getLongDateString()));
        }
    };

    private MultipleDaysPickCallback multipleDaysPickCallback = new MultipleDaysPickCallback() {
        @Override
        public void onMultipleDaysPicked(List<PrimeCalendar> multipleDays) {
            List<String> list = new ArrayList<>();
            for (PrimeCalendar calendar : multipleDays) {
                list.add(calendar.getLongDateString());
            }
            longToast(TextUtils.join(" -\n", list));
        }
    };

    private CalendarType getCalendarType() {
        if (civilRadioButton.isChecked()) {
            return CalendarType.CIVIL;
        } else if (persianRadioButton.isChecked()) {
            return CalendarType.PERSIAN;
        } else if (hijriRadioButton.isChecked()) {
            return CalendarType.HIJRI;
        } else if (japaneseRadioButton.isChecked()) {
            return CalendarType.JAPANESE;
        } else {
            return CalendarType.CIVIL;
        }
    }

    private PickType getPickType() {
        if (singleRadioButton.isChecked()) {
            return PickType.SINGLE;
        } else if (rangeRadioButton.isChecked()) {
            return PickType.RANGE_START;
        } else if (multipleRadioButton.isChecked()) {
            return PickType.MULTIPLE;
        } else {
            return PickType.SINGLE;
        }
    }

    private Locale getLocale(CalendarType calendarType) {
        if (calendarDefaultLocaleRadioButton.isChecked()) {
            return CalendarFactory.newInstance(calendarType).getLocale();
        } else {
            return Locale.ENGLISH;
        }
    }

    private BaseThemeFactory getDefaultTheme(final String typeface) {
        if (lightThemeRadioButton.isChecked()) {
            return new LightThemeFactory() {
                @Nullable
                @Override
                public String getTypefacePath() {
                    return typeface;
                }
            };
        } else {
            return new DarkThemeFactory() {
                @Nullable
                @Override
                public String getTypefacePath() {
                    return typeface;
                }
            };
        }
    }

    private PrimeCalendar getMinDateCalendar(CalendarType calendarType) {
        PrimeCalendar minDateCalendar = null;
        if (minDateCheckBox.isChecked()) {
            minDateCalendar = CalendarFactory.newInstance(calendarType);
            minDateCalendar.add(Calendar.MONTH, -5);
        }
        return minDateCalendar;
    }

    private PrimeCalendar getMaxDateCalendar(CalendarType calendarType) {
        PrimeCalendar maxDateCalendar = null;
        if (maxDateCheckBox.isChecked()) {
            maxDateCalendar = CalendarFactory.newInstance(calendarType);
            maxDateCalendar.add(Calendar.MONTH, +5);
        }
        return maxDateCalendar;
    }

    private String getTypeface(CalendarType calendarType) {
        switch (calendarType) {
            case CIVIL:
                return FONT_PATH_CIVIL;
            case PERSIAN:
                return FONT_PATH_PERSIAN;
            case HIJRI:
                return FONT_PATH_HIJRI;
            case JAPANESE:
                return FONT_PATH_JAPANESE;
            default:
                return null;
        }
    }

    private void longToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
    }

}
