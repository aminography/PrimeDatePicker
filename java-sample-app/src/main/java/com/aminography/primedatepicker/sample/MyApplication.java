package com.aminography.primedatepicker.sample;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * @author aminography
 */
public class MyApplication extends Application {

    public static final String FONT_PATH_CIVIL = "fonts/Roboto-Regular.ttf";
    public static final String FONT_PATH_PERSIAN = "fonts/IRANSans.ttf";
    public static final String FONT_PATH_HIJRI = "fonts/Amiri-Regular.ttf";
    public static final String FONT_PATH_JAPANESE = "fonts/Roboto-Regular.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

}
