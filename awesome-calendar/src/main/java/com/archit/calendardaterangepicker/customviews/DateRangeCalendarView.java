package com.archit.calendardaterangepicker.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.adapter.AdapterEventCalendarMonths;
import com.archit.calendardaterangepicker.models.CalendarStyleAttr;
import com.archit.calendardaterangepicker.models.DayModel;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DateRangeCalendarView extends LinearLayout {

    private Context mContext;
    private CustomTextView tvYearTitle;
    private AppCompatImageView imgVNavLeft, imgVNavRight;
    private List<Calendar> dataList = new ArrayList<>();

    private AdapterEventCalendarMonths adapterEventCalendarMonths;
    private Locale locale;

    private ViewPager vpCalendar;
    private CalendarStyleAttr calendarStyleAttr;


    public final static int TOTAL_ALLOWED_MONTHS = 30;
    private Calendar dateDefaultDisplayed;

    private OnPageChangeListener onPageChangeListener;

    public DateRangeCalendarView(Context context) {
        super(context);
        initViews(context, null);
    }

    public DateRangeCalendarView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initViews(context, attrs);
    }

    public DateRangeCalendarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context, attrs);
    }

    private void initViews(Context context, AttributeSet attrs) {

        mContext = context;
        locale = context.getResources().getConfiguration().locale;
        calendarStyleAttr = new CalendarStyleAttr(mContext, attrs);

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.layout_calendar_container, this, true);

        RelativeLayout rlHeaderCalendar = findViewById(R.id.rlHeaderCalendar);
        rlHeaderCalendar.setBackground(calendarStyleAttr.getHeaderBg());

        tvYearTitle = findViewById(R.id.tvYearTitle);
        imgVNavLeft = findViewById(R.id.imgVNavLeft);
        imgVNavRight = findViewById(R.id.imgVNavRight);

        vpCalendar = findViewById(R.id.vpCalendar);

        initDataCalendar();

        adapterEventCalendarMonths = new AdapterEventCalendarMonths(mContext, dataList, calendarStyleAttr);
        vpCalendar.setOffscreenPageLimit(0);
        vpCalendar.setAdapter(adapterEventCalendarMonths);
        vpCalendar.setCurrentItem(TOTAL_ALLOWED_MONTHS);
        setCalendarYearTitle(TOTAL_ALLOWED_MONTHS);
        setListeners();
    }

    private void initDataCalendar() {
        dataList.clear();
        Calendar datedisplayed;
        if (dateDefaultDisplayed == null) {
            datedisplayed = (Calendar) Calendar.getInstance().clone();
        } else {
            datedisplayed = (Calendar) dateDefaultDisplayed.clone();
        }
        datedisplayed.add(Calendar.MONTH, -TOTAL_ALLOWED_MONTHS);

        for (int i = 0; i < TOTAL_ALLOWED_MONTHS * 2; i++) {
            dataList.add((Calendar) datedisplayed.clone());
            datedisplayed.add(Calendar.MONTH, 1);
        }

    }

    private void setListeners() {

        vpCalendar.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                setCalendarYearTitle(position);
                setNavigationHeader(position);
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageScrollStateChanged(state);
                }
            }
        });

        imgVNavLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int newPosition = vpCalendar.getCurrentItem() - 1;
                if (newPosition > -1) {
                    vpCalendar.setCurrentItem(newPosition);
                }
            }
        });
        imgVNavRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                int newPosition = vpCalendar.getCurrentItem() + 1;
                if (newPosition < dataList.size()) {
                    vpCalendar.setCurrentItem(newPosition);
                }
            }
        });
    }

    private Calendar getMonth(Calendar calendar) {
        Calendar month = (Calendar) calendar.clone();
        month.set(Calendar.DAY_OF_MONTH, 1);
        month.set(Calendar.HOUR_OF_DAY, 0);
        month.set(Calendar.MINUTE, 0);
        month.set(Calendar.SECOND, 0);
        month.set(Calendar.MILLISECOND, 0);
        return month;
    }

    public Calendar getMonth(int positionOfViewPager) {
        return getMonth(dataList.get(positionOfViewPager));
    }

    public Calendar getCurrentMonth() {
        return getMonth(vpCalendar.getCurrentItem());
    }

    /**
     * To set navigation header ( Left-Right button )
     *
     * @param position New page position
     */
    private void setNavigationHeader(int position) {
        imgVNavRight.setVisibility(VISIBLE);
        imgVNavLeft.setVisibility(VISIBLE);
        if (position == 0) {
            imgVNavLeft.setVisibility(INVISIBLE);
        } else if (position == dataList.size() - 1) {
            imgVNavRight.setVisibility(INVISIBLE);
        }
    }

    /**
     * To set calendar year title
     *
     * @param position data list position for getting date
     */
    private void setCalendarYearTitle(int position) {

        Calendar currentCalendarMonth = dataList.get(position);
        String dateText = new DateFormatSymbols(locale).getMonths()[currentCalendarMonth.get(Calendar.MONTH)];
        dateText = dateText.substring(0, 1).toUpperCase() + dateText.subSequence(1, dateText.length());

        String yearTitle = dateText + " " + currentCalendarMonth.get(Calendar.YEAR);

        tvYearTitle.setText(yearTitle);
        tvYearTitle.setTextColor(calendarStyleAttr.getTitleColor());

    }

    /**
     * To set calendar listener
     *
     * @param calendarListener Listener
     */
    public void setCalendarListener(final CalendarListener calendarListener) {
        adapterEventCalendarMonths.setCalendarListener(calendarListener);
    }

    /**
     * To apply custom fonts to all the text views
     *
     * @param fonts - Typeface that you want to apply
     */
    public void setFonts(Typeface fonts) {
        tvYearTitle.setTypeface(fonts);
        calendarStyleAttr.setFonts(fonts);
        adapterEventCalendarMonths.invalidateCalendar();
    }

    /**
     * To remove all selection and redraw current calendar
     */
    public void resetAllSelectedViews() {
        adapterEventCalendarMonths.resetAllSelectedViews();
    }

    /**
     * To set week offset. To start week from any of the day. Default is 0 (Sunday).
     *
     * @param offset 0-Sun, 1-Mon, 2-Tue, 3-Wed, 4-Thu, 5-Fri, 6-Sat
     */
    public void setWeekOffset(int offset) {
        calendarStyleAttr.setWeekOffset(offset);
        adapterEventCalendarMonths.invalidateCalendar();
    }

    public void setEnableDayBeforeSelection(boolean enableDayBeforeSelection) {
        calendarStyleAttr.setEnableDayBeforeSelection(enableDayBeforeSelection);
    }


    public void setEnableDayAfterSelection(boolean enableDayAfterSelection) {
        calendarStyleAttr.setEnableDayAfterSelection(enableDayAfterSelection);
    }

    public void setRangeSelectedDate(@Nullable Calendar startDate, @Nullable Calendar endDate) {
        adapterEventCalendarMonths.setRangeSelectedDate(startDate, endDate);
    }

    public void updateDataDescription(HashMap<Long, String> data) {
        adapterEventCalendarMonths.setDataDescription(data);
    }

    public void setDateDisplayed(Calendar calendar) {
        dateDefaultDisplayed = calendar;
        initDataCalendar();
        vpCalendar.setCurrentItem(TOTAL_ALLOWED_MONTHS);
        setCalendarYearTitle(TOTAL_ALLOWED_MONTHS);
    }

    public void invalidateCalendar() {
        adapterEventCalendarMonths.invalidateCalendar();
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }



    public interface CalendarListener {
        void onFirstDateSelected(Calendar startDate);

        void onDateRangeSelected(Calendar startDate, Calendar endDate);

        void onDrawCompelete();
    }

    public interface OnPageChangeListener {
        void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

        void onPageSelected(int position);

        void onPageScrollStateChanged(int state);
    }
}
