package com.mycompany.calendarapp;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

/**
 * Event Statistics Generator
 * Provides insights and analytics on events
 */
public class EventStatistics {

    /**
     * Get the busiest day of the week based on event count
     */
    public static Map.Entry<DayOfWeek, Integer> getBusiestDayOfWeek(List<MainEvent> events) {
        Map<DayOfWeek, Integer> dayCount = new HashMap<>();
        
        for (DayOfWeek day : DayOfWeek.values()) {
            dayCount.put(day, 0);
        }
        
        for (MainEvent event : events) {
            DayOfWeek day = event.getStartDateTime().getDayOfWeek();
            dayCount.put(day, dayCount.get(day) + 1);
        }
        
        return dayCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElse(null);
    }

    /**
     * Get busiest hour of the day (0-23)
     */
    public static Map.Entry<Integer, Integer> getBusiestHour(List<MainEvent> events) {
        Map<Integer, Integer> hourCount = new HashMap<>();
        
        for (int hour = 0; hour < 24; hour++) {
            hourCount.put(hour, 0);
        }
        
        for (MainEvent event : events) {
            int hour = event.getStartDateTime().getHour();
            hourCount.put(hour, hourCount.get(hour) + 1);
        }
        
        return hourCount.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .orElse(null);
    }

    /**
     * Get event distribution by month
     */
    public static Map<String, Integer> getEventsByMonth(List<MainEvent> events) {
        Map<String, Integer> monthCount = new LinkedHashMap<>();
        
        for (MainEvent event : events) {
            String monthYear = event.getStartDateTime().getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) 
                             + " " + event.getStartDateTime().getYear();
            monthCount.put(monthYear, monthCount.getOrDefault(monthYear, 0) + 1);
        }
        
