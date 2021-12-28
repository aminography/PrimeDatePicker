package com.aminography.primedatepicker.sample.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aminography.primedatepicker.sample.R;

/**
 * @author aminography
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button datePickerButton = findViewById(R.id.datePickerButton);
        Button monthViewButton = findViewById(R.id.monthViewButton);
        Button calendarViewButton = findViewById(R.id.calendarViewButton);

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DatePickerActivity.class));
            }
        });

        monthViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not implemented yet in Java!", Toast.LENGTH_LONG).show();
            }
        });

        calendarViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Not implemented yet in Java!", Toast.LENGTH_LONG).show();
            }
        });
    }

}
