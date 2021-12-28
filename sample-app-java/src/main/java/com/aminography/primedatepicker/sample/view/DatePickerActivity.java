package com.aminography.primedatepicker.sample.view;

import android.content.DialogInterface;
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
import com.aminography.primedatepicker.common.PickType;
import com.aminography.primedatepicker.picker.PrimeDatePicker;
import com.aminography.primedatepicker.picker.callback.MultipleDaysPickCallback;
import com.aminography.primedatepicker.picker.callback.RangeDaysPickCallback;
import com.aminography.primedatepicker.picker.callback.SingleDayPickCallback;
import com.aminography.primedatepicker.picker.theme.DarkThemeFactory;
import com.aminography.primedatepicker.picker.theme.LightThemeFactory;
import com.aminography.primedatepicker.picker.theme.base.ThemeFactory;
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
    private PrimeDatePicker datePicker = null;

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

    private RadioButton bottomSheetRadioButton;
    private RadioButton dialogRadioButton;

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

        bottomSheetRadioButton = findViewById(R.id.bottomSheetRadioButton);
        dialogRadioButton = findViewById(R.id.dialogRadioButton);

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
                ThemeFactory theme = getDefaultTheme(typeface);

                PrimeCalendar today = CalendarFactory.newInstance(calendarType, locale);

                switch (pickType) {
                    case SINGLE:
                        if (isBottomSheet()) {
                            datePicker = PrimeDatePicker.Companion.bottomSheetWith(today)
                                .pickSingleDay(singleDayPickCallback)
                                .minPossibleDate(minDateCalendar) // TODO: check if not null
                                .maxPossibleDate(maxDateCalendar)
                                .applyTheme(theme)
                                .build();
                        } else {
                            datePicker = PrimeDatePicker.Companion.dialogWith(today)
                                .pickSingleDay(singleDayPickCallback)
                                .minPossibleDate(minDateCalendar)
                                .maxPossibleDate(maxDateCalendar)
                                .applyTheme(theme)
                                .build();
                        }
                        break;
                    case RANGE_START:
                        if (isBottomSheet()) {
                            datePicker = PrimeDatePicker.Companion.bottomSheetWith(today)
                                .pickRangeDays(rangeDaysPickCallback)
                                .minPossibleDate(minDateCalendar)
                                .maxPossibleDate(maxDateCalendar)
                                .applyTheme(theme)
                                .build();
                        } else {
                            datePicker = PrimeDatePicker.Companion.dialogWith(today)
                                .pickRangeDays(rangeDaysPickCallback)
                                .minPossibleDate(minDateCalendar)
                                .maxPossibleDate(maxDateCalendar)
                                .applyTheme(theme)
                                .build();
                        }
                        break;
                    case MULTIPLE:
                        if (isBottomSheet()) {
                            datePicker = PrimeDatePicker.Companion.bottomSheetWith(today)
                                .pickMultipleDays(multipleDaysPickCallback)
                                .minPossibleDate(minDateCalendar)
                                .maxPossibleDate(maxDateCalendar)
                                .applyTheme(theme)
                                .build();
                        } else {
                            datePicker = PrimeDatePicker.Companion.dialogWith(today)
                                .pickMultipleDays(multipleDaysPickCallback)
                                .minPossibleDate(minDateCalendar)
                                .maxPossibleDate(maxDateCalendar)
                                .applyTheme(theme)
                                .build();
                        }
                        break;
                }

                datePicker.show(getSupportFragmentManager(), PICKER_TAG);

                datePicker.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        datePicker = null;
                    }
                });
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
            datePicker = (PrimeDatePicker) fragment;

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

    private final SingleDayPickCallback singleDayPickCallback = new SingleDayPickCallback() {
        @Override
        public void onSingleDayPicked(PrimeCalendar singleDay) {
            longToast(singleDay.getLongDateString());
        }
    };

    private final RangeDaysPickCallback rangeDaysPickCallback = new RangeDaysPickCallback() {
        @Override
        public void onRangeDaysPicked(PrimeCalendar startDay, PrimeCalendar endDay) {
            longToast(String.format("From: %s\nTo: %s", startDay.getLongDateString(), endDay.getLongDateString()));
        }
    };

    private final MultipleDaysPickCallback multipleDaysPickCallback = new MultipleDaysPickCallback() {
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

    private ThemeFactory getDefaultTheme(final String typeface) {
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

    private boolean isBottomSheet() {
        return bottomSheetRadioButton.isChecked();
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
