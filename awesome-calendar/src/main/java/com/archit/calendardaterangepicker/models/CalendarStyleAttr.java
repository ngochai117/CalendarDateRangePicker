package com.archit.calendardaterangepicker.models;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;

import com.archit.calendardaterangepicker.R;

public class CalendarStyleAttr {
    public static int VIEW_MODE_RANGE_SELECTION = 0;
    public static int VIEW_MODE_NONE_SELECTION = 1;
    private Typeface fonts;
    private Drawable headerBg;

    private int titleColor;

    private int weekColor;
    private int rangeStripColor;
    private int selectedDateCircleColor;
    private int selectedDateColor, defaultDateColor, disableDateColor, rangeDateColor;
    private float textSizeTitle, textSizeWeek, textSizeDate;
    private boolean shouldEnabledTime = false;
    private int weekOffset = 0;
    private int todayColor;
    private int todayCircleColor;
    private boolean enableDayBeforeSelection = false;
    private boolean enableDayAfterSelection = true;
    private int calendarViewMode = 0;

    private int descriptionBeforeColor;
    private int descriptionAfterColor;

    public CalendarStyleAttr(Context context) {
        setDefAttributes(context);
    }

    public CalendarStyleAttr(Context context, AttributeSet attributeSet) {
        setDefAttributes(context);
        setAttributes(context, attributeSet);
    }

    /**
     * To parse attributes from xml layout to configure calendar views.
     */
    public static CalendarStyleAttr getDefAttributes(Context context) {

        CalendarStyleAttr calendarStyleAttr = new CalendarStyleAttr(context);
        calendarStyleAttr.setTextSizeTitle(context.getResources().getDimension(R.dimen.text_size_title));
        calendarStyleAttr.setTextSizeWeek(context.getResources().getDimension(R.dimen.text_size_week));
        calendarStyleAttr.setTextSizeDate(context.getResources().getDimension(R.dimen.text_size_date));

        calendarStyleAttr.setWeekColor(ContextCompat.getColor(context, R.color.week_color));
        calendarStyleAttr.setRangeStripColor(ContextCompat.getColor(context, R.color.range_bg_color));
        calendarStyleAttr.setSelectedDateCircleColor(ContextCompat.getColor(context, R.color.selected_date_circle_color));
        calendarStyleAttr.setSelectedDateColor(ContextCompat.getColor(context, R.color.selected_date_color));
        calendarStyleAttr.setDefaultDateColor(ContextCompat.getColor(context, R.color.default_date_color));
        calendarStyleAttr.setRangeDateColor(ContextCompat.getColor(context, R.color.range_date_color));
        calendarStyleAttr.setDisableDateColor(ContextCompat.getColor(context, R.color.disable_date_color));

        calendarStyleAttr.setTodayColor(ContextCompat.getColor(context, R.color.today_color));
        calendarStyleAttr.setTodayCircleColor(ContextCompat.getColor(context, R.color.today_circle_color));

        calendarStyleAttr.setDescriptionBeforeColor(ContextCompat.getColor(context, R.color.description_color));
        calendarStyleAttr.setDescriptionAfterColor(ContextCompat.getColor(context, R.color.description_color));

        return calendarStyleAttr;
    }

    /**
     * To parse attributes from xml layout to configure calendar views.
     */
    public void setDefAttributes(Context context) {

        setTextSizeTitle(context.getResources().getDimension(R.dimen.text_size_title));
        setTextSizeWeek(context.getResources().getDimension(R.dimen.text_size_week));
        setTextSizeDate(context.getResources().getDimension(R.dimen.text_size_date));

        setTitleColor(ContextCompat.getColor(context, R.color.title_color));
        setWeekColor(ContextCompat.getColor(context, R.color.week_color));
        setRangeStripColor(ContextCompat.getColor(context, R.color.range_bg_color));
        setSelectedDateCircleColor(ContextCompat.getColor(context, R.color.selected_date_circle_color));
        setSelectedDateColor(ContextCompat.getColor(context, R.color.selected_date_color));
        setDefaultDateColor(ContextCompat.getColor(context, R.color.default_date_color));
        setRangeDateColor(ContextCompat.getColor(context, R.color.range_date_color));
        setDisableDateColor(ContextCompat.getColor(context, R.color.disable_date_color));

        setTodayColor(ContextCompat.getColor(context, R.color.today_color));
        setTodayCircleColor(ContextCompat.getColor(context, R.color.today_circle_color));

        setDescriptionBeforeColor(ContextCompat.getColor(context, R.color.description_color));
        setDescriptionAfterColor(ContextCompat.getColor(context, R.color.description_color));
    }

