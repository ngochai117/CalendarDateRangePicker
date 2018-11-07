package com.archit.calendardaterangepicker.models;

import java.util.Calendar;

public class DayModel {
    private Calendar calendar;

    private String description;

    public DayModel(Calendar calendar, String description) {
        this.calendar = calendar;
        this.description = description;
    }

    public DayModel(Calendar calendar) {
        this.calendar = calendar;
        this.description = "";
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
