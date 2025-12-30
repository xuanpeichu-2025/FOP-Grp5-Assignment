package com.mycompany.calendarapp;

public class Reminder {
    private int minutesBefore;  // How many minutes before the event to remind

    public Reminder(int minutesBefore) {
        this.minutesBefore = minutesBefore;
    }

    public int getMinutesBefore() {
        return minutesBefore;
    }

    public void setMinutesBefore(int minutesBefore) {
        this.minutesBefore = minutesBefore;
    }

    public String getDisplayText() {
        if (minutesBefore < 60) {
            return minutesBefore + " minutes before";
        } else if (minutesBefore == 1440) {
            return "1 day before";
        } else if (minutesBefore % 1440 == 0) {
            return (minutesBefore / 1440) + " days before";
        } else if (minutesBefore % 60 == 0) {
            return (minutesBefore / 60) + " hours before";
        }
        return minutesBefore + " minutes before";
    }

    @Override
    public String toString() {
        return getDisplayText();
    }
}
