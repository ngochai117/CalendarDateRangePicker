package com.archit.calendardaterangepicker.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.archit.calendardaterangepicker.R;
import com.archit.calendardaterangepicker.customviews.DateRangeCalendarView;
import com.archit.calendardaterangepicker.customviews.DateRangeMonthView;
import com.archit.calendardaterangepicker.manager.DateRangeCalendarManager;
import com.archit.calendardaterangepicker.models.CalendarStyleAttr;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class AdapterEventCalendarMonths extends PagerAdapter {

    private Context mContext;
    private List<Calendar> dataList;
    private CalendarStyleAttr calendarStyleAttr;
    private DateRangeCalendarView.CalendarListener calendarListener;
    private DateRangeCalendarManager dateRangeCalendarManager;
    private Handler mHandler;
    private HashMap<Long, String> hashMapDescription = new HashMap<>();
    private DateRangeCalendarView.CustomCellInterface customCellInterface;

    public AdapterEventCalendarMonths(Context mContext, List<Calendar> list, CalendarStyleAttr calendarStyleAttr) {
        this.mContext = mContext;
        dataList = list;
        this.calendarStyleAttr = calendarStyleAttr;
        dateRangeCalendarManager = new DateRangeCalendarManager();
        mHandler = new Handler();
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        if (position == DateRangeCalendarView.TOTAL_ALLOWED_MONTHS + 1) {
            if (calendarListener != null) {
                calendarListener.onDrawCompelete();
            }
        }
        Calendar modelObject = dataList.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.layout_pager_month, container, false);

        DateRangeMonthView dateRangeMonthView = layout.findViewById(R.id.cvEventCalendarView);
        dateRangeMonthView.drawCalendarForMonth(calendarStyleAttr, getMonth(modelObject), hashMapDescription, dateRangeCalendarManager);
        dateRangeMonthView.setCalendarListener(calendarAdapterListener);
        dateRangeMonthView.setCustomCellInterface(customCellInterface);
        container.addView(layout);

        return layout;
    }

    /**
     * To clone calendar obj and get month calendar starting from 1st date.
     *
     * @param calendar - Calendar
     * @return - Modified calendar obj of month of 1st date.
     */
    private Calendar getMonth(Calendar calendar) {
        Calendar month = (Calendar) calendar.clone();
        month.set(Calendar.DAY_OF_MONTH, 1);
        month.set(Calendar.HOUR_OF_DAY, 0);
        month.set(Calendar.MINUTE, 0);
        month.set(Calendar.SECOND, 0);
        month.set(Calendar.MILLISECOND, 0);
        return month;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }


    private DateRangeCalendarView.CalendarListener calendarAdapterListener = new DateRangeCalendarView.CalendarListener() {
        @Override
        public void onFirstDateSelected(Calendar startDate) {

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 50);


            if (calendarListener != null) {
                calendarListener.onFirstDateSelected(startDate);
            }
        }

        @Override
        public void onDateRangeSelected(Calendar startDate, Calendar endDate) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            }, 50);
            if (calendarListener != null) {
                calendarListener.onDateRangeSelected(startDate, endDate);
            }
        }

        @Override
        public void onDrawCompelete() {
            if (calendarListener != null) {
                calendarListener.onDrawCompelete();
            }
        }
    };

    public void setCalendarListener(DateRangeCalendarView.CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    /**
     * To redraw calendar.
     */
    public void invalidateCalendar() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 50);
    }

    /**
     * To remove all selection and redraw current calendar
     */
    public void resetAllSelectedViews() {
        dateRangeCalendarManager.setMinSelectedDate(null);
        dateRangeCalendarManager.setMaxSelectedDate(null);
        notifyDataSetChanged();
    }

    public void setRangeSelectedDate(@Nullable Calendar startDate, @Nullable Calendar endDate) {
        dateRangeCalendarManager.setMinSelectedDate(startDate);
        dateRangeCalendarManager.setMaxSelectedDate(endDate);
        notifyDataSetChanged();
    }

    public void setDataDescription(HashMap<Long, String> hashMapDescription) {
        this.hashMapDescription = hashMapDescription;
    }

    public void setCustomCellInterface(DateRangeCalendarView.CustomCellInterface customCellInterface) {
        this.customCellInterface = customCellInterface;
    }
}
