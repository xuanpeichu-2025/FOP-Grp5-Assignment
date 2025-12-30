package com.mycompany.calendarapp;

import java.time.LocalDateTime;

/**
 * Basic Event class for CSV compatibility
 * This matches the event.csv format: eventId, title, description, startDateTime, endDateTime
 */
public class Event {
    private int eventId;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;

    public Event(int eventId, String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }

    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }

    /**
     * Convert to CSV line for event.csv
     * Format: eventId, title, description, startDateTime, endDateTime
     */
    public String toCSVLine() {
        return eventId + "," + title + "," + description + "," + 
               startDateTime.toString() + "," + endDateTime.toString();
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
