package com.mycompany.calendarapp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MainMenu {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        EventManager manager = new EventManager();
        CSVHandler.loadEvents(manager);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        while (true) {
            System.out.println("\n===== EVENT MANAGEMENT SYSTEM =====");
            System.out.println("1. Add Event");
            System.out.println("2. Add Recurring Event");
            System.out.println("3. View All Events");
            System.out.println("4. Update Event");
            System.out.println("5. Delete Event");
            System.out.println("6. Save & Exit");
            System.out.print("Enter choice: ");

            int choice = input.nextInt();
            input.nextLine();

            switch (choice) {

                case 1: 
                    System.out.print("Title: ");
                    String title = input.nextLine();
                    System.out.print("Description: ");
                    String desc = input.nextLine();
                    System.out.print("Start (yyyy-MM-dd HH:mm): ");
                    LocalDateTime start = LocalDateTime.parse(input.nextLine(), formatter);
                    System.out.print("End (yyyy-MM-dd HH:mm): ");
                    LocalDateTime end = LocalDateTime.parse(input.nextLine(), formatter);
                    MainEvent e = new MainEvent(manager.generateEventId(), title, desc, start, end);
                    manager.addEvent(e);
                    System.out.println("Event added!");
                    break;

                case 2: 
                    System.out.print("Title: ");
                    String rTitle = input.nextLine();
                    System.out.print("Description: ");
                    String rDesc = input.nextLine();
                    System.out.print("Start (yyyy-MM-dd HH:mm): ");
                    LocalDateTime rStart = LocalDateTime.parse(input.nextLine(), formatter);
                    System.out.print("End (yyyy-MM-dd HH:mm): ");
                    LocalDateTime rEnd = LocalDateTime.parse(input.nextLine(), formatter);
                    System.out.print("Recurrence Type (DAILY, WEEKLY, MONTHLY): ");
                    String rType = input.nextLine();
                    System.out.print("Number of occurrences: ");
                    int rOcc = input.nextInt();
                    input.nextLine();
                    RecurringEvent rEvent = new RecurringEvent(manager.generateEventId(), rTitle, rDesc, rStart, rEnd, rType, rOcc);
                    manager.addEvent(rEvent);
                    System.out.println("Recurring event added!");
                    break;

                case 3: 
                    System.out.println("\n===== ALL EVENTS =====");
                    System.out.printf("%-4s | %-9s | %-15s | %-16s | %-16s | %-12s\n", 
                                      "ID", "Type", "Title", "Start", "End", "Recurrence");
                    System.out.println("--------------------------------------------------------------------------------");
                    for (MainEvent me : manager.getAllEvents()) {
                        String type = me instanceof RecurringEvent ? "RECURRING" : "NORMAL";
                        String recurrence = "-";
                        if (me instanceof RecurringEvent r) {
                            recurrence = r.getRecurrenceType() + " x" + r.getOccurrences();
                        }
                        System.out.printf("%-4d | %-9s | %-15s | %-16s | %-16s | %-12s\n",
                                me.getEventId(),
                                type,
                                me.getTitle(),
                                me.getStartDateTime().format(formatter),
                                me.getEndDateTime().format(formatter),
                                recurrence
                        );
                    }
                    break;

                case 4: 
                    System.out.print("Enter Event ID to update: ");
                    int updId = input.nextInt();
                    input.nextLine();
                    MainEvent upEvent = manager.findEventById(updId);
                    if (upEvent == null) {
                        System.out.println("Event not found!");
                        break;
                    }
                    System.out.print("New title: ");
                    upEvent.setTitle(input.nextLine());
                    System.out.print("New description: ");
                    upEvent.setDescription(input.nextLine());
                    System.out.print("New start (yyyy-MM-dd HH:mm): ");
                    upEvent.setStartDateTime(LocalDateTime.parse(input.nextLine(), formatter));
                    System.out.print("New end (yyyy-MM-dd HH:mm): ");
                    upEvent.setEndDateTime(LocalDateTime.parse(input.nextLine(), formatter));
                    if (upEvent instanceof RecurringEvent r) {
                        System.out.print("New recurrence type: ");
                        r.setRecurrenceType(input.nextLine());
                        System.out.print("New number of occurrences: ");
                        r.setOccurrences(input.nextInt());
                        input.nextLine();
                    }
                    System.out.println("Event updated!");
                    break;

                case 5: 
                    System.out.print("Enter Event ID to delete: ");
                    int delId = input.nextInt();
                    input.nextLine();
                    if (manager.deleteEvent(delId)) System.out.println("Event deleted!");
                    else System.out.println("Event not found!");
                    break;

                case 6: 
                    CSVHandler.saveEvents(manager);
                    System.out.println("Saved! Goodbye!");
                    System.exit(0);

                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}



