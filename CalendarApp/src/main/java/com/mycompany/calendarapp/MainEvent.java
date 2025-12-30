package com.mycompany.calendarapp;

import java.time.LocalDateTime;

public class MainEvent {
    private int eventId;
    private String title;
    private String description;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Reminder reminder;  // Default: null (no reminder set)
    
    // Additional fields
    private String location;
    private String category;
    private String priority;  // HIGH, MEDIUM, LOW

    public MainEvent(int eventId, String title, String description, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.eventId = eventId;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.reminder = null;
        this.location = "";
        this.category = "General";
        this.priority = "MEDIUM";
    }

    public int getEventId() { return eventId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartDateTime() { return startDateTime; }
    public LocalDateTime getEndDateTime() { return endDateTime; }
    public Reminder getReminder() { return reminder; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }

    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setStartDateTime(LocalDateTime startDateTime) { this.startDateTime = startDateTime; }
    public void setEndDateTime(LocalDateTime endDateTime) { this.endDateTime = endDateTime; }
    public void setReminder(Reminder reminder) { this.reminder = reminder; }
    public void setLocation(String location) { this.location = location; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }

    @Override
    public String toString() {
        return "MainEvent{" +
                "eventId=" + eventId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDateTime=" + startDateTime +
                ", endDateTime=" + endDateTime +
                '}';
    }
}
