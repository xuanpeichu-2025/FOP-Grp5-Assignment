package com.mycompany.calendarapp;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CSVHandler {

    private static final String FILE_NAME = "events.csv";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void saveEvents(EventManager manager) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (MainEvent e : manager.getAllEvents()) {
                String type = e instanceof RecurringEvent ? "RECURRING" : "NORMAL";
                String extra = "";
                if (e instanceof RecurringEvent r) {
                    extra = "," + r.getRecurrenceType() + "," + r.getOccurrences();
                }
                String reminder = e.getReminder() != null ? "," + e.getReminder().getMinutesBefore() : ",";
                pw.println(e.getEventId() + "," + type + "," + e.getTitle() + "," + e.getDescription() + "," +
                        e.getStartDateTime().format(formatter) + "," + e.getEndDateTime().format(formatter) + extra + reminder);
            }
        } catch (IOException ex) {
            System.out.println("Error saving CSV: " + ex.getMessage());
        }
    }

    public static void loadEvents(EventManager manager) {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            int maxId = 0;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0]);
                String type = parts[1];
                String title = parts[2];
                String desc = parts[3];
                LocalDateTime start = LocalDateTime.parse(parts[4], formatter);
                LocalDateTime end = LocalDateTime.parse(parts[5], formatter);

                MainEvent event;
                if ("RECURRING".equals(type)) {
                    String recurrenceType = parts[6];
                    int occurrences = Integer.parseInt(parts[7]);
                    event = new RecurringEvent(id, title, desc, start, end, recurrenceType, occurrences);
                    // Reminder is at index 8 for recurring events
                    if (parts.length > 8 && !parts[8].isEmpty()) {
                        int reminderMinutes = Integer.parseInt(parts[8]);
                        event.setReminder(new Reminder(reminderMinutes));
                    }
                } else {
                    event = new MainEvent(id, title, desc, start, end);
                    // Reminder is at index 6 for normal events
                    if (parts.length > 6 && !parts[6].isEmpty()) {
                        int reminderMinutes = Integer.parseInt(parts[6]);
                        event.setReminder(new Reminder(reminderMinutes));
                    }
                }

                manager.addEvent(event);
                if (id > maxId) maxId = id;
            }
            manager.setNextEventId(maxId + 1);
        } catch (IOException ex) {
            System.out.println("Error loading CSV: " + ex.getMessage());
        }
    }
}