        return monthCount;
    }

    /**
     * Get average events per week
     */
    public static double getAverageEventsPerWeek(List<MainEvent> events) {
        if (events.isEmpty()) return 0.0;
        
        LocalDate earliest = events.stream()
            .map(e -> e.getStartDateTime().toLocalDate())
            .min(LocalDate::compareTo)
            .orElse(LocalDate.now());
        
        LocalDate latest = events.stream()
            .map(e -> e.getStartDateTime().toLocalDate())
            .max(LocalDate::compareTo)
            .orElse(LocalDate.now());
        
        long totalDays = java.time.temporal.ChronoUnit.DAYS.between(earliest, latest);
        if (totalDays == 0) totalDays = 1;
        
        double weeks = totalDays / 7.0;
        if (weeks < 1) weeks = 1;
        
        return events.size() / weeks;
    }

    /**
     * Get total events count
     */
    public static int getTotalEvents(List<MainEvent> events) {
        return events.size();
    }

    /**
     * Get events with reminders count
     */
    public static int getEventsWithReminders(List<MainEvent> events) {
        return (int) events.stream()
            .filter(e -> e.getReminder() != null)
            .count();
    }

    /**
     * Get recurring events count
     */
    public static int getRecurringEvents(List<MainEvent> events) {
        return (int) events.stream()
            .filter(e -> e instanceof RecurringEvent)
            .count();
    }

    /**
     * Get upcoming events (future events)
     */
    public static int getUpcomingEventsCount(List<MainEvent> events) {
        LocalDateTime now = LocalDateTime.now();
        return (int) events.stream()
            .filter(e -> e.getStartDateTime().isAfter(now))
            .count();
    }

    /**
     * Get past events count
     */
    public static int getPastEventsCount(List<MainEvent> events) {
        LocalDateTime now = LocalDateTime.now();
        return (int) events.stream()
            .filter(e -> e.getEndDateTime().isBefore(now))
            .count();
    }

    /**
     * Get average event duration in minutes
     */
    public static double getAverageEventDuration(List<MainEvent> events) {
        if (events.isEmpty()) return 0.0;
        
        long totalMinutes = events.stream()
            .mapToLong(e -> java.time.temporal.ChronoUnit.MINUTES.between(
                e.getStartDateTime(), e.getEndDateTime()))
            .sum();
        
        return totalMinutes / (double) events.size();
    }

    /**
     * Get longest event
     */
    public static MainEvent getLongestEvent(List<MainEvent> events) {
        return events.stream()
            .max(Comparator.comparingLong(e -> java.time.temporal.ChronoUnit.MINUTES.between(
                e.getStartDateTime(), e.getEndDateTime())))
            .orElse(null);
    }

    /**
     * Get events per day of week distribution
     */
    public static Map<DayOfWeek, Integer> getEventsByDayOfWeek(List<MainEvent> events) {
        Map<DayOfWeek, Integer> dayCount = new LinkedHashMap<>();
        
        for (DayOfWeek day : DayOfWeek.values()) {
            dayCount.put(day, 0);
        }
        
        for (MainEvent event : events) {
            DayOfWeek day = event.getStartDateTime().getDayOfWeek();
            dayCount.put(day, dayCount.get(day) + 1);
        }
        
        return dayCount;
    }

    /**
     * Generate a full statistics report as a string
     */
    public static String generateStatisticsReport(List<MainEvent> events) {
        if (events.isEmpty()) {
            return "No events to analyze.";
        }

        StringBuilder report = new StringBuilder();
        report.append("📊 EVENT STATISTICS REPORT\n");
        report.append("=" .repeat(60)).append("\n\n");
        
        // Basic counts
        report.append("📈 OVERVIEW:\n");
        report.append(String.format("   Total Events: %d\n", getTotalEvents(events)));
        report.append(String.format("   Upcoming Events: %d\n", getUpcomingEventsCount(events)));
        report.append(String.format("   Past Events: %d\n", getPastEventsCount(events)));
        report.append(String.format("   Recurring Events: %d\n", getRecurringEvents(events)));
        report.append(String.format("   Events with Reminders: %d\n\n", getEventsWithReminders(events)));
        
        // Busiest day
        Map.Entry<DayOfWeek, Integer> busiestDay = getBusiestDayOfWeek(events);
        if (busiestDay != null) {
            report.append("🗓️ BUSIEST DAY OF WEEK:\n");
            report.append(String.format("   %s (%d events)\n\n",
                busiestDay.getKey().getDisplayName(TextStyle.FULL, Locale.ENGLISH),
                busiestDay.getValue()));
        }
        
        // Busiest hour
        Map.Entry<Integer, Integer> busiestHour = getBusiestHour(events);
        if (busiestHour != null) {
            report.append("⏰ BUSIEST HOUR OF DAY:\n");
            report.append(String.format("   %02d:00 (%d events)\n\n",
                busiestHour.getKey(), busiestHour.getValue()));
        }
        
        // Day of week distribution
        report.append("📅 EVENTS BY DAY OF WEEK:\n");
        Map<DayOfWeek, Integer> dayDist = getEventsByDayOfWeek(events);
        for (Map.Entry<DayOfWeek, Integer> entry : dayDist.entrySet()) {
            report.append(String.format("   %-10s: %3d events %s\n",
                entry.getKey().getDisplayName(TextStyle.SHORT, Locale.ENGLISH),
                entry.getValue(),
                "█".repeat(Math.min(entry.getValue(), 50))));
        }
        report.append("\n");
        
        // Average stats
        report.append("📊 AVERAGES:\n");
        report.append(String.format("   Average Events per Week: %.2f\n", getAverageEventsPerWeek(events)));
        report.append(String.format("   Average Event Duration: %.1f minutes\n\n", getAverageEventDuration(events)));
        
        // Monthly distribution
        report.append("📆 EVENTS BY MONTH:\n");
        Map<String, Integer> monthDist = getEventsByMonth(events);
        for (Map.Entry<String, Integer> entry : monthDist.entrySet()) {
            report.append(String.format("   %-10s: %3d events\n", entry.getKey(), entry.getValue()));
        }
        report.append("\n");
        
        // Longest event
        MainEvent longest = getLongestEvent(events);
        if (longest != null) {
            long duration = java.time.temporal.ChronoUnit.MINUTES.between(
                longest.getStartDateTime(), longest.getEndDateTime());
            report.append("⏱️ LONGEST EVENT:\n");
            report.append(String.format("   %s (%d minutes)\n", longest.getTitle(), duration));
        }
        
        return report.toString();
    }
}
