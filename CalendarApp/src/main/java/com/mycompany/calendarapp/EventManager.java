package com.mycompany.calendarapp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventManager {

    private List<MainEvent> events = new ArrayList<>();
    private int nextEventId = 1;

    public int generateEventId() { return nextEventId++; }

    public void addEvent(MainEvent event) { events.add(event); }
    public List<MainEvent> getAllEvents() { return events; }

    public MainEvent findEventById(int id) {
        for (MainEvent e : events) {
            if (e.getEventId() == id) return e;
        }
        return null;
    }

    public boolean deleteEvent(int id) {
        MainEvent e = findEventById(id);
        if (e != null) {
            events.remove(e);
            return true;
        }
        return false;
    }

    public void setNextEventId(int id) { this.nextEventId = id; }
    
    /**
     * Get all events including expanded occurrences of recurring events
     * @return List of all events with recurring events expanded into individual occurrences
     */
    public List<MainEvent> getAllEventsExpanded() {
        List<MainEvent> expandedEvents = new ArrayList<>();
        
        for (MainEvent event : events) {
            if (event instanceof RecurringEvent) {
                // Add all occurrences of the recurring event
                RecurringEvent recurring = (RecurringEvent) event;
                expandedEvents.addAll(recurring.generateOccurrences());
            } else {
                // Add normal events as-is
                expandedEvents.add(event);
            }
        }
        
        return expandedEvents;
    }
    
    /**
     * Check if a new event clashes with any existing events
     * @param newEvent The event to check for clashes
     * @return List of events that clash with the new event
     */
    public List<MainEvent> findClashingEvents(MainEvent newEvent) {
        List<MainEvent> clashes = new ArrayList<>();
        LocalDateTime newStart = newEvent.getStartDateTime();
        LocalDateTime newEnd = newEvent.getEndDateTime();
        
        for (MainEvent existing : events) {
            // Skip comparing with itself (when updating)
            if (existing.getEventId() == newEvent.getEventId()) {
                continue;
            }
            
            if (existing instanceof RecurringEvent) {
                // For recurring events, check all occurrences
                RecurringEvent recurring = (RecurringEvent) existing;
                List<MainEvent> occurrences = recurring.generateOccurrences();
                for (MainEvent occurrence : occurrences) {
                    if (eventsOverlap(newStart, newEnd, occurrence.getStartDateTime(), occurrence.getEndDateTime())) {
                        clashes.add(existing);
                        break; // Only add once per recurring event
                    }
                }
            } else {
                // For normal events, direct comparison
                if (eventsOverlap(newStart, newEnd, existing.getStartDateTime(), existing.getEndDateTime())) {
                    clashes.add(existing);
                }
            }
        }
        
        return clashes;
    }
    
    /**
     * Check if two time ranges overlap
     */
    private boolean eventsOverlap(LocalDateTime start1, LocalDateTime end1, 
                                   LocalDateTime start2, LocalDateTime end2) {
        // Events overlap if one starts before the other ends
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
    
    /**
     * Find all events that have clashes with any other events
     * @return List of events that have conflicts
     */
    public List<MainEvent> findAllClashingEvents() {
        List<MainEvent> clashing = new ArrayList<>();
        
        for (int i = 0; i < events.size(); i++) {
            MainEvent event = events.get(i);
            List<MainEvent> clashes = findClashingEvents(event);
            if (!clashes.isEmpty() && !clashing.contains(event)) {
                clashing.add(event);
            }
        }
        
        return clashing;
    }
}
