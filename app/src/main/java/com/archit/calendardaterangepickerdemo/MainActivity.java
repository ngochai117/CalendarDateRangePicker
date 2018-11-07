package com.archit.calendardaterangepickerdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;

import java.util.Calendar;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private DateRangeCalendarView calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = findViewById(R.id.calendar);
/*
        Typeface typeface = Typeface.createFromAsset(getAssets(), "JosefinSans-Regular.ttf");
//        Typeface typeface = Typeface.createFromAsset(getAssets(), "LobsterTwo-Regular.ttf");
        calendar.setFonts(typeface);*/
        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
            @Override
            public void onFirstDateSelected(Calendar startDate) {
                Toast.makeText(MainActivity.this, "Start Date: " + startDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                Toast.makeText(MainActivity.this, "Start Date: " + startDate.getTime().toString() + " End date: " + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
            }

        });

        calendar.post(new Runnable() {
            @Override
            public void run() {
                int totalDate = 20;
                HashMap<Long, String> hashMap = new HashMap<>();
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 0);
                today.set(Calendar.MINUTE, 0);
                today.set(Calendar.SECOND, 0);
                today.set(Calendar.MILLISECOND, 0);
                today.add(Calendar.DATE, -totalDate);
                for (int i = 0; i < totalDate * 2; i++) {
                    hashMap.put(today.getTimeInMillis(), String.valueOf(today.get(Calendar.DAY_OF_YEAR)));
                    today.add(Calendar.DATE, 1);
                    Log.d(MainActivity.this.getClass().getSimpleName(), "main: " + today.getTime() + today.getTimeInMillis());
                }
                Log.d(MainActivity.this.getClass().getSimpleName(), "hashMap: " + hashMap.toString());

                /*Calendar start = Calendar.getInstance();
                start.add(Calendar.MONTH, 2);
                calendar.setDateDisplayed(start);*/
                calendar.updateDataDescription(hashMap);
                calendar.invalidateCalendar();
            }
        });
    }
}
