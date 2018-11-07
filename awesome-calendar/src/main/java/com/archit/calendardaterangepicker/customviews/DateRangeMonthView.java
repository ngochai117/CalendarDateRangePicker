package com.archit.calendardaterangepicker.customviews;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.manager.DateRangeCalendarManager;
import com.archit.calendardaterangepicker.models.CalendarStyleAttr;
import com.archit.calendardaterangepicker.models.DayContainer;
import com.archit.calendardaterangepicker.timepicker.AwesomeTimePickerDialog;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by archit.shah on 08/09/2017.
 */

public class DateRangeMonthView extends LinearLayout {

    private static final String LOG_TAG = DateRangeMonthView.class.getSimpleName();
    private Context mContext;
    private LinearLayout llDaysContainer;
    private LinearLayout llTitleWeekContainer;

    private Calendar currentCalendarMonth;

    private CalendarStyleAttr calendarStyleAttr;

    private DateRangeCalendarView.CalendarListener calendarListener;

    private DateRangeCalendarManager dateRangeCalendarManager;

    private List<Calendar> listCalendarSelected = new ArrayList<>();

    private HashMap<Long, String> hashMapDescription = new HashMap<>();

    public void setCalendarListener(DateRangeCalendarView.CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    public DateRangeMonthView(Context context) {
        super(context);
        initView(context, null);
    }

    public DateRangeMonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public DateRangeMonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DateRangeMonthView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    /**
     * To initialize child views
     *
     * @param context      - App context
     * @param attributeSet - Attr set
     */
    private void initView(Context context, AttributeSet attributeSet) {
        mContext = context;

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        LinearLayout mainView = (LinearLayout) layoutInflater.inflate(R.layout.layout_calendar_month, this, true);
        llDaysContainer = (LinearLayout) mainView.findViewById(R.id.llDaysContainer);
        llTitleWeekContainer = (LinearLayout) mainView.findViewById(R.id.llTitleWeekContainer);

        setListeners();

        if (isInEditMode()) {
            return;
        }

    }


    /**
     * To set listeners.
     */
    private void setListeners() {


    }


    private OnClickListener dayClickListener = new OnClickListener() {
        @Override
        public void onClick(final View view) {
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int key = (int) view.getTag();
                        final Calendar selectedCal = Calendar.getInstance();
                        Date date = new Date();
                        try {
                            date = DateRangeCalendarManager.SIMPLE_DATE_FORMAT.parse(String.valueOf(key));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        selectedCal.setTime(date);

                        Calendar minSelectedDate = dateRangeCalendarManager.getMinSelectedDate();
                        Calendar maxSelectedDate = dateRangeCalendarManager.getMaxSelectedDate();

                        if (minSelectedDate != null && maxSelectedDate == null) {
                            maxSelectedDate = selectedCal;

                            int startDateKey = DayContainer.GetContainerKey(minSelectedDate);
                            int lastDateKey = DayContainer.GetContainerKey(maxSelectedDate);

                            if (startDateKey == lastDateKey) {
                                minSelectedDate = maxSelectedDate;
                            } else if (startDateKey > lastDateKey) {
                                Calendar temp = (Calendar) minSelectedDate.clone();
                                minSelectedDate = maxSelectedDate;
                                maxSelectedDate = temp;
                            }
                        } else if (maxSelectedDate == null) {
                            //This will call one time only
                            minSelectedDate = selectedCal;
                        } else {
                            minSelectedDate = selectedCal;
                            maxSelectedDate = null;
                        }

                        dateRangeCalendarManager.setMinSelectedDate(minSelectedDate);
                        dateRangeCalendarManager.setMaxSelectedDate(maxSelectedDate);

                        final Calendar finalMinSelectedDate = minSelectedDate;
                        final Calendar finalMaxSelectedDate = maxSelectedDate;
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            drawCalendarForMonth(currentCalendarMonth);
                                        }
                                    });
                                }
                            }).start();
                        } catch (Exception e) {
                            drawCalendarForMonth(currentCalendarMonth);
                        }
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (calendarStyleAttr.isShouldEnabledTime()) {
                                                AwesomeTimePickerDialog awesomeTimePickerDialog = new AwesomeTimePickerDialog(mContext, mContext.getString(R.string.select_time), new AwesomeTimePickerDialog.TimePickerCallback() {
                                                    @Override
                                                    public void onTimeSelected(int hours, int mins) {
                                                        selectedCal.set(Calendar.HOUR, hours);
                                                        selectedCal.set(Calendar.MINUTE, mins);

                                                        Log.i(LOG_TAG, "Time: " + selectedCal.getTime().toString());
                                                        if (calendarListener != null) {

                                                            if (finalMaxSelectedDate != null) {
                                                                calendarListener.onDateRangeSelected(finalMinSelectedDate, finalMaxSelectedDate);
                                                            } else {
                                                                calendarListener.onFirstDateSelected(finalMinSelectedDate);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancel() {
                                                        DateRangeMonthView.this.resetAllSelectedViews();
                                                    }
                                                });
                                                awesomeTimePickerDialog.showDialog();
                                            } else {
                                                Log.i(LOG_TAG, "Time: " + selectedCal.getTime().toString());
                                                if (finalMaxSelectedDate != null) {
                                                    calendarListener.onDateRangeSelected(finalMinSelectedDate, finalMaxSelectedDate);
                                                } else {
                                                    calendarListener.onFirstDateSelected(finalMinSelectedDate);
                                                }
                                            }
                                        }
                                    });
                                }
                            }).start();
                        } catch (Exception e) {

                        }

                    }
                }).start();
            } catch (Exception e) {
            }


        }
    };

    /**
     * To draw calendar for the given month. Here calendar object should start from date of 1st.
     *
     * @param calendarStyleAttr        Calendar style attributes
     * @param month                    Month to be drawn
     * @param dateRangeCalendarManager Calendar data manager
     */
    public void drawCalendarForMonth(CalendarStyleAttr calendarStyleAttr, Calendar month, HashMap<Long, String> hashMapDescription, DateRangeCalendarManager dateRangeCalendarManager) {
        this.calendarStyleAttr = calendarStyleAttr;
        this.currentCalendarMonth = (Calendar) month.clone();
        this.hashMapDescription = hashMapDescription;
        this.dateRangeCalendarManager = dateRangeCalendarManager;
        setFonts();
        setWeekTitleColor(calendarStyleAttr.getWeekColor());
        drawCalendarForMonth(currentCalendarMonth);
    }

    /**
     * To draw calendar for the given month. Here calendar object should start from date of 1st.
     *
     * @param month Calendar month
     */
    private void drawCalendarForMonth(Calendar month) {
        Log.v(LOG_TAG, "Current cal: " + month.getTime().toString());
        currentCalendarMonth = (Calendar) month.clone();
        currentCalendarMonth.set(Calendar.DATE, 1);

        String[] weekTitle = mContext.getResources().getStringArray(R.array.week_sun_sat);

        //To set week day title as per offset
        for (int i = 0; i < 7; i++) {

            CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);

            String weekStr = weekTitle[(i + calendarStyleAttr.getWeekOffset()) % 7];
            textView.setText(weekStr);

        }

        int startDay = month.get(Calendar.DAY_OF_WEEK) - calendarStyleAttr.getWeekOffset();

        //To ratate week day according to offset
        if (startDay < 1) {
            startDay = startDay + 7;
        }

        month.add(Calendar.DATE, -startDay + 1);
        LinearLayout weekRow;
        ConstraintLayout ctlDayContainer;
        DayContainer container;
        for (int i = 0; i < llDaysContainer.getChildCount(); i++) {
            weekRow = (LinearLayout) llDaysContainer.getChildAt(i);
            for (int j = 0; j < 7; j++) {
                ctlDayContainer = (ConstraintLayout) weekRow.getChildAt(j);

                container = new DayContainer(ctlDayContainer);

                //container.tvDate.setText(String.valueOf(month.get(Calendar.DATE)));
                if (calendarStyleAttr.getFonts() != null) {
                    container.tvDate.setTypeface(calendarStyleAttr.getFonts());
                }
                Log.v(LOG_TAG, "Date: " + month.getTime().toString());
                drawDayContainer(container, month);
                month.add(Calendar.DATE, 1);
            }
        }
    }

    /**
     * To draw specific date container according to past date, today, selected or from range.
     *
     * @param container - Date container
     * @param calendar  - Calendar obj of specific date of the month.
     */
    private void drawDayContainer(DayContainer container, Calendar calendar) {
        Log.d(LOG_TAG, "drawDayContainer: "+calendar.getTime());
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        //int date = calendar.get(Calendar.DATE);

        StateDate stateDate = StateDate.TODAY;

        if (calendar.after(today)) {
            stateDate = StateDate.AFTER;
        }
        if (calendar.before(today)) {
            stateDate = StateDate.BEFORE;
        }

        if ((today.get(Calendar.DAY_OF_YEAR) == calendar.get(Calendar.DAY_OF_YEAR)) && today.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
            stateDate = StateDate.TODAY;
        }

        if (currentCalendarMonth.get(Calendar.MONTH) != calendar.get(Calendar.MONTH)) {
            hideDayContainer(container);
        } else if ((!calendarStyleAttr.isEnableDayAfterSelection() && stateDate == StateDate.AFTER) /*&& (today.get(Calendar.DAY_OF_YEAR) != calendar.get(Calendar.DAY_OF_YEAR))*/
                || (!calendarStyleAttr.isEnableDayBeforeSelection() && stateDate == StateDate.BEFORE)) {
            disableDayContainer(container, calendar, stateDate);
        } else {
            if (calendarStyleAttr.getCalendarViewMode() == CalendarStyleAttr.VIEW_MODE_RANGE_SELECTION) {
                @DateRangeCalendarManager.RANGE_TYPE
                int type = dateRangeCalendarManager.checkDateRange(calendar);
                if (type == DateRangeCalendarManager.RANGE_TYPE.START_DATE || type == DateRangeCalendarManager.RANGE_TYPE.LAST_DATE) {
                    makeAsSelectedDate(container, calendar, stateDate, type);
                } else if (type == DateRangeCalendarManager.RANGE_TYPE.MIDDLE_DATE) {
                    makeAsRangeDate(container, calendar, stateDate);
                } else {
                    enabledDayContainer(container, calendar, stateDate, CalendarStyleAttr.VIEW_MODE_RANGE_SELECTION);
                }
            } else {
                enabledDayContainer(container, calendar, stateDate, CalendarStyleAttr.VIEW_MODE_NONE_SELECTION);
            }
        }
        container.rootView.setTag(DayContainer.GetContainerKey(calendar));
    }

    private void setTextDescription(TextView tv, Calendar calendar, StateDate stateDate) {
        Log.d(getClass().getSimpleName(), "hashMapDescription: " + hashMapDescription.toString());
        Log.d(getClass().getSimpleName(), "\ncalendar: " + calendar.getTime()+calendar.getTimeInMillis());
        String description = hashMapDescription.get(calendar.getTimeInMillis());
        if (TextUtils.isEmpty(description)) {
            description = "";
        }
        Log.d(getClass().getSimpleName(), "description: "+description);
        tv.setText(description);
        tv.setTextColor(stateDate == StateDate.AFTER ?
                calendarStyleAttr.getDescriptionAfterColor() : calendarStyleAttr.getDescriptionBeforeColor());
    }

    /**
     * To hide date if date is from previous month.
     *
     * @param container - Container
     */
    private void hideDayContainer(DayContainer container) {
        /*container.tvDate.setText("");
        container.tvDescription.setText("");
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);*/
        container.rootView.setVisibility(INVISIBLE);
        container.rootView.setOnClickListener(null);
    }

    /**
     * To disable past date. Click listener will be removed.
     *
     * @param container - Container
     */
    private void disableDayContainer(DayContainer container, Calendar calendar, StateDate stateDate) {
        container.tvDate.setText(String.valueOf(calendar.get(Calendar.DATE)));
        setTextDescription(container.tvDescription, calendar, stateDate);
        container.tvDate.setBackgroundColor(Color.TRANSPARENT);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.tvDate.setTextColor(calendarStyleAttr.getDisableDateColor());
        container.rootView.setVisibility(VISIBLE);
        container.rootView.setOnClickListener(null);
    }

    /**
     * To enable date by enabling click listeners.
     *
     * @param container - Container
     */
    private void enabledDayContainer(DayContainer container, Calendar calendar, StateDate stateDate, int viewMode) {
        if (stateDate == StateDate.TODAY) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.today_circle);
            mDrawable.setColor(Color.TRANSPARENT);
            mDrawable.setStroke(mContext.getResources().getDimensionPixelSize(R.dimen.today_stroke_size),
                    calendarStyleAttr.getTodayCircleColor());
            container.tvDate.setBackground(mDrawable);
            container.tvDate.setTextColor(calendarStyleAttr.getTodayColor());
        } else {
            container.tvDate.setBackgroundColor(Color.TRANSPARENT);
            container.tvDate.setTextColor(calendarStyleAttr.getDefaultDateColor());
        }

        container.tvDate.setText(String.valueOf(calendar.get(Calendar.DATE)));
        setTextDescription(container.tvDescription, calendar, stateDate);
        container.strip.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setVisibility(VISIBLE);
        if (viewMode == CalendarStyleAttr.VIEW_MODE_RANGE_SELECTION) {
            container.rootView.setOnClickListener(dayClickListener);
        } else {
            container.rootView.setOnClickListener(null);
        }
    }

    /**
     * To draw date container as selected as end selection or middle selection.
     *
     * @param container - Container
     * @param stripType - Right end date, Left end date or middle
     */
    private void makeAsSelectedDate(DayContainer container, Calendar calendar, StateDate stateDate, @DateRangeCalendarManager.RANGE_TYPE int stripType) {
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) container.strip.getLayoutParams();

        Calendar minDate = dateRangeCalendarManager.getMinSelectedDate();
        Calendar maxDate = dateRangeCalendarManager.getMaxSelectedDate();

        if (stripType == DateRangeCalendarManager.RANGE_TYPE.START_DATE && maxDate != null &&
                minDate.compareTo(maxDate) != 0) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg_left);
            mDrawable.setColor(calendarStyleAttr.getRangeStripColor());
            container.strip.setBackground(mDrawable);
            layoutParams.setMargins(20, 0, 0, 0);
        } else if (stripType == DateRangeCalendarManager.RANGE_TYPE.LAST_DATE) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg_right);
            mDrawable.setColor(calendarStyleAttr.getRangeStripColor());
            container.strip.setBackground(mDrawable);
            layoutParams.setMargins(0, 0, 20, 0);
        } else {
            container.strip.setBackgroundColor(Color.TRANSPARENT);
            layoutParams.setMargins(0, 0, 0, 0);
        }
        container.strip.setLayoutParams(layoutParams);
        if (stateDate == StateDate.TODAY) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.today_circle);
            mDrawable.setColor(calendarStyleAttr.getSelectedDateCircleColor());
            mDrawable.setStroke(mContext.getResources().getDimensionPixelSize(R.dimen.today_stroke_size),
                    calendarStyleAttr.getTodayCircleColor());
            container.tvDate.setBackground(mDrawable);
            container.tvDate.setTextColor(calendarStyleAttr.getTodayColor());
        } else {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.green_circle);
            mDrawable.setColor(calendarStyleAttr.getSelectedDateCircleColor());
            container.tvDate.setBackground(mDrawable);
            container.tvDate.setTextColor(calendarStyleAttr.getSelectedDateColor());
        }

        container.tvDate.setText(String.valueOf(calendar.get(Calendar.DATE)));
        setTextDescription(container.tvDescription, calendar, stateDate);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setVisibility(VISIBLE);
        container.rootView.setOnClickListener(dayClickListener);

    }

    /**
     * To draw date as middle date
     *
     * @param container - Container
     */
    private void makeAsRangeDate(DayContainer container, Calendar calendar, StateDate stateDate) {
        if (stateDate == StateDate.TODAY) {
            GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.today_circle);
            mDrawable.setColor(calendarStyleAttr.getRangeStripColor());
            mDrawable.setStroke(mContext.getResources().getDimensionPixelSize(R.dimen.today_stroke_size),
                    calendarStyleAttr.getTodayCircleColor());
            container.tvDate.setBackground(mDrawable);
            container.tvDate.setTextColor(calendarStyleAttr.getTodayColor());
        } else {
            container.tvDate.setBackgroundColor(Color.TRANSPARENT);
            container.tvDate.setTextColor(calendarStyleAttr.getRangeDateColor());
        }

        container.tvDate.setText(String.valueOf(calendar.get(Calendar.DATE)));
        setTextDescription(container.tvDescription, calendar, stateDate);
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(mContext, R.drawable.range_bg);
        mDrawable.setColor(calendarStyleAttr.getRangeStripColor());
        container.strip.setBackground(mDrawable);
        container.rootView.setBackgroundColor(Color.TRANSPARENT);
        container.rootView.setVisibility(VISIBLE);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) container.strip.getLayoutParams();
        layoutParams.setMargins(0, 0, 0, 0);
        container.strip.setLayoutParams(layoutParams);
        container.rootView.setOnClickListener(dayClickListener);
    }


    /**
     * To remove all selection and redraw current calendar
     */
    public void resetAllSelectedViews() {

        dateRangeCalendarManager.setMinSelectedDate(null);
        dateRangeCalendarManager.setMaxSelectedDate(null);

        drawCalendarForMonth(currentCalendarMonth);

    }


    /**
     * To set week title color
     *
     * @param color - resource color value
     */
    public void setWeekTitleColor(@ColorInt int color) {
        for (int i = 0; i < llTitleWeekContainer.getChildCount(); i++) {
            CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);
            textView.setTextColor(color);
        }
    }

    /**
     * To apply custom fonts to all the text views
     */
    private void setFonts() {
        if (calendarStyleAttr.getFonts() != null) {
            //drawCalendarForMonth(currentCalendarMonth);
            for (int i = 0; i < llTitleWeekContainer.getChildCount(); i++) {
                CustomTextView textView = (CustomTextView) llTitleWeekContainer.getChildAt(i);
                textView.setTypeface(calendarStyleAttr.getFonts());
            }
        }
    }

    enum StateDate {
        TODAY,
        BEFORE,
        AFTER
    }
}
