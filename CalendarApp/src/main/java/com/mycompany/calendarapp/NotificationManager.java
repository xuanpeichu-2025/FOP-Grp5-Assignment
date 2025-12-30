package com.mycompany.calendarapp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    public static void checkAndDisplayNotifications(EventManager manager) {
        LocalDateTime now = LocalDateTime.now();
        List<MainEvent> upcomingEvents = new ArrayList<>();

        // Find events that should trigger notifications
        for (MainEvent event : manager.getAllEvents()) {
            if (event.getStartDateTime().isAfter(now)) {
                // Check if event is within reminder time
                if (event.getReminder() != null) {
                    long minutesUntilEvent = ChronoUnit.MINUTES.between(now, event.getStartDateTime());
                    if (minutesUntilEvent <= event.getReminder().getMinutesBefore() + 1) {
                        upcomingEvents.add(event);
                    }
                } else {
                    // Default: show events happening today or within 24 hours
                    long hoursUntilEvent = ChronoUnit.HOURS.between(now, event.getStartDateTime());
                    if (hoursUntilEvent >= 0 && hoursUntilEvent <= 24) {
                        upcomingEvents.add(event);
                    }
                }
            }
        }

        // Display notifications
        if (!upcomingEvents.isEmpty()) {
            System.out.println("\n========== UPCOMING EVENT REMINDERS ==========");
            
            for (MainEvent event : upcomingEvents) {
                long minutesUntilEvent = ChronoUnit.MINUTES.between(now, event.getStartDateTime());
                String durationText = formatDuration(minutesUntilEvent);
                System.out.println("[*] " + event.getTitle() + " is coming soon in " + durationText);
                if (event.getReminder() != null) {
                    System.out.println("    Reminder: " + event.getReminder().getDisplayText());
                }
            }
            System.out.println();
        }
    }

    public static String formatDuration(long minutes) {
        if (minutes < 1) {
            return "moments";
        } else if (minutes < 60) {
            return minutes + " minute" + (minutes > 1 ? "s" : "");
        } else if (minutes < 1440) {
            long hours = minutes / 60;
            return hours + " hour" + (hours > 1 ? "s" : "");
        } else {
            long days = minutes / 1440;
            return days + " day" + (days > 1 ? "s" : "");
        }
    }
}
