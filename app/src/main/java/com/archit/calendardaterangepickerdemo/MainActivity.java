package com.archit.calendardaterangepickerdemo;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    private DateRangeCalendarView calendar;
    private boolean viewReady = false;
    private ProgressBar progressBar;
    private RelativeLayout rlParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBar);

        getWindow().getDecorView().post(new Runnable() {

            @Override
            public void run() {

                // TODO your magic code to be run
                progressBar.post(new Runnable() {

                    @Override
                    public void run() {

                        rlParent = findViewById(R.id.rl_parrent);
                        calendar = (DateRangeCalendarView) getLayoutInflater().inflate(R.layout.layout_calendar, null);
                        calendar.setCalendarListener(new DateRangeCalendarView.CalendarListener() {
                            @Override
                            public void onFirstDateSelected(Calendar startDate) {
                                Toast.makeText(MainActivity.this, "Start Date: " + startDate.getTime().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
                                Toast.makeText(MainActivity.this, "Start Date: " + startDate.getTime().toString() + " End date: " + endDate.getTime().toString(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDrawCompelete() {
                                if (viewReady) {
                                    progressBar.setVisibility(View.GONE);
                                }
                            }

                        });
                        calendar.setOnPageChangeListener(new DateRangeCalendarView.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                            }

                            @Override
                            public void onPageSelected(int position) {
                                Toast.makeText(MainActivity.this, new DateFormatSymbols(getResources().getConfiguration().locale).getMonths()[calendar.getMonth(position).get(Calendar.MONTH)], Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {

                            }
                        });


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
                        }

                        /*Calendar start = Calendar.getInstance();
                        start.add(Calendar.MONTH, -1);
                        calendar.setDateDisplayed(start);*/
                        calendar.updateDataDescription(hashMap);
                        //calendar.invalidateCalendar();
                        viewReady = true;
                        Toast.makeText(MainActivity.this, new DateFormatSymbols(getResources().getConfiguration().locale).getMonths()[calendar.getCurrentMonth().get(Calendar.MONTH)], Toast.LENGTH_SHORT).show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                rlParent.addView(calendar);
                                            }
                                        }, 500);
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }

        });


    }
}