    public void setAttributes(Context context, AttributeSet attributeSet) {
        if (attributeSet != null) {
            TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.DateRangeMonthView, 0, 0);
            try {
                titleColor = ta.getColor(R.styleable.DateRangeMonthView_title_color, titleColor);
                headerBg = ta.getDrawable(R.styleable.DateRangeMonthView_header_bg);
                weekColor = ta.getColor(R.styleable.DateRangeMonthView_week_color, weekColor);
                rangeStripColor = ta.getColor(R.styleable.DateRangeMonthView_range_color, rangeStripColor);
                selectedDateCircleColor = ta.getColor(R.styleable.DateRangeMonthView_selected_date_circle_color, selectedDateCircleColor);
                shouldEnabledTime = ta.getBoolean(R.styleable.DateRangeMonthView_enable_time_selection, false);

                textSizeTitle = ta.getDimension(R.styleable.DateRangeMonthView_text_size_title, textSizeTitle);
                textSizeWeek = ta.getDimension(R.styleable.DateRangeMonthView_text_size_week, textSizeWeek);
                textSizeDate = ta.getDimension(R.styleable.DateRangeMonthView_text_size_date, textSizeDate);

                selectedDateColor = ta.getColor(R.styleable.DateRangeMonthView_selected_date_color, selectedDateColor);
                defaultDateColor = ta.getColor(R.styleable.DateRangeMonthView_default_date_color, defaultDateColor);
                rangeDateColor = ta.getColor(R.styleable.DateRangeMonthView_range_date_color, rangeDateColor);
                disableDateColor = ta.getColor(R.styleable.DateRangeMonthView_disable_date_color, disableDateColor);
                setWeekOffset(ta.getColor(R.styleable.DateRangeMonthView_week_offset, 0));

                todayColor = ta.getColor(R.styleable.DateRangeMonthView_today_color, todayColor);
                todayCircleColor = ta.getColor(R.styleable.DateRangeMonthView_today_circle_color, todayCircleColor);
                enableDayBeforeSelection = ta.getBoolean(R.styleable.DateRangeMonthView_enable_day_before_selection, false);
                enableDayAfterSelection = ta.getBoolean(R.styleable.DateRangeMonthView_enable_day_after_selection, true);

                calendarViewMode = ta.getInt(R.styleable.DateRangeMonthView_calendar_view_mode, VIEW_MODE_RANGE_SELECTION);

                descriptionBeforeColor = ta.getColor(R.styleable.DateRangeMonthView_description_before_color, descriptionBeforeColor);
                descriptionAfterColor = ta.getColor(R.styleable.DateRangeMonthView_description_after_color, descriptionAfterColor);
            } finally {
                ta.recycle();
            }
        }
    }

    public Typeface getFonts() {
        return fonts;
    }

    public void setFonts(Typeface fonts) {
        this.fonts = fonts;
    }

    public int getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(int titleColor) {
        this.titleColor = titleColor;
    }

    public Drawable getHeaderBg() {
        return headerBg;
    }

    public void setHeaderBg(Drawable headerBg) {
        this.headerBg = headerBg;
    }

    public int getWeekColor() {
        return weekColor;
    }

    public void setWeekColor(int weekColor) {
        this.weekColor = weekColor;
    }

    public int getRangeStripColor() {
        return rangeStripColor;
    }

    public void setRangeStripColor(int rangeStripColor) {
        this.rangeStripColor = rangeStripColor;
    }

    public int getSelectedDateCircleColor() {
        return selectedDateCircleColor;
    }

    public void setSelectedDateCircleColor(int selectedDateCircleColor) {
        this.selectedDateCircleColor = selectedDateCircleColor;
    }

    public int getSelectedDateColor() {
        return selectedDateColor;
    }

    public void setSelectedDateColor(int selectedDateColor) {
        this.selectedDateColor = selectedDateColor;
    }

    public int getDefaultDateColor() {
        return defaultDateColor;
    }

    public void setDefaultDateColor(int defaultDateColor) {
        this.defaultDateColor = defaultDateColor;
    }

    public int getDisableDateColor() {
        return disableDateColor;
    }

    public void setDisableDateColor(int disableDateColor) {
        this.disableDateColor = disableDateColor;
    }

    public int getRangeDateColor() {
        return rangeDateColor;
    }

    public void setRangeDateColor(int rangeDateColor) {
        this.rangeDateColor = rangeDateColor;
    }

    public float getTextSizeTitle() {
        return textSizeTitle;
    }

    public void setTextSizeTitle(float textSizeTitle) {
        this.textSizeTitle = textSizeTitle;
    }

    public float getTextSizeWeek() {
        return textSizeWeek;
    }

    public void setTextSizeWeek(float textSizeWeek) {
        this.textSizeWeek = textSizeWeek;
    }

    public float getTextSizeDate() {
        return textSizeDate;
    }

    public void setTextSizeDate(float textSizeDate) {
        this.textSizeDate = textSizeDate;
    }

    public boolean isShouldEnabledTime() {
        return shouldEnabledTime;
    }

    public void setShouldEnabledTime(boolean shouldEnabledTime) {
        this.shouldEnabledTime = shouldEnabledTime;
    }

    public int getWeekOffset() {
        return weekOffset;
    }

    public int getTodayColor() {
        return todayColor;
    }

    public void setTodayColor(int todayColor) {
        this.todayColor = todayColor;
    }

    public int getTodayCircleColor() {
        return todayCircleColor;
    }

    public void setTodayCircleColor(int todayCircleColor) {
        this.todayCircleColor = todayCircleColor;
    }

    public boolean isEnableDayBeforeSelection() {
        return enableDayBeforeSelection;
    }

    public void setEnableDayBeforeSelection(boolean enableDayBeforeSelection) {
        this.enableDayBeforeSelection = enableDayBeforeSelection;
    }

    public boolean isEnableDayAfterSelection() {
        return enableDayAfterSelection;
    }

    public void setEnableDayAfterSelection(boolean enableDayAfterSelection) {
        this.enableDayAfterSelection = enableDayAfterSelection;
    }

    public int getCalendarViewMode() {
        return calendarViewMode;
    }

    public void setCalendarViewMode(int calendarViewMode) {
        this.calendarViewMode = calendarViewMode;
    }

    public int getDescriptionBeforeColor() {
        return descriptionBeforeColor;
    }

    public void setDescriptionBeforeColor(int descriptionBeforeColor) {
        this.descriptionBeforeColor = descriptionBeforeColor;
    }

    public int getDescriptionAfterColor() {
        return descriptionAfterColor;
    }

    public void setDescriptionAfterColor(int descriptionAfterColor) {
        this.descriptionAfterColor = descriptionAfterColor;
    }

    /**
     * To set week offset
     *
     * @param weekOffset
     */
    public void setWeekOffset(int weekOffset) {
        if (weekOffset < 0 || weekOffset > 6) {
            throw new RuntimeException("Week offset can only be between 0 to 6. " +
                    "0->Sun, 1->Mon, 2->Tue, 3->Wed, 4->Thu, 5->Fri, 6->Sat");
        }
        this.weekOffset = weekOffset;
    }
}
