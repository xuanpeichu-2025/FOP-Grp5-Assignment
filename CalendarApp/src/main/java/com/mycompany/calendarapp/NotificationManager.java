package com.mycompany.calendarapp;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.util.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class NotificationManager {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("MMM dd, HH:mm");

    /**
     * Get all upcoming events that should show reminders
     */
    public static List<MainEvent> getUpcomingReminders(EventManager manager) {
        LocalDateTime now = LocalDateTime.now();
        List<MainEvent> upcomingEvents = new ArrayList<>();

        // Only show notifications for events that have explicit reminders
        for (MainEvent event : manager.getAllEventsExpanded()) {
            if (event.getReminder() == null) continue; // No reminder set
            if (!event.getStartDateTime().isAfter(now)) continue; // Already started

            long minutesUntilEvent = ChronoUnit.MINUTES.between(now, event.getStartDateTime());
            // Trigger only within the configured reminder window
            if (minutesUntilEvent >= 0 && minutesUntilEvent <= event.getReminder().getMinutesBefore()) {
                upcomingEvents.add(event);
            }
        }

        return upcomingEvents;
    }

    /**
     * Create a notification banner for upcoming events
     */
    public static VBox createNotificationBanner(EventManager manager) {
        VBox notificationContainer = new VBox(8);
        notificationContainer.setPadding(new Insets(0, 0, 15, 0));
        
        List<MainEvent> upcomingEvents = getUpcomingReminders(manager);
        
        if (upcomingEvents.isEmpty()) {
            return notificationContainer; // Return empty container
        }

        for (MainEvent event : upcomingEvents) {
            HBox notification = createNotificationBox(event, notificationContainer);
            notificationContainer.getChildren().add(notification);
        }

        return notificationContainer;
    }

    /**
     * Create individual notification box
     */
    private static HBox createNotificationBox(MainEvent event, VBox parent) {
        HBox notificationBox = new HBox(12);
        notificationBox.setPadding(new Insets(12, 16, 12, 16));
        notificationBox.setAlignment(Pos.CENTER_LEFT);
        
        // Determine notification color based on urgency
        LocalDateTime now = LocalDateTime.now();
        long minutesUntil = ChronoUnit.MINUTES.between(now, event.getStartDateTime());
        String bgColor;
        String iconEmoji;
        
        if (minutesUntil <= 15) {
            bgColor = "linear-gradient(to right, #d32f2f, #c62828)"; // Red - urgent
            iconEmoji = "🚨";
        } else if (minutesUntil <= 60) {
            bgColor = "linear-gradient(to right, #f57c00, #ef6c00)"; // Orange - soon
            iconEmoji = "⚠️";
        } else {
            bgColor = "linear-gradient(to right, #1976d2, #1565c0)"; // Blue - upcoming
            iconEmoji = "🔔";
        }
        
        notificationBox.setStyle(
            "-fx-background-color: " + bgColor + ";" +
            "-fx-background-radius: 10;" +
            "-fx-border-radius: 10;" +
            "-fx-border-color: rgba(255,255,255,0.2);" +
            "-fx-border-width: 1;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 8, 0, 0, 2);"
        );

        // Icon
        Label icon = new Label(iconEmoji);
        icon.setStyle("-fx-font-size: 20; -fx-text-fill: white;");

        // Event info
        VBox eventInfo = new VBox(4);
        
        Label titleLabel = new Label(event.getTitle());
        titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold; -fx-text-fill: white;");
        
        String timeText = formatDuration(minutesUntil);
        String dateTimeText = event.getStartDateTime().format(DATE_TIME_FORMATTER);
        Label timeLabel = new Label("In " + timeText + " • " + dateTimeText);
        timeLabel.setStyle("-fx-font-size: 12; -fx-text-fill: rgba(255,255,255,0.9);");
        
        eventInfo.getChildren().addAll(titleLabel, timeLabel);
        HBox.setHgrow(eventInfo, Priority.ALWAYS);

        // Close button
        Button closeBtn = new Button("✕");
        closeBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 4 8 4 8;"
        );
        closeBtn.setOnMouseEntered(e -> closeBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.3);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 4 8 4 8;"
        ));
        closeBtn.setOnMouseExited(e -> closeBtn.setStyle(
            "-fx-background-color: rgba(255,255,255,0.2);" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 14;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 6;" +
            "-fx-border-radius: 6;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 4 8 4 8;"
        ));
        
        closeBtn.setOnAction(e -> {
            FadeTransition fade = new FadeTransition(Duration.millis(300), notificationBox);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);
            fade.setOnFinished(ev -> parent.getChildren().remove(notificationBox));
            fade.play();
        });

        notificationBox.getChildren().addAll(icon, eventInfo, closeBtn);
        
        // Fade in animation
        FadeTransition fadeIn = new FadeTransition(Duration.millis(400), notificationBox);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        fadeIn.play();
        
        return notificationBox;
    }

    public static String formatDuration(long minutes) {
        if (minutes < 1) {
            return "a few seconds";
        } else if (minutes == 1) {
            return "1 minute";
        } else if (minutes < 60) {
            return minutes + " minutes";
        } else if (minutes < 120) {
            return "1 hour";
        } else if (minutes < 1440) {
            long hours = minutes / 60;
            return hours + " hours";
        } else if (minutes < 2880) {
            return "1 day";
        } else {
            long days = minutes / 1440;
            return days + " days";
        }
    }
}
