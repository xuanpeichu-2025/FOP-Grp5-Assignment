package com.mycompany.calendarapp;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class RecurringEvent extends MainEvent {

    private String recurrenceType;
    private int occurrences;       

    public RecurringEvent(int eventId, String title, String description,
                          LocalDateTime startDateTime, LocalDateTime endDateTime,
                          String recurrenceType, int occurrences) {
        super(eventId, title, description, startDateTime, endDateTime);
        this.recurrenceType = recurrenceType.toUpperCase();
        this.occurrences = occurrences;
    }

    public String getRecurrenceType() { return recurrenceType; }
    public int getOccurrences() { return occurrences; }

    public void setRecurrenceType(String recurrenceType) { this.recurrenceType = recurrenceType.toUpperCase(); }
    public void setOccurrences(int occurrences) { this.occurrences = occurrences; }

    public LocalDateTime getNextOccurrence(LocalDateTime current) {
        switch (recurrenceType) {
            case "DAILY": return current.plus(1, ChronoUnit.DAYS);
            case "WEEKLY": return current.plus(1, ChronoUnit.WEEKS);
            case "MONTHLY": return current.plus(1, ChronoUnit.MONTHS);
            default: return current;
        }
    }
    
    /**
     * Generate all occurrences of this recurring event
     * @return List of MainEvent objects representing each occurrence
     */
    public List<MainEvent> generateOccurrences() {
        List<MainEvent> occurrencesList = new ArrayList<>();
        LocalDateTime currentStart = this.getStartDateTime();
        LocalDateTime currentEnd = this.getEndDateTime();
        long duration = ChronoUnit.MINUTES.between(currentStart, currentEnd);
        
        for (int i = 0; i < occurrences; i++) {
            MainEvent occurrence = new MainEvent(
                this.getEventId(), 
                this.getTitle() + " (Occurrence " + (i + 1) + ")", 
                this.getDescription(), 
                currentStart, 
                currentEnd
            );
            occurrencesList.add(occurrence);
            
            // Move to next occurrence
            currentStart = getNextOccurrence(currentStart);
            currentEnd = currentStart.plus(duration, ChronoUnit.MINUTES);
        }
        
        return occurrencesList;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", RecurringEvent{" +
                "recurrenceType='" + recurrenceType + '\'' +
                ", occurrences=" + occurrences +
                '}';
    }
}

