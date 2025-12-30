package com.mycompany.calendarapp;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import java.time.LocalTime;

/**
 * Custom TimePicker control for selecting hours and minutes.
 * Displays two spinners for hour (0-23) and minute (0-59).
 */
public class TimePicker extends HBox {
    private Spinner<Integer> hourSpinner;
    private Spinner<Integer> minuteSpinner;

    public TimePicker() {
        this(LocalTime.now());
    }

    public TimePicker(LocalTime initialTime) {
        super(5);
        setPadding(new Insets(5));
        setStyle("-fx-border-color: #ddd; -fx-border-radius: 4; -fx-background-color: #f9f9f9;");

        int hour = initialTime != null ? initialTime.getHour() : 0;
        int minute = initialTime != null ? initialTime.getMinute() : 0;

        // Hour spinner (0-23)
        hourSpinner = new Spinner<>(0, 23, hour);
        hourSpinner.setEditable(true);
        hourSpinner.setPrefWidth(60);

        // Minute spinner (0-59)
        minuteSpinner = new Spinner<>(0, 59, minute);
        minuteSpinner.setEditable(true);
        minuteSpinner.setPrefWidth(60);

        // Labels
        Label hourLabel = new Label("Hour:");
        Label minuteLabel = new Label("Minute:");

        getChildren().addAll(hourLabel, hourSpinner, minuteLabel, minuteSpinner);
    }

    /**
     * Get the selected time as LocalTime.
     * @return LocalTime with selected hour and minute
     */
    public LocalTime getValue() {
        int hour = hourSpinner.getValue();
        int minute = minuteSpinner.getValue();
        return LocalTime.of(hour, minute);
    }

    /**
     * Set the time displayed in the picker.
     * @param time LocalTime to display
     */
    public void setValue(LocalTime time) {
        if (time != null) {
            hourSpinner.getValueFactory().setValue(time.getHour());
            minuteSpinner.getValueFactory().setValue(time.getMinute());
        }
    }
}

