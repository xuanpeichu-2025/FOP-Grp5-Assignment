package com.mycompany.calendarapp;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * CSV Handler that complies with the assignment specification:
 * - event.csv: eventId, title, description, startDateTime, endDateTime
 * - recurrent.csv: eventId, recurrentInterval, recurrentTimes, recurrentEndDate
 */
public class CSVHandlerCompliant {

    private static final String EVENT_FILE = "event.csv";
    private static final String RECURRENT_FILE = "recurrent.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Save all events to event.csv and recurrent.csv
     */
    public static void saveEvents(EventManager manager) {
        saveEventCSV(manager);
        saveRecurrentCSV(manager);
        AdditionalFieldsHandler.saveAdditionalFields(manager);  // Save additional fields
    }

    /**
     * Save basic events to event.csv
     * Format: eventId, title, description, startDateTime, endDateTime
     */
    private static void saveEventCSV(EventManager manager) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(EVENT_FILE))) {
            pw.println("eventId,title,description,startDateTime,endDateTime");
            for (MainEvent e : manager.getAllEvents()) {
                // Save all events (including recurring) with their first occurrence
                pw.println(e.getEventId() + "," + 
                          escapeCsvValue(e.getTitle()) + "," + 
                          escapeCsvValue(e.getDescription()) + "," +
                          e.getStartDateTime().format(formatter) + "," + 
                          e.getEndDateTime().format(formatter));
            }
        } catch (IOException ex) {
            System.out.println("Error saving event.csv: " + ex.getMessage());
        }
    }

    /**
     * Save recurring events to recurrent.csv
     * Format: eventId, recurrentInterval, recurrentTimes, recurrentEndDate
     */
    private static void saveRecurrentCSV(EventManager manager) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(RECURRENT_FILE))) {
            pw.println("eventId,recurrentInterval,recurrentTimes,recurrentEndDate");
            for (MainEvent e : manager.getAllEvents()) {
                if (e instanceof RecurringEvent) {
                    RecurringEvent re = (RecurringEvent) e;
                    String interval = convertRecurrenceTypeToInterval(re.getRecurrenceType());
                    int times = re.getOccurrences();
                    String endDate = "0"; // Using times instead of end date
                    
                    pw.println(e.getEventId() + "," + interval + "," + times + "," + endDate);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error saving recurrent.csv: " + ex.getMessage());
        }
    }

    /**
     * Load all events from event.csv and recurrent.csv
     */
    public static void loadEvents(EventManager manager) {
        // First load basic events from event.csv
        Map<Integer, MainEvent> basicEvents = loadEventCSV();
        
        // Then load recurrent data and link to events
        Map<Integer, RecurrentEventData> recurrentData = loadRecurrentCSV();
        
        // Merge events with recurrent data
        int maxId = 0;
        for (Map.Entry<Integer, MainEvent> entry : basicEvents.entrySet()) {
            int eventId = entry.getKey();
            MainEvent event = entry.getValue();
            
            if (recurrentData.containsKey(eventId)) {
                // This is a recurring event
                RecurrentEventData rd = recurrentData.get(eventId);
                String recurrenceType = convertIntervalToRecurrenceType(rd.getRecurrentInterval());
                int occurrences = rd.getRecurrentTimes();
                
                // Create RecurringEvent from basic event data
                RecurringEvent recurringEvent = new RecurringEvent(
                    eventId,
                    event.getTitle(),
                    event.getDescription(),
                    event.getStartDateTime(),
                    event.getEndDateTime(),
                    recurrenceType,
                    occurrences
                );
                
                // Copy reminder if exists
                if (event.getReminder() != null) {
                    recurringEvent.setReminder(event.getReminder());
                }
                
                manager.addEvent(recurringEvent);
            } else {
                // Normal event
                manager.addEvent(event);
            }
            
            if (eventId > maxId) maxId = eventId;
        }
        
        manager.setNextEventId(maxId + 1);
        
        // Load additional fields after all events are loaded
        AdditionalFieldsHandler.loadAdditionalFields(manager);
    }

    /**
     * Load events from event.csv
     */
    private static Map<Integer, MainEvent> loadEventCSV() {
        Map<Integer, MainEvent> events = new HashMap<>();
        File file = new File(EVENT_FILE);
        if (!file.exists()) return events;

        try (BufferedReader br = new BufferedReader(new FileReader(EVENT_FILE))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 5) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String desc = parts[2];
                    LocalDateTime start = LocalDateTime.parse(parts[3], formatter);
                    LocalDateTime end = LocalDateTime.parse(parts[4], formatter);
                    
                    MainEvent event = new MainEvent(id, title, desc, start, end);
                    events.put(id, event);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading event.csv: " + ex.getMessage());
        }
        
        return events;
    }

    /**
     * Load recurring event data from recurrent.csv
     */
    private static Map<Integer, RecurrentEventData> loadRecurrentCSV() {
        Map<Integer, RecurrentEventData> recurrentData = new HashMap<>();
        File file = new File(RECURRENT_FILE);
        if (!file.exists()) return recurrentData;

        try (BufferedReader br = new BufferedReader(new FileReader(RECURRENT_FILE))) {
            String line = br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    int eventId = Integer.parseInt(parts[0]);
                    String interval = parts[1];
                    int times = Integer.parseInt(parts[2]);
                    String endDate = parts[3];
                    
                    RecurrentEventData rd = new RecurrentEventData(eventId, interval, times, endDate);
                    recurrentData.put(eventId, rd);
                }
            }
        } catch (IOException ex) {
            System.out.println("Error loading recurrent.csv: " + ex.getMessage());
        }
        
        return recurrentData;
    }

    /**
     * Convert RecurrenceType (DAILY, WEEKLY, MONTHLY) to interval format (1d, 1w, 1m)
     */
    private static String convertRecurrenceTypeToInterval(String recurrenceType) {
        switch (recurrenceType.toUpperCase()) {
            case "DAILY": return "1d";
            case "WEEKLY": return "1w";
            case "MONTHLY": return "1m";
            default: return "1d";
        }
    }

    /**
     * Convert interval format (1d, 1w, 1m) to RecurrenceType (DAILY, WEEKLY, MONTHLY)
     */
    private static String convertIntervalToRecurrenceType(String interval) {
        if (interval.endsWith("d")) return "DAILY";
        if (interval.endsWith("w")) return "WEEKLY";
        if (interval.endsWith("m")) return "MONTHLY";
        return "DAILY";
    }

    /**
     * Escape CSV values that contain commas or quotes
     */
    private static String escapeCsvValue(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    /**
     * Parse CSV line handling quoted values
     */
    private static String[] parseCsvLine(String line) {
        java.util.List<String> values = new java.util.ArrayList<>();
        boolean inQuotes = false;
        StringBuilder current = new StringBuilder();
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                values.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        values.add(current.toString());
        
        return values.toArray(new String[0]);
    }
}
