package com.aminography.primedatepicker.sample;

import android.app.Application;
import androidx.appcompat.app.AppCompatDelegate;

/**
 * Created by aminography on 7/14/2018.
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

}
