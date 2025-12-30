package com.mycompany.calendarapp;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Handles saving and loading additional event fields to/from additional.csv
 * Format: eventId,location,category,priority
 */
public class AdditionalFieldsHandler {
    
    private static final String ADDITIONAL_CSV = "additional.csv";
    
    /**
     * Save additional fields for all events to additional.csv
     */
    public static void saveAdditionalFields(EventManager manager) {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(ADDITIONAL_CSV), StandardCharsets.UTF_8))) {
            
            // Write header
            writer.write("eventId,location,category,priority");
            writer.newLine();
            
            // Write each event's additional fields
            for (MainEvent event : manager.getAllEvents()) {
                String location = event.getLocation() != null ? event.getLocation() : "";
                String category = event.getCategory() != null ? event.getCategory() : "General";
                String priority = event.getPriority() != null ? event.getPriority() : "MEDIUM";
                
                writer.write(String.format("%d,%s,%s,%s",
                    event.getEventId(),
                    escapeCsv(location),
                    escapeCsv(category),
                    escapeCsv(priority)));
                writer.newLine();
            }
            
        } catch (IOException e) {
            System.err.println("Error saving additional fields: " + e.getMessage());
        }
    }
    
    /**
     * Load additional fields from additional.csv and apply to events
     */
    public static void loadAdditionalFields(EventManager manager) {
        File file = new File(ADDITIONAL_CSV);
        if (!file.exists()) {
            return; // File doesn't exist yet, skip loading
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            
            String line = reader.readLine(); // Skip header
            
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length >= 4) {
                    int eventId = Integer.parseInt(parts[0]);
                    String location = parts[1];
                    String category = parts[2];
                    String priority = parts[3];
                    
                    // Find the event and update its additional fields
                    for (MainEvent event : manager.getAllEvents()) {
                        if (event.getEventId() == eventId) {
                            event.setLocation(location);
                            event.setCategory(category);
                            event.setPriority(priority);
                            break;
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("Error loading additional fields: " + e.getMessage());
        }
    }
    
    /**
     * Escape CSV special characters
     */
    private static String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
    
    /**
     * Parse a CSV line handling quoted values
     */
    private static String[] parseCsvLine(String line) {
        java.util.List<String> result = new java.util.ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    current.append('"');
                    i++; // Skip next quote
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result.toArray(new String[0]);
    }
}
