package com.mycompany.calendarapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CalendarAppGUI extends Application {
    private EventManager manager;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private CalendarView calendarView;
    private SearchEvent searchEvent;

    private static final String APP_GRADIENT = "-fx-background-color: linear-gradient(to bottom, #0f1f33, #0b1423); -fx-text-fill: #e7f7ff;";
    private static final String PANEL_STYLE = "-fx-background-color: #142238; -fx-background-radius: 14; -fx-border-radius: 14; -fx-border-color: #1f3550; -fx-border-width: 1; -fx-text-fill: #e7f7ff;";
    private static final String CARD_STYLE = "-fx-background-color: #1b2d45; -fx-background-radius: 12; -fx-border-radius: 12; -fx-border-color: #234567; -fx-border-width: 1; -fx-text-fill: #e7f7ff;";
    private static final String ACCENT = "#21d4e4";
    private static final String TEXT_PRIMARY = "#e7f7ff";
    private static final String TEXT_MUTED = "#9fb8d3";

    @Override
    public void start(Stage primaryStage) {
        manager = new EventManager();
        CSVHandlerCompliant.loadEvents(manager);
        calendarView = new CalendarView();
        searchEvent = new SearchEvent();

        primaryStage.setTitle("Calendar Application");
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);

        // Create main menu
        StackPane mainMenuPane = createMainMenu(primaryStage);
        Scene scene = new Scene(mainMenuPane, 1200, 800);
        primaryStage.setScene(scene);
        
        // Center the window on screen
        primaryStage.centerOnScreen();
        
        primaryStage.show();
    }

    private StackPane createMainMenu(Stage stage) {
        VBox vbox = new VBox(25);
        vbox.setPadding(new Insets(30, 30, 30, 30));
        vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        vbox.setStyle("-fx-background-color: transparent;");

        // Add notification banner at the top
        VBox notificationBanner = NotificationManager.createNotificationBanner(manager);

        Label title = new Label("📅 CALENDAR APPLICATION");
        title.setStyle("-fx-font-size: 36; -fx-font-weight: bold; -fx-text-fill: " + ACCENT + "; -fx-effect: dropshadow(gaussian, rgba(33,212,228,0.35), 14, 0, 0, 4);");

        Label subtitle = new Label("Manage Your Events with Style");
        subtitle.setStyle("-fx-font-size: 16; -fx-text-fill: " + TEXT_MUTED + "; -fx-padding: 0 0 20 0;");

        Button btnEventManagement = createStyledButton("📝 Event Management", "#4CAF50");
        btnEventManagement.setOnAction(e -> showEventManagementMenu(stage));

        Button btnCalendarView = createStyledButton("📆 Calendar View", "#2196F3");
        btnCalendarView.setOnAction(e -> showCalendarViewMenu(stage));

        Button btnSearchEvent = createStyledButton("🔍 Search Event", "#FF9800");
        btnSearchEvent.setOnAction(e -> showSearchEventMenu(stage));

        Button btnManageReminders = createStyledButton("⏰ Manage Reminders", "#9C27B0");
        btnManageReminders.setOnAction(e -> showManageRemindersMenu(stage));

        Button btnBackupRestore = createStyledButton("💾 Backup & Restore", "#607D8B");
        btnBackupRestore.setOnAction(e -> showBackupRestoreMenu(stage));

        Button btnStatistics = createStyledButton("📊 Event Statistics", "#00BCD4");
        btnStatistics.setOnAction(e -> showEventStatisticsDialog());

        Button btnExit = createStyledButton("🚪 Save & Exit", "#f44336");
        btnExit.setOnAction(e -> {
            CSVHandlerCompliant.saveEvents(manager);
            stage.close();
        });

        VBox buttonBox = new VBox(18);
        buttonBox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        buttonBox.setPadding(new Insets(20, 0, 0, 0));
        buttonBox.getChildren().addAll(btnEventManagement, btnCalendarView, btnSearchEvent, 
                                       btnManageReminders, btnBackupRestore, btnStatistics, btnExit);

        vbox.getChildren().addAll(notificationBanner, title, subtitle, buttonBox);

        StackPane root = new StackPane(vbox);
        root.setStyle(APP_GRADIENT);
        StackPane.setAlignment(vbox, javafx.geometry.Pos.TOP_LEFT);
        return root;
    }

    private Button createStyledButton(String text, String color) {
        Button button = new Button(text);
        button.setPrefWidth(320);
        button.setPrefHeight(55);
        button.setStyle(
            "-fx-background-color: linear-gradient(to right, " + color + ", #1bb3c9);" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255,255,255,0.08);" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(33,212,228,0.35), 12, 0, 0, 3);"
        );
        
        button.setOnMouseEntered(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to right, derive(" + color + ", 10%), #28e1f1);" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255,255,255,0.15);" +
            "-fx-cursor: hand;" +
            "-fx-scale-x: 1.04;" +
            "-fx-scale-y: 1.04;" +
            "-fx-effect: dropshadow(gaussian, rgba(33,212,228,0.55), 16, 0, 0, 4);"
        ));
        
        button.setOnMouseExited(e -> button.setStyle(
            "-fx-background-color: linear-gradient(to right, " + color + ", #1bb3c9);" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-font-size: 16px;" +
            "-fx-font-weight: bold;" +
            "-fx-background-radius: 12;" +
            "-fx-border-radius: 12;" +
            "-fx-border-color: rgba(255,255,255,0.08);" +
            "-fx-cursor: hand;" +
            "-fx-effect: dropshadow(gaussian, rgba(33,212,228,0.35), 12, 0, 0, 3);"
        ));
        
        return button;
    }

    private void showEventManagementMenu(Stage stage) {
        VBox vbox = new VBox(18);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        vbox.setStyle("-fx-background-color: transparent;");

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");

        // Add notification banner at the top
        VBox notificationBanner = NotificationManager.createNotificationBanner(manager);

        Label title = new Label("📝 EVENT MANAGEMENT");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        Button btnAddEvent = createStyledButton("➕ Add Event", "#4CAF50");
        btnAddEvent.setOnAction(e -> showAddEventDialog());

        Button btnAddRecurring = createStyledButton("🔄 Add Recurring Event", "#8BC34A");
        btnAddRecurring.setOnAction(e -> showAddRecurringEventDialog());

        Button btnViewAllEvents = createStyledButton("📋 View All Events", "#009688");
        btnViewAllEvents.setOnAction(e -> showAllEventsDialog());

        Button btnUpdateEvent = createStyledButton("✏️ Update Event", "#00BCD4");
        btnUpdateEvent.setOnAction(e -> showUpdateEventDialog());

        Button btnDeleteEvent = createStyledButton("🗑️ Delete Event", "#FF5722");
        btnDeleteEvent.setOnAction(e -> showDeleteEventDialog());

        Button btnViewClashes = createStyledButton("⚠️ View Clashing Events", "#FF9800");
        btnViewClashes.setOnAction(e -> showClashingEventsDialog());

        Button btnBack = createStyledButton("⬅️ Back to Main Menu", "#607D8B");
        btnBack.setOnAction(e -> {
            Stage currentStage = ((Stage) vbox.getScene().getWindow());
            currentStage.setScene(new Scene(createMainMenu(stage), 1200, 800));
            currentStage.centerOnScreen();
        });

        VBox buttonContainer = new VBox(18);
        buttonContainer.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        buttonContainer.getChildren().addAll(btnAddEvent, btnAddRecurring, 
                                  btnViewAllEvents, btnUpdateEvent, btnDeleteEvent, 
                                  btnViewClashes, btnBack);

        vbox.getChildren().addAll(notificationBanner, title, new Separator(), buttonContainer);

        StackPane root = new StackPane(scrollPane);
        root.setStyle(APP_GRADIENT);
        StackPane.setAlignment(scrollPane, javafx.geometry.Pos.TOP_LEFT);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void showAddEventDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Add Event");
        dialog.setHeaderText("Enter Event Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField();
        titleField.setPromptText("Event Title");
        TextField descField = new TextField();
        descField.setPromptText("Description");

        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        DatePicker endDatePicker = new DatePicker(LocalDate.now());
        TimePicker startTimePicker = new TimePicker(LocalTime.now().withSecond(0).withNano(0));
        TimePicker endTimePicker = new TimePicker(LocalTime.now().withSecond(0).withNano(0).plusHours(1));

        // Additional fields
        TextField locationField = new TextField();
        locationField.setPromptText("Location (optional)");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("General", "Work", "Personal", "Meeting", "Study", "Health", "Other");
        categoryBox.setValue("General");
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("HIGH", "MEDIUM", "LOW");
        priorityBox.setValue("MEDIUM");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("Start Time:"), 0, 3);
        grid.add(startTimePicker, 1, 3);
        grid.add(new Label("End Date:"), 0, 4);
        grid.add(endDatePicker, 1, 4);
        grid.add(new Label("End Time:"), 0, 5);
        grid.add(endTimePicker, 1, 5);
        grid.add(new Label("Location:"), 0, 6);
        grid.add(locationField, 1, 6);
        grid.add(new Label("Category:"), 0, 7);
        grid.add(categoryBox, 1, 7);
        grid.add(new Label("Priority:"), 0, 8);
        grid.add(priorityBox, 1, 8);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String title = titleField.getText();
                    String description = descField.getText();
                    LocalDate startDate = startDatePicker.getValue();
                    LocalDate endDate = endDatePicker.getValue();
                    LocalTime startTime = startTimePicker.getValue();
                    LocalTime endTime = endTimePicker.getValue();
                    if (startDate == null || endDate == null || startTime == null || endTime == null) {
                        throw new IllegalArgumentException("Please choose start/end date and time.");
                    }
                    LocalDateTime start = LocalDateTime.of(startDate, startTime);
                    LocalDateTime end = LocalDateTime.of(endDate, endTime);

                    MainEvent event = new MainEvent(manager.generateEventId(), title, description, start, end);
                    
                    // Set additional fields
                    event.setLocation(locationField.getText());
                    event.setCategory(categoryBox.getValue());
                    event.setPriority(priorityBox.getValue());
                    
                    // Check for clashes
                    List<MainEvent> clashes = manager.findClashingEvents(event);
                    if (!clashes.isEmpty()) {
                        StringBuilder clashMsg = new StringBuilder("⚠️ WARNING: This event clashes with:\n\n");
                        for (MainEvent clash : clashes) {
                            clashMsg.append("• ").append(clash.getTitle())
                                   .append(" (").append(clash.getStartDateTime().format(dateTimeFormatter))
                                   .append(" - ").append(clash.getEndDateTime().format(dateTimeFormatter))
                                   .append(")\n");
                        }
                        clashMsg.append("\n\nChoose an action:");

                        ButtonType replaceExisting = new ButtonType("Replace Existing", ButtonBar.ButtonData.YES);
                        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                        if (clashes.size() == 1) {
                            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, clashMsg.toString(), replaceExisting, cancelBtn);
                            confirmAlert.setTitle("Event Clash Detected");
                            confirmAlert.setHeaderText("Scheduling Conflict");

                            ButtonType result = confirmAlert.showAndWait().orElse(cancelBtn);

                            if (result == cancelBtn) {
                                return false; // user cancelled
                            } else if (result == replaceExisting) {
                                // Replace the single conflicting event
                                MainEvent toRemove = clashes.get(0);
                                manager.deleteEvent(toRemove.getEventId());
                            }
                        } else {
                            // Multiple clashes: do not allow adding, prompt user to adjust
                            showAlert(Alert.AlertType.WARNING, "Event Clash Detected", "This event clashes with multiple existing events. Please adjust the event time or delete conflicting events.");
                            return false;
                        }
                    }

                    manager.addEvent(event);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Event added successfully!");
                    return true;
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid input: " + ex.getMessage());
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    private void showAddRecurringEventDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Add Recurring Event");
        dialog.setHeaderText("Enter Recurring Event Details");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField();
        titleField.setPromptText("Event Title");
        TextField descField = new TextField();
        descField.setPromptText("Description");

        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        DatePicker endDatePicker = new DatePicker(LocalDate.now());
        TimePicker startTimePicker = new TimePicker(LocalTime.now().withSecond(0).withNano(0));
        TimePicker endTimePicker = new TimePicker(LocalTime.now().withSecond(0).withNano(0).plusHours(1));

        ComboBox<String> recurrenceBox = new ComboBox<>();
        recurrenceBox.getItems().addAll("DAILY", "WEEKLY", "MONTHLY");
        TextField occurrencesField = new TextField();
        occurrencesField.setPromptText("Number of occurrences");

        // Recurrence mode: choose either occurrences or end date
        RadioButton occurrencesRadio = new RadioButton("By occurrences");
        RadioButton endDateRadio = new RadioButton("By end date");
        ToggleGroup recurrenceModeGroup = new ToggleGroup();
        occurrencesRadio.setToggleGroup(recurrenceModeGroup);
        endDateRadio.setToggleGroup(recurrenceModeGroup);
        occurrencesRadio.setSelected(true);

        DatePicker recurrenceEndDatePicker = new DatePicker();
        recurrenceEndDatePicker.setDisable(true);

        // Interval (N) spinner: repeat every N units
        Spinner<Integer> intervalSpinner = new Spinner<>(1, 1000, 1);
        intervalSpinner.setEditable(true);

        // Toggle enabling/disabling of inputs based on mode
        recurrenceModeGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            boolean byOccurrences = (newToggle == occurrencesRadio);
            occurrencesField.setDisable(!byOccurrences);
            recurrenceEndDatePicker.setDisable(byOccurrences);
        });

        // Additional fields
        TextField locationField = new TextField();
        locationField.setPromptText("Location (optional)");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("General", "Work", "Personal", "Meeting", "Study", "Health", "Other");
        categoryBox.setValue("General");
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("HIGH", "MEDIUM", "LOW");
        priorityBox.setValue("MEDIUM");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("Start Time:"), 0, 3);
        grid.add(startTimePicker, 1, 3);
        grid.add(new Label("End Date:"), 0, 4);
        grid.add(endDatePicker, 1, 4);
        grid.add(new Label("End Time:"), 0, 5);
        grid.add(endTimePicker, 1, 5);
        grid.add(new Label("Recurrence Type:"), 0, 6);
        grid.add(recurrenceBox, 1, 6);
        grid.add(new Label("Recurrence Mode:"), 0, 7);
        HBox modeBox = new HBox(10, occurrencesRadio, endDateRadio);
        grid.add(modeBox, 1, 7);
        grid.add(new Label("Interval (N):"), 0, 8);
        grid.add(intervalSpinner, 1, 8);
        grid.add(new Label("Occurrences:"), 0, 9);
        grid.add(occurrencesField, 1, 9);
        grid.add(new Label("Recurrence End Date:"), 0, 10);
        grid.add(recurrenceEndDatePicker, 1, 10);
        grid.add(new Label("Location:"), 0, 11);
        grid.add(locationField, 1, 11);
        grid.add(new Label("Category:"), 0, 12);
        grid.add(categoryBox, 1, 12);
        grid.add(new Label("Priority:"), 0, 13);
        grid.add(priorityBox, 1, 13);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String title = titleField.getText();
                    String description = descField.getText();
                    LocalDate startDate = startDatePicker.getValue();
                    LocalDate endDate = endDatePicker.getValue();
                    LocalTime startTime = startTimePicker.getValue();
                    LocalTime endTime = endTimePicker.getValue();
                    if (startDate == null || endDate == null || startTime == null || endTime == null) {
                        throw new IllegalArgumentException("Please choose start/end date and time.");
                    }
                    LocalDateTime start = LocalDateTime.of(startDate, startTime);
                    LocalDateTime end = LocalDateTime.of(endDate, endTime);
                    String recurrence = recurrenceBox.getValue();
                    if (recurrence == null || recurrence.isEmpty()) {
                        throw new IllegalArgumentException("Please choose a recurrence type.");
                    }

                    RecurringEvent event;
                    Toggle selectedMode = recurrenceModeGroup.getSelectedToggle();
                    if (selectedMode == null) {
                        throw new IllegalArgumentException("Please choose recurrence by occurrences or by end date.");
                    }

                    int interval = intervalSpinner.getValue();
                    if (interval <= 0) throw new IllegalArgumentException("Interval must be > 0.");

                    if (selectedMode == occurrencesRadio) {
                        // Use occurrences
                        int occurrences = Integer.parseInt(occurrencesField.getText());
                        if (occurrences <= 0) throw new IllegalArgumentException("Occurrences must be > 0.");
                        event = new RecurringEvent(manager.generateEventId(), title, description, start, end, recurrence, interval, occurrences);
                    } else {
                        // Use end date
                        LocalDate recEnd = recurrenceEndDatePicker.getValue();
                        if (recEnd == null) throw new IllegalArgumentException("Please choose a recurrence end date.");
                        event = new RecurringEvent(manager.generateEventId(), title, description, start, end, recurrence, interval, recEnd);
                    }
                    
                    // Set additional fields
                    event.setLocation(locationField.getText());
                    event.setCategory(categoryBox.getValue());
                    event.setPriority(priorityBox.getValue());
                    
                    // Check for clashes with recurring event occurrences
                    List<MainEvent> clashes = manager.findClashingEvents(event);
                    if (!clashes.isEmpty()) {
                        StringBuilder clashMsg = new StringBuilder("⚠️ WARNING: This recurring event clashes with:\n\n");
                        for (MainEvent clash : clashes) {
                            clashMsg.append("• ").append(clash.getTitle())
                                   .append(" (").append(clash.getStartDateTime().format(dateTimeFormatter))
                                   .append(" - ").append(clash.getEndDateTime().format(dateTimeFormatter))
                                   .append(")\n");
                        }
                        clashMsg.append("\n\nChoose an action:");

                        ButtonType replaceExisting = new ButtonType("Replace Existing", ButtonBar.ButtonData.YES);
                        ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

                        if (clashes.size() == 1) {
                            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, clashMsg.toString(), replaceExisting, cancelBtn);
                            confirmAlert.setTitle("Event Clash Detected");
                            confirmAlert.setHeaderText("Scheduling Conflict");

                            ButtonType result = confirmAlert.showAndWait().orElse(cancelBtn);

                            if (result == cancelBtn) {
                                return false;
                            } else if (result == replaceExisting) {
                                MainEvent toRemove = clashes.get(0);
                                manager.deleteEvent(toRemove.getEventId());
                            }
                        } else {
                            showAlert(Alert.AlertType.WARNING, "Event Clash Detected", "This recurring event clashes with multiple existing events. Please adjust the schedule or delete conflicting events.");
                            return false;
                        }
                    }
                    
                    manager.addEvent(event);
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Recurring event added successfully!");
                    return true;
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid input: " + ex.getMessage());
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    private void showAllEventsDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("All Events");
        dialog.setHeaderText("Viewing All Events");

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        TableView<MainEvent> table = new TableView<>();
        TableColumn<MainEvent, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEventId()));

        TableColumn<MainEvent, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getTitle()));

        TableColumn<MainEvent, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getDescription()));

        TableColumn<MainEvent, String> startCol = new TableColumn<>("Start");
        startCol.setCellValueFactory(cellData -> new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getStartDateTime().format(dateTimeFormatter)));

        TableColumn<MainEvent, String> reminderCol = new TableColumn<>("Reminder");
        reminderCol.setCellValueFactory(cellData -> {
            Reminder r = cellData.getValue().getReminder();
            String text = r != null ? r.getDisplayText() : "-";
            return new javafx.beans.property.SimpleObjectProperty<>(text);
        });

        table.getColumns().setAll(Arrays.asList(idCol, titleCol, descCol, startCol, reminderCol));
        table.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllEventsExpanded()));
        table.setPrefHeight(400);

        content.getChildren().addAll(table);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showUpdateEventDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Update Event");
        dialog.setHeaderText("Select Event to Update");

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));

        // Create table showing all events
        TableView<MainEvent> eventTable = new TableView<>();
        eventTable.setPrefHeight(300);
        
        TableColumn<MainEvent, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getEventId()));
        idCol.setPrefWidth(50);
        
        TableColumn<MainEvent, String> titleCol = new TableColumn<>("Event Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.setPrefWidth(200);
        
        TableColumn<MainEvent, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getStartDateTime().format(dateTimeFormatter)));
        startCol.setPrefWidth(150);
        
        TableColumn<MainEvent, String> endCol = new TableColumn<>("End Time");
        endCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getEndDateTime().format(dateTimeFormatter)));
        endCol.setPrefWidth(150);
        
        eventTable.getColumns().setAll(Arrays.asList(idCol, titleCol, startCol, endCol));
        eventTable.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllEvents()));

        Label instructionLabel = new Label("📋 Select an event from the table above, then enter its ID below:");
        instructionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        idField.setPromptText("Event ID");

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(idField, 1, 0);

        mainContent.getChildren().addAll(eventTable, instructionLabel, grid);

        dialog.getDialogPane().setContent(mainContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    int eventId = Integer.parseInt(idField.getText());
                    MainEvent event = manager.findEventById(eventId);
                    if (event != null) {
                        showUpdateEventDetailsDialog(event);
                        return true;
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Event not found!");
                    }
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid event ID!");
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    private void showUpdateEventDetailsDialog(MainEvent event) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Update Event Details");
        dialog.setHeaderText("Update " + event.getTitle());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField(event.getTitle());
        TextField descField = new TextField(event.getDescription());

        DatePicker startDatePicker = new DatePicker(event.getStartDateTime().toLocalDate());
        DatePicker endDatePicker = new DatePicker(event.getEndDateTime().toLocalDate());
        TimePicker startTimePicker = new TimePicker(event.getStartDateTime().toLocalTime());
        TimePicker endTimePicker = new TimePicker(event.getEndDateTime().toLocalTime());

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Start Date:"), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("Start Time:"), 0, 3);
        grid.add(startTimePicker, 1, 3);
        grid.add(new Label("End Date:"), 0, 4);
        grid.add(endDatePicker, 1, 4);
        grid.add(new Label("End Time:"), 0, 5);
        grid.add(endTimePicker, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    event.setTitle(titleField.getText());
                    event.setDescription(descField.getText());

                    LocalDate startDate = startDatePicker.getValue();
                    LocalDate endDate = endDatePicker.getValue();
                    LocalTime startTime = startTimePicker.getValue();
                    LocalTime endTime = endTimePicker.getValue();
                    if (startDate == null || endDate == null || startTime == null || endTime == null) {
                        throw new IllegalArgumentException("Please choose start/end date and time.");
                    }

                    event.setStartDateTime(LocalDateTime.of(startDate, startTime));
                    event.setEndDateTime(LocalDateTime.of(endDate, endTime));

                    showAlert(Alert.AlertType.INFORMATION, "Success", "Event updated successfully!");
                    return true;
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid input: " + ex.getMessage());
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    private void showDeleteEventDialog() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Delete Event");
        dialog.setHeaderText("Select Event to Delete");

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));

        // Create table showing all events
        TableView<MainEvent> eventTable = new TableView<>();
        eventTable.setPrefHeight(300);
        
        TableColumn<MainEvent, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getEventId()));
        idCol.setPrefWidth(50);
        
        TableColumn<MainEvent, String> titleCol = new TableColumn<>("Event Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.setPrefWidth(200);
        
        TableColumn<MainEvent, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getStartDateTime().format(dateTimeFormatter)));
        startCol.setPrefWidth(150);
        
        TableColumn<MainEvent, String> endCol = new TableColumn<>("End Time");
        endCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getEndDateTime().format(dateTimeFormatter)));
        endCol.setPrefWidth(150);
        
        eventTable.getColumns().setAll(Arrays.asList(idCol, titleCol, startCol, endCol));
        eventTable.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllEvents()));

        Label instructionLabel = new Label("📋 Select an event from the table above, then enter its ID below:");
        instructionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        idField.setPromptText("Event ID");

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(idField, 1, 0);

        mainContent.getChildren().addAll(eventTable, instructionLabel, grid);
        
        dialog.getDialogPane().setContent(mainContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    int eventId = Integer.parseInt(idField.getText());
                    if (manager.deleteEvent(eventId)) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Event deleted successfully!");
                        return eventId;
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Event not found!");
                    }
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid event ID!");
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showClashingEventsDialog() {
        List<MainEvent> clashingEvents = manager.findAllClashingEvents();
        
        if (clashingEvents.isEmpty()) {
            showAlert(Alert.AlertType.INFORMATION, "No Clashes", "✅ Great! You have no clashing events.");
            return;
        }
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Clashing Events");
        dialog.setHeaderText("⚠️ Events with Scheduling Conflicts");

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle(PANEL_STYLE);

        Label info = new Label("The following events have time conflicts with other events:");
        info.setWrapText(true);
        info.setStyle("-fx-font-weight: bold; -fx-text-fill: " + TEXT_PRIMARY + ";");
        content.getChildren().add(info);

        for (MainEvent event : clashingEvents) {
            VBox eventBox = new VBox(5);
            eventBox.setStyle(CARD_STYLE + "-fx-border-color: " + ACCENT + "; -fx-border-width: 1.5; -fx-padding: 10;");
            
            Label eventTitle = new Label("📌 " + event.getTitle());
            eventTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: " + TEXT_PRIMARY + ";");
            
            Label eventTime = new Label("🕐 " + event.getStartDateTime().format(dateTimeFormatter) + 
                                       " - " + event.getEndDateTime().format(dateTimeFormatter));
            eventTime.setStyle("-fx-text-fill: " + TEXT_MUTED + ";");
            
            // Find what it clashes with
            List<MainEvent> clashes = manager.findClashingEvents(event);
            Label clashInfo = new Label("⚠️ Clashes with: " + clashes.size() + " event(s)");
            clashInfo.setStyle("-fx-text-fill: " + ACCENT + ";");
            
            VBox clashDetails = new VBox(3);
            clashDetails.setPadding(new Insets(5, 0, 0, 20));
            for (MainEvent clash : clashes) {
                Label clashLabel = new Label("• " + clash.getTitle() + " (" + 
                    clash.getStartDateTime().format(dateTimeFormatter) + ")");
                clashLabel.setStyle("-fx-font-size: 12; -fx-text-fill: " + TEXT_MUTED + ";");
                clashDetails.getChildren().add(clashLabel);
            }
            
            eventBox.getChildren().addAll(eventTitle, eventTime, clashInfo, clashDetails);
            content.getChildren().add(eventBox);
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: #0f1c2d;");
        
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void showCalendarViewMenu(Stage stage) {
        VBox vbox = new VBox(18);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        vbox.setStyle("-fx-background-color: transparent;");

        // Add notification banner at the top
        VBox notificationBanner = NotificationManager.createNotificationBanner(manager);

        Label title = new Label("📆 CALENDAR VIEW");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        Button btnDailyView = createStyledButton("📅 Daily View", "#2196F3");
        btnDailyView.setOnAction(e -> showDailyViewDialog());

        Button btnWeeklyView = createStyledButton("📋 Weekly List View", "#1E88E5");
        btnWeeklyView.setOnAction(e -> showWeeklyViewDialog());

        Button btnMonthlyGridView = createStyledButton("📅 Monthly Calendar Grid", "#1565C0");
        btnMonthlyGridView.setOnAction(e -> showMonthlyGridViewDialog());

        Button btnCLIFormat = createStyledButton("📄 View CLI Format", "#00897B");
        btnCLIFormat.setOnAction(e -> showCLIFormatViewDialog());

        Button btnBack = createStyledButton("⬅️ Back to Main Menu", "#607D8B");
        btnBack.setOnAction(e -> {
            stage.setScene(new Scene(createMainMenu(stage), 1200, 800));
            stage.centerOnScreen();
        });

        VBox buttonContainer = new VBox(18);
        buttonContainer.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        buttonContainer.getChildren().addAll(btnDailyView, btnWeeklyView, btnMonthlyGridView, btnCLIFormat, btnBack);

        vbox.getChildren().addAll(notificationBanner, title, new Separator(), buttonContainer);

        StackPane root = new StackPane(vbox);
        root.setStyle(APP_GRADIENT);
        StackPane.setAlignment(vbox, javafx.geometry.Pos.TOP_LEFT);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void showMonthlyGridViewDialog() {
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Monthly Calendar Grid");
        dialog.setHeaderText("Select a date in the month");

        DatePicker datePicker = new DatePicker(LocalDate.of(LocalDate.now().getYear(), 1, 1)); // Default to January
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(new Label("Select any date in the month:"), datePicker);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                LocalDate selectedDate = datePicker.getValue();
                showMonthlyGridDisplay(selectedDate.getYear(), selectedDate.getMonthValue());
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showMonthlyGridDisplay(int year, int month) {
        Dialog<Boolean> dialog = new Dialog<>();
        LocalDate firstDay = LocalDate.of(year, month, 1);
        dialog.setTitle("Calendar - " + firstDay.getMonth() + " " + year);
        dialog.setHeaderText(firstDay.getMonth() + " " + year);

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));
        mainContent.setStyle(PANEL_STYLE);

        // Month and Year header
        Label monthLabel = new Label(firstDay.getMonth() + " " + year);
        monthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18; -fx-text-fill: " + ACCENT + ";");
        mainContent.getChildren().add(monthLabel);

        // Day names header
        HBox dayNamesBox = new HBox(2);
        dayNamesBox.setStyle("-fx-padding: 5; -fx-border-color: " + ACCENT + "; -fx-border-width: 1; -fx-background-color: #0f1c2d; -fx-background-radius: 8;");
        String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String dayName : dayNames) {
            Label dayLabel = new Label(dayName);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-background-color: rgba(33,212,228,0.12); " +
                            "-fx-padding: 8; -fx-alignment: center; -fx-pref-width: 90; -fx-background-radius: 6;");
            dayNamesBox.getChildren().add(dayLabel);
        }
        mainContent.getChildren().add(dayNamesBox);

        // Calendar grid
        GridPane calendarGrid = new GridPane();
        calendarGrid.setHgap(2);
        calendarGrid.setVgap(2);
        calendarGrid.setPadding(new Insets(5));
        calendarGrid.setStyle(CARD_STYLE + "-fx-padding: 6;");

        int lastDayOfMonth = firstDay.lengthOfMonth();
        int startDayOfWeek = firstDay.getDayOfWeek().getValue() % 7; // 0 = Sunday

        // Map events by date for quick lookup
        java.util.Map<Integer, List<MainEvent>> eventsByDate = new java.util.HashMap<>();
        for (MainEvent event : manager.getAllEventsExpanded()) {
            if (event.getStartDateTime().getYear() == year && event.getStartDateTime().getMonthValue() == month) {
                int day = event.getStartDateTime().getDayOfMonth();
                eventsByDate.computeIfAbsent(day, k -> new ArrayList<>()).add(event);
            }
        }

        // Add empty cells for days before month starts
        for (int i = 0; i < startDayOfWeek; i++) {
            VBox emptyCell = createCalendarCell(-1, null, 0);
            calendarGrid.add(emptyCell, i, 0);
        }

        // Add date cells
        int row = 0;
        int col = startDayOfWeek;
        for (int day = 1; day <= lastDayOfMonth; day++) {
            List<MainEvent> dayEvents = eventsByDate.getOrDefault(day, new ArrayList<>());
            VBox dateCell = createCalendarCell(day, dayEvents, month);
            calendarGrid.add(dateCell, col, row);

            col++;
            if (col == 7) {
                col = 0;
                row++;
            }
        }

        ScrollPane scrollPane = new ScrollPane(calendarGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(450);
        scrollPane.setStyle("-fx-background: transparent; -fx-background-color: #0f1c2d;");

        mainContent.getChildren().add(scrollPane);

        dialog.getDialogPane().setContent(mainContent);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private VBox createCalendarCell(int day, List<MainEvent> events, int month) {
        VBox cell = new VBox(2);
        cell.setPrefWidth(90);
        cell.setPrefHeight(110);
        cell.setStyle(CARD_STYLE + "-fx-border-color: rgba(33,212,228,0.25); -fx-border-width: 1; -fx-padding: 6;");

        if (day == -1) {
            // Empty cell
            return cell;
        }

        // Date number
        Label dateLabel = new Label(String.valueOf(day));
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: " + TEXT_PRIMARY + ";");
        cell.getChildren().add(dateLabel);

        if (events != null && !events.isEmpty()) {
            // Event indicator
            Label eventIndicator = new Label("● " + events.size() + " event" + (events.size() > 1 ? "s" : ""));
            eventIndicator.setStyle("-fx-font-size: 10; -fx-text-fill: " + ACCENT + "; -fx-font-weight: bold;");
            cell.getChildren().add(eventIndicator);

            // Show first 2 events in cell
            for (int i = 0; i < Math.min(2, events.size()); i++) {
                MainEvent event = events.get(i);
                Label eventLabel = new Label(event.getStartDateTime().format(DateTimeFormatter.ofPattern("HH:mm")) + 
                                           " " + truncateString(event.getTitle(), 12));
                eventLabel.setStyle("-fx-font-size: 9; -fx-text-fill: " + TEXT_PRIMARY + "; -fx-wrap-text: true;");
                eventLabel.setWrapText(true);
                cell.getChildren().add(eventLabel);
            }

            if (events.size() > 2) {
                Label moreLabel = new Label("+" + (events.size() - 2) + " more");
                moreLabel.setStyle("-fx-font-size: 8; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-style: italic;");
                cell.getChildren().add(moreLabel);
            }

            cell.setStyle(CARD_STYLE + "-fx-border-color: " + ACCENT + "; -fx-border-width: 1.5; -fx-padding: 6;");
        } else {
            Label noEventsLabel = new Label("No events");
            noEventsLabel.setStyle("-fx-font-size: 8; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-style: italic;");
            cell.getChildren().add(noEventsLabel);
        }

        return cell;
    }

    private String truncateString(String str, int maxLength) {
        if (str.length() <= maxLength) {
            return str;
        }
        return str.substring(0, maxLength - 2) + "..";
    }

    private void showWeeklyViewDialog() {
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Weekly View");
        dialog.setHeaderText("Select a date in the week");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(new Label("Select any date in the week:"), datePicker);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                LocalDate selectedDate = datePicker.getValue();
                showWeeklyEventsDialog(selectedDate);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showWeeklyEventsDialog(LocalDate date) {
        Dialog<Boolean> dialog = new Dialog<>();
        
        // Calculate week start (Sunday)
        LocalDate weekStart = date.minusDays(date.getDayOfWeek().getValue() % 7);
        LocalDate weekEnd = weekStart.plusDays(6);
        
        dialog.setTitle("Weekly View");
        dialog.setHeaderText("Week of " + weekStart + " to " + weekEnd);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle(PANEL_STYLE);

        String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        LocalDate currentDay = weekStart;

        for (int i = 0; i < 7; i++) {
            VBox dayBox = new VBox(8);
            dayBox.setStyle(CARD_STYLE + "-fx-border-color: " + ACCENT + "; -fx-border-width: 1.5; -fx-padding: 10;");
            
            LocalDate finalCurrentDay = currentDay;
            Label dayLabel = new Label("📌 " + dayNames[i] + " - " + currentDay);
            dayLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: " + TEXT_PRIMARY + ";");
            dayBox.getChildren().add(dayLabel);

            boolean hasEvent = false;
            for (MainEvent event : manager.getAllEventsExpanded()) {
                if (event.getStartDateTime().toLocalDate().equals(finalCurrentDay)) {
                    VBox eventItemBox = new VBox(3);
                    eventItemBox.setPadding(new Insets(5, 0, 0, 20));
                    
                    Label eventLabel = new Label("🕐 " + event.getStartDateTime().format(dateTimeFormatter) + 
                                                " - " + event.getTitle());
                    eventLabel.setStyle("-fx-font-size: 12; -fx-text-fill: " + TEXT_PRIMARY + ";");
                    
                    Label descLabel = new Label("   " + event.getDescription());
                    descLabel.setStyle("-fx-font-size: 10; -fx-text-fill: " + TEXT_MUTED + ";");
                    
                    eventItemBox.getChildren().addAll(eventLabel, descLabel);
                    dayBox.getChildren().add(eventItemBox);
                    hasEvent = true;
                }
            }
            
            if (!hasEvent) {
                Label noEventsLabel = new Label("   No events");
                noEventsLabel.setStyle("-fx-font-size: 12; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-style: italic;");
                dayBox.getChildren().add(noEventsLabel);
            }

            content.getChildren().add(dayBox);
            currentDay = currentDay.plusDays(1);
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(500);
        
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void showDailyViewDialog() {
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Daily View");
        dialog.setHeaderText("Select Date");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(new Label("Select Date:"), datePicker);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                LocalDate selectedDate = datePicker.getValue();
                showDailyEventsDialog(selectedDate);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showDailyEventsDialog(LocalDate date) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Daily View - " + date);
        dialog.setHeaderText("Events for " + date);

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle(PANEL_STYLE);

        Label dateLabel = new Label("📅 " + date.getDayOfWeek() + ", " + date);
        dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: " + TEXT_PRIMARY + ";");
        content.getChildren().add(dateLabel);

        List<MainEvent> dailyEvents = new ArrayList<>();
        for (MainEvent event : manager.getAllEventsExpanded()) {
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                dailyEvents.add(event);
            }
        }

        if (dailyEvents.isEmpty()) {
            Label noEventsLabel = new Label("✅ No events scheduled for this day");
            noEventsLabel.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-style: italic;");
            content.getChildren().add(noEventsLabel);
        } else {
            // Sort events by start time
            dailyEvents.sort((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()));
            
            for (MainEvent event : dailyEvents) {
                VBox eventBox = new VBox(5);
                eventBox.setStyle(CARD_STYLE + "-fx-border-color: rgba(33,212,228,0.35); -fx-border-width: 1.5; -fx-padding: 12;");
                
                HBox timeBox = new HBox(10);
                Label timeLabel = new Label("🕐 " + event.getStartDateTime().format(dateTimeFormatter));
                timeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: " + ACCENT + ";");
                Label durationLabel = new Label("→ " + event.getEndDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                durationLabel.setStyle("-fx-font-size: 12; -fx-text-fill: " + TEXT_MUTED + ";");
                timeBox.getChildren().addAll(timeLabel, durationLabel);
                
                Label titleLabel = new Label("📌 " + event.getTitle());
                titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13; -fx-text-fill: " + TEXT_PRIMARY + ";");
                
                Label descLabel = new Label("📝 " + event.getDescription());
                descLabel.setStyle("-fx-font-size: 11; -fx-text-fill: " + TEXT_MUTED + ";");
                
                eventBox.getChildren().addAll(timeBox, titleLabel, descLabel);
                content.getChildren().add(eventBox);
            }
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showMonthlyViewDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Monthly View");
        dialog.setHeaderText("Select Year and Month");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Spinner<Integer> yearSpinner = new Spinner<>(2000, 2100, LocalDate.now().getYear());
        Spinner<Integer> monthSpinner = new Spinner<>(1, 12, LocalDate.now().getMonthValue());

        grid.add(new Label("Year:"), 0, 0);
        grid.add(yearSpinner, 1, 0);
        grid.add(new Label("Month:"), 0, 1);
        grid.add(monthSpinner, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                showMonthlyEventsDialog(yearSpinner.getValue(), monthSpinner.getValue());
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showMonthlyEventsDialog(int year, int month) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Monthly View - " + year + "-" + String.format("%02d", month));
        dialog.setHeaderText("All Events for " + year + "-" + String.format("%02d", month));

        VBox content = new VBox(10);
        content.setPadding(new Insets(20));
        content.setStyle(PANEL_STYLE);

        Label monthLabel = new Label("📅 " + LocalDate.of(year, month, 1).getMonth() + " " + year);
        monthLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16; -fx-text-fill: " + ACCENT + ";");
        content.getChildren().add(monthLabel);

        List<MainEvent> monthlyEvents = new ArrayList<>();
        for (MainEvent event : manager.getAllEventsExpanded()) {
            if (event.getStartDateTime().getYear() == year && event.getStartDateTime().getMonthValue() == month) {
                monthlyEvents.add(event);
            }
        }

        if (monthlyEvents.isEmpty()) {
            Label noEventsLabel = new Label("✅ No events scheduled for this month");
            noEventsLabel.setStyle("-fx-font-size: 14; -fx-text-fill: " + TEXT_MUTED + "; -fx-font-style: italic;");
            content.getChildren().add(noEventsLabel);
        } else {
            // Group events by date
            monthlyEvents.sort((e1, e2) -> e1.getStartDateTime().compareTo(e2.getStartDateTime()));
            
            LocalDate currentDate = null;
            VBox dateGroupBox = null;
            
            for (MainEvent event : monthlyEvents) {
                LocalDate eventDate = event.getStartDateTime().toLocalDate();
                
                if (!eventDate.equals(currentDate)) {
                    if (dateGroupBox != null) {
                        content.getChildren().add(dateGroupBox);
                    }
                    
                    dateGroupBox = new VBox(5);
                    dateGroupBox.setStyle(CARD_STYLE + "-fx-border-color: rgba(33,212,228,0.35); -fx-border-width: 1.5; -fx-padding: 10;");
                    
                    Label dateLabel = new Label("📌 " + eventDate.getDayOfWeek() + ", " + eventDate);
                    dateLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12; -fx-text-fill: " + TEXT_PRIMARY + ";");
                    dateGroupBox.getChildren().add(dateLabel);
                    
                    currentDate = eventDate;
                }
                
                HBox eventItem = new HBox(15);
                eventItem.setPadding(new Insets(5, 0, 5, 20));
                Label timeLabel = new Label("🕐 " + event.getStartDateTime().format(DateTimeFormatter.ofPattern("HH:mm")));
                timeLabel.setStyle("-fx-font-size: 11; -fx-text-fill: " + ACCENT + "; -fx-font-weight: bold;");
                Label titleLabel = new Label(event.getTitle());
                titleLabel.setStyle("-fx-font-size: 11; -fx-text-fill: " + TEXT_PRIMARY + ";");
                eventItem.getChildren().addAll(timeLabel, titleLabel);
                
                dateGroupBox.getChildren().add(eventItem);
            }
            
            if (dateGroupBox != null) {
                content.getChildren().add(dateGroupBox);
            }
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        
        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showSearchEventMenu(Stage stage) {
        VBox vbox = new VBox(18);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        vbox.setStyle("-fx-background-color: transparent;");

        // Add notification banner at the top
        VBox notificationBanner = NotificationManager.createNotificationBanner(manager);

        Label title = new Label("🔍 SEARCH EVENT");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        Button btnSearchByDate = createStyledButton("📅 Search by Date", "#FF9800");
        btnSearchByDate.setOnAction(e -> showSearchByDateDialog());

        Button btnSearchByDateRange = createStyledButton("📆 Search by Date Range", "#FF5722");
        btnSearchByDateRange.setOnAction(e -> showSearchByDateRangeDialog());

        Button btnSearchByTitle = createStyledButton("📝 Search by Event Title", "#F44336");
        btnSearchByTitle.setOnAction(e -> showSearchByTitleDialog());

        Button btnAdvancedSearch = createStyledButton("🔎 Advanced Search & Filter", "#00ACC1");
        btnAdvancedSearch.setOnAction(e -> showAdvancedSearchDialog());

        Button btnBack = createStyledButton("⬅️ Back to Main Menu", "#607D8B");
        btnBack.setOnAction(e -> {
            stage.setScene(new Scene(createMainMenu(stage), 1200, 800));
            stage.centerOnScreen();
        });

        vbox.getChildren().addAll(notificationBanner, title, new Separator(), btnSearchByDate, btnSearchByDateRange, 
                      btnSearchByTitle, btnAdvancedSearch, btnBack);

        StackPane root = new StackPane(vbox);
        root.setStyle(APP_GRADIENT);
        StackPane.setAlignment(vbox, javafx.geometry.Pos.TOP_LEFT);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void showSearchByDateDialog() {
        Dialog<LocalDate> dialog = new Dialog<>();
        dialog.setTitle("Search by Date");
        dialog.setHeaderText("Select Date to Search");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(new Label("Date:"), datePicker);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                showSearchResultsDialog(datePicker.getValue());
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showSearchResultsDialog(LocalDate date) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Search Results");
        dialog.setHeaderText("Events on " + date);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        java.util.List<MainEvent> results = new java.util.ArrayList<>();
        for (MainEvent event : manager.getAllEventsExpanded()) {
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                results.add(event);
            }
        }

        if (results.isEmpty()) {
            textArea.setText("No events found on this date.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (MainEvent event : results) {
                sb.append("ID: ").append(event.getEventId()).append("\n");
                sb.append("Title: ").append(event.getTitle()).append("\n");
                sb.append("Time: ").append(event.getStartDateTime().format(dateTimeFormatter)).append("\n");
                sb.append("---\n");
            }
            textArea.setText(sb.toString());
        }

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showSearchByDateRangeDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Search by Date Range");
        dialog.setHeaderText("Select Date Range");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        DatePicker startDatePicker = new DatePicker(LocalDate.now());
        DatePicker endDatePicker = new DatePicker(LocalDate.now().plusDays(7));

        grid.add(new Label("Start Date:"), 0, 0);
        grid.add(startDatePicker, 1, 0);
        grid.add(new Label("End Date:"), 0, 1);
        grid.add(endDatePicker, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                showSearchRangeResultsDialog(startDatePicker.getValue(), endDatePicker.getValue());
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showSearchRangeResultsDialog(LocalDate startDate, LocalDate endDate) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Search Results");
        dialog.setHeaderText("Events from " + startDate + " to " + endDate);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        java.util.List<MainEvent> results = new java.util.ArrayList<>();
        for (MainEvent event : manager.getAllEventsExpanded()) {
            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            if (!eventDate.isBefore(startDate) && !eventDate.isAfter(endDate)) {
                results.add(event);
            }
        }

        if (results.isEmpty()) {
            textArea.setText("No events found in this date range.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (MainEvent event : results) {
                sb.append("ID: ").append(event.getEventId()).append("\n");
                sb.append("Title: ").append(event.getTitle()).append("\n");
                sb.append("Date: ").append(event.getStartDateTime().format(dateTimeFormatter)).append("\n");
                sb.append("---\n");
            }
            textArea.setText(sb.toString());
        }

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showSearchByTitleDialog() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Search by Title");
        dialog.setHeaderText("Enter Event Title");

        TextField titleField = new TextField();
        titleField.setPromptText("Event Title");
        VBox content = new VBox(10);
        content.setPadding(new Insets(10));
        content.getChildren().addAll(new Label("Title:"), titleField);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                showSearchTitleResultsDialog(titleField.getText());
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showSearchTitleResultsDialog(String title) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Search Results");
        dialog.setHeaderText("Events matching '" + title + "'");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        java.util.List<MainEvent> results = new java.util.ArrayList<>();
        for (MainEvent event : manager.getAllEventsExpanded()) {
            if (event.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(event);
            }
        }

        if (results.isEmpty()) {
            textArea.setText("No events found with that title.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (MainEvent event : results) {
                sb.append("ID: ").append(event.getEventId()).append("\n");
                sb.append("Title: ").append(event.getTitle()).append("\n");
                sb.append("Description: ").append(event.getDescription()).append("\n");
                sb.append("Date: ").append(event.getStartDateTime().format(dateTimeFormatter)).append("\n");
                sb.append("---\n");
            }
            textArea.setText(sb.toString());
        }

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showAdvancedSearchDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Advanced Search & Filter");
        dialog.setHeaderText("Combine multiple filters");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField titleField = new TextField();
        titleField.setPromptText("Title contains (optional)");

        TextField descField = new TextField();
        descField.setPromptText("Description contains (optional)");

        DatePicker startDatePicker = new DatePicker();
        DatePicker endDatePicker = new DatePicker();

        TimePicker startTimePicker = new TimePicker(null);
        TimePicker endTimePicker = new TimePicker(null);
        CheckBox startTimeFilter = new CheckBox("Filter start time");
        CheckBox endTimeFilter = new CheckBox("Filter end time");

        startTimeFilter.selectedProperty().addListener((obs, oldV, newV) -> startTimePicker.setDisable(!newV));
        endTimeFilter.selectedProperty().addListener((obs, oldV, newV) -> endTimePicker.setDisable(!newV));
        startTimePicker.setDisable(true);
        endTimePicker.setDisable(true);

        CheckBox remindersOnly = new CheckBox("Only events with reminders");

        // Additional field filters
        TextField locationField = new TextField();
        locationField.setPromptText("Location contains (optional)");
        ComboBox<String> categoryBox = new ComboBox<>();
        categoryBox.getItems().addAll("All Categories", "General", "Work", "Personal", "Meeting", "Study", "Health", "Other");
        categoryBox.setValue("All Categories");
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("All Priorities", "HIGH", "MEDIUM", "LOW");
        priorityBox.setValue("All Priorities");

        grid.add(new Label("Title contains:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description contains:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Start date (from):"), 0, 2);
        grid.add(startDatePicker, 1, 2);
        grid.add(new Label("End date (to):"), 0, 3);
        grid.add(endDatePicker, 1, 3);
        grid.add(startTimeFilter, 0, 4);
        grid.add(startTimePicker, 1, 4);
        grid.add(endTimeFilter, 0, 5);
        grid.add(endTimePicker, 1, 5);
        grid.add(remindersOnly, 1, 6);
        grid.add(new Label("Location:"), 0, 7);
        grid.add(locationField, 1, 7);
        grid.add(new Label("Category:"), 0, 8);
        grid.add(categoryBox, 1, 8);
        grid.add(new Label("Priority:"), 0, 9);
        grid.add(priorityBox, 1, 9);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String titleQuery = titleField.getText().trim();
                    String descQuery = descField.getText().trim();
                    LocalDate startDate = startDatePicker.getValue();
                    LocalDate endDate = endDatePicker.getValue();

                    LocalTime startTime = startTimeFilter.isSelected() ? startTimePicker.getValue() : null;
                    LocalTime endTime = endTimeFilter.isSelected() ? endTimePicker.getValue() : null;

                    String locationQuery = locationField.getText().trim();
                    String categoryFilter = categoryBox.getValue().equals("All Categories") ? null : categoryBox.getValue();
                    String priorityFilter = priorityBox.getValue().equals("All Priorities") ? null : priorityBox.getValue();

                    java.util.List<MainEvent> results = filterAdvanced(titleQuery, descQuery, startDate, endDate, 
                                                                       startTime, endTime, remindersOnly.isSelected(),
                                                                       locationQuery, categoryFilter, priorityFilter);
                    showAdvancedSearchResults(results, titleQuery, descQuery, startDate, endDate, startTime, endTime, remindersOnly.isSelected());
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid input", "Please check your selected filters.");
                    return null;
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private java.util.List<MainEvent> filterAdvanced(String titleQuery, String descQuery,
                                                     LocalDate startDate, LocalDate endDate,
                                                     LocalTime startTime, LocalTime endTime,
                                                     boolean remindersOnly,
                                                     String locationQuery, String categoryFilter, String priorityFilter) {
        java.util.List<MainEvent> results = new java.util.ArrayList<>();

        for (MainEvent event : manager.getAllEventsExpanded()) {
            boolean match = true;

            if (titleQuery != null && !titleQuery.isEmpty()) {
                match &= event.getTitle().toLowerCase().contains(titleQuery.toLowerCase());
            }

            if (descQuery != null && !descQuery.isEmpty()) {
                match &= event.getDescription() != null && event.getDescription().toLowerCase().contains(descQuery.toLowerCase());
            }

            LocalDate eventDate = event.getStartDateTime().toLocalDate();
            LocalTime eventTime = event.getStartDateTime().toLocalTime();

            if (startDate != null && eventDate.isBefore(startDate)) {
                match = false;
            }
            if (endDate != null && eventDate.isAfter(endDate)) {
                match = false;
            }

            if (startTime != null && eventTime.isBefore(startTime)) {
                match = false;
            }
            if (endTime != null && eventTime.isAfter(endTime)) {
                match = false;
            }

            if (remindersOnly && event.getReminder() == null) {
                match = false;
            }

            // Additional field filters
            if (locationQuery != null && !locationQuery.isEmpty()) {
                match &= event.getLocation() != null && event.getLocation().toLowerCase().contains(locationQuery.toLowerCase());
            }

            if (categoryFilter != null) {
                match &= event.getCategory() != null && event.getCategory().equals(categoryFilter);
            }

            if (priorityFilter != null) {
                match &= event.getPriority() != null && event.getPriority().equals(priorityFilter);
            }

            if (match) {
                results.add(event);
            }
        }

        return results;
    }

    private void showAdvancedSearchResults(java.util.List<MainEvent> results, String titleQuery, String descQuery,
                                           LocalDate startDate, LocalDate endDate,
                                           LocalTime startTime, LocalTime endTime,
                                           boolean remindersOnly) {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Advanced Search Results");

        StringBuilder header = new StringBuilder("Filters: ");
        if (titleQuery != null && !titleQuery.isEmpty()) header.append("title contains '" + titleQuery + "'; ");
        if (descQuery != null && !descQuery.isEmpty()) header.append("desc contains '" + descQuery + "'; ");
        if (startDate != null) header.append("from " + startDate + "; ");
        if (endDate != null) header.append("to " + endDate + "; ");
        if (startTime != null) header.append("time >= " + startTime + "; ");
        if (endTime != null) header.append("time <= " + endTime + "; ");
        if (remindersOnly) header.append("with reminders; ");
        dialog.setHeaderText(header.toString());

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);

        if (results.isEmpty()) {
            textArea.setText("No events match the selected filters.");
        } else {
            StringBuilder sb = new StringBuilder();
            for (MainEvent event : results) {
                sb.append("ID: ").append(event.getEventId()).append("\n");
                sb.append("Title: ").append(event.getTitle()).append("\n");
                sb.append("Description: ").append(event.getDescription()).append("\n");
                sb.append("Date: ").append(event.getStartDateTime().format(dateTimeFormatter)).append("\n");
                if (event.getReminder() != null) {
                    sb.append("Reminder: ").append(event.getReminder().getMinutesBefore()).append(" minutes before\n");
                }
                sb.append("---\n");
            }
            textArea.setText(sb.toString());
        }

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showManageRemindersMenu(Stage stage) {
        VBox vbox = new VBox(18);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        vbox.setStyle("-fx-background-color: transparent;");

        // Add notification banner at the top
        VBox notificationBanner = NotificationManager.createNotificationBanner(manager);

        Label title = new Label("⏰ MANAGE REMINDERS");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        Button btnSetReminder = createStyledButton("➕ Set Reminder for Event", "#9C27B0");
        btnSetReminder.setOnAction(e -> showSetReminderDialog());

        Button btnViewReminders = createStyledButton("📋 View Event Reminders", "#BA68C8");
        btnViewReminders.setOnAction(e -> showViewRemindersDialog());

        Button btnRemoveReminder = createStyledButton("🗑️ Remove Reminder", "#E91E63");
        btnRemoveReminder.setOnAction(e -> showRemoveReminderDialog());

        Button btnCheckReminders = createStyledButton("🔔 Check Reminders Now", "#673AB7");
        btnCheckReminders.setOnAction(e -> {
            List<MainEvent> upcomingReminders = NotificationManager.getUpcomingReminders(manager);
            if (upcomingReminders.isEmpty()) {
                showAlert(Alert.AlertType.INFORMATION, "Reminders", "No upcoming reminders at this time.");
            } else {
                showUpcomingRemindersDialog(upcomingReminders);
            }
        });

        Button btnBack = createStyledButton("⬅️ Back to Main Menu", "#607D8B");
        btnBack.setOnAction(e -> {
            stage.setScene(new Scene(createMainMenu(stage), 1200, 800));
            stage.centerOnScreen();
        });

        vbox.getChildren().addAll(notificationBanner, title, new Separator(), btnSetReminder, btnViewReminders, 
                                  btnRemoveReminder, btnCheckReminders, btnBack);

        StackPane root = new StackPane(vbox);
        root.setStyle(APP_GRADIENT);
        StackPane.setAlignment(vbox, javafx.geometry.Pos.TOP_LEFT);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void showSetReminderDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Set Reminder");
        dialog.setHeaderText("Set Reminder for Event");

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));

        // Create table showing all events
        TableView<MainEvent> eventTable = new TableView<>();
        eventTable.setPrefHeight(300);
        
        TableColumn<MainEvent, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getEventId()));
        idCol.setPrefWidth(50);
        
        TableColumn<MainEvent, String> titleCol = new TableColumn<>("Event Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.setPrefWidth(200);
        
        TableColumn<MainEvent, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getStartDateTime().format(dateTimeFormatter)));
        startCol.setPrefWidth(150);
        
        TableColumn<MainEvent, String> reminderCol = new TableColumn<>("Current Reminder");
        reminderCol.setCellValueFactory(data -> {
            Reminder r = data.getValue().getReminder();
            String text = r != null ? r.getDisplayText() : "None";
            return new javafx.beans.property.SimpleStringProperty(text);
        });
        reminderCol.setPrefWidth(150);
        
        eventTable.getColumns().setAll(Arrays.asList(idCol, titleCol, startCol, reminderCol));
        eventTable.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllEvents()));

        Label instructionLabel = new Label("📋 Select an event from the table above, then enter details below:");
        instructionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        idField.setPromptText("Event ID");
        ComboBox<String> reminderBox = new ComboBox<>();
        reminderBox.getItems().addAll("15 minutes before", "30 minutes before", "1 hour before", 
                                      "2 hours before", "1 day before", "Custom minutes");

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Reminder Time:"), 0, 1);
        grid.add(reminderBox, 1, 1);

        mainContent.getChildren().addAll(eventTable, instructionLabel, grid);

        dialog.getDialogPane().setContent(mainContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    int eventId = Integer.parseInt(idField.getText());
                    MainEvent event = manager.findEventById(eventId);
                    if (event != null) {
                        String reminderChoice = reminderBox.getValue();
                        int minutesBefore = 0;

                        switch (reminderChoice) {
                            case "15 minutes before": minutesBefore = 15; break;
                            case "30 minutes before": minutesBefore = 30; break;
                            case "1 hour before": minutesBefore = 60; break;
                            case "2 hours before": minutesBefore = 120; break;
                            case "1 day before": minutesBefore = 1440; break;
                            case "Custom minutes":
                                TextInputDialog customDialog = new TextInputDialog();
                                customDialog.setTitle("Custom Reminder");
                                customDialog.setHeaderText("Enter minutes before event:");
                                java.util.Optional<String> result = customDialog.showAndWait();
                                if (result.isPresent()) {
                                    minutesBefore = Integer.parseInt(result.get());
                                }
                                break;
                        }

                        event.setReminder(new Reminder(minutesBefore));
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Reminder set for '" + event.getTitle() + "'");
                        return true;
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Event not found!");
                    }
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid input!");
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    private void showViewRemindersDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Event Reminders");
        dialog.setHeaderText("All Event Reminders");

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefHeight(400);

        StringBuilder sb = new StringBuilder();
        boolean hasReminders = false;
        for (MainEvent event : manager.getAllEventsExpanded()) {
            if (event.getReminder() != null) {
                hasReminders = true;
                sb.append("ID: ").append(event.getEventId()).append("\n");
                sb.append("Event: ").append(event.getTitle()).append("\n");
                sb.append("Start: ").append(event.getStartDateTime().format(dateTimeFormatter)).append("\n");
                sb.append("Reminder: ").append(event.getReminder().getDisplayText()).append("\n");
                sb.append("---\n");
            }
        }

        if (!hasReminders) {
            sb.append("No reminders set.");
        }

        textArea.setText(sb.toString());
        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showRemoveReminderDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Remove Reminder");
        dialog.setHeaderText("Remove Reminder from Event");

        VBox mainContent = new VBox(15);
        mainContent.setPadding(new Insets(20));

        // Create table showing events with reminders
        TableView<MainEvent> eventTable = new TableView<>();
        eventTable.setPrefHeight(300);
        
        TableColumn<MainEvent, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getEventId()));
        idCol.setPrefWidth(50);
        
        TableColumn<MainEvent, String> titleCol = new TableColumn<>("Event Title");
        titleCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getTitle()));
        titleCol.setPrefWidth(200);
        
        TableColumn<MainEvent, String> startCol = new TableColumn<>("Start Time");
        startCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getStartDateTime().format(dateTimeFormatter)));
        startCol.setPrefWidth(150);
        
        TableColumn<MainEvent, String> reminderCol = new TableColumn<>("Current Reminder");
        reminderCol.setCellValueFactory(data -> {
            Reminder r = data.getValue().getReminder();
            String text = r != null ? r.getDisplayText() : "None";
            return new javafx.beans.property.SimpleStringProperty(text);
        });
        reminderCol.setPrefWidth(150);
        
        eventTable.getColumns().setAll(Arrays.asList(idCol, titleCol, startCol, reminderCol));
        eventTable.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllEvents()));

        Label instructionLabel = new Label("📋 Select an event from the table above, then enter its ID below:");
        instructionLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #333;");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField idField = new TextField();
        idField.setPromptText("Event ID");

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(idField, 1, 0);

        mainContent.getChildren().addAll(eventTable, instructionLabel, grid);

        dialog.getDialogPane().setContent(mainContent);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    int eventId = Integer.parseInt(idField.getText());
                    MainEvent event = manager.findEventById(eventId);
                    if (event != null) {
                        if (event.getReminder() != null) {
                            event.setReminder(null);
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Reminder removed from '" + event.getTitle() + "'");
                            return true;
                        } else {
                            showAlert(Alert.AlertType.WARNING, "Info", "This event has no reminder set.");
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Event not found!");
                    }
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Invalid event ID!");
                }
            }
            return false;
        });

        dialog.showAndWait();
    }

    private void showUpcomingRemindersDialog(List<MainEvent> upcomingReminders) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Upcoming Reminders");
        dialog.setHeaderText("🔔 You have " + upcomingReminders.size() + " upcoming event(s)");

        VBox content = new VBox(12);
        content.setPadding(new Insets(15));
        content.setStyle("-fx-background-color: #f5f5f5;");

        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm");

        for (MainEvent event : upcomingReminders) {
            VBox eventBox = new VBox(6);
            eventBox.setPadding(new Insets(12));
            
            long minutesUntil = java.time.temporal.ChronoUnit.MINUTES.between(now, event.getStartDateTime());
            String bgColor;
            
            if (minutesUntil <= 15) {
                bgColor = "#ffebee"; // Light red
            } else if (minutesUntil <= 60) {
                bgColor = "#fff3e0"; // Light orange
            } else {
                bgColor = "#e3f2fd"; // Light blue
            }
            
            eventBox.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                "-fx-background-radius: 8;" +
                "-fx-border-color: #ddd;" +
                "-fx-border-radius: 8;" +
                "-fx-border-width: 1;"
            );

            Label titleLabel = new Label("📌 " + event.getTitle());
            titleLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

            Label timeLabel = new Label("⏰ " + event.getStartDateTime().format(formatter) + 
                                       " (in " + NotificationManager.formatDuration(minutesUntil) + ")");
            timeLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

            if (event.getDescription() != null && !event.getDescription().isEmpty()) {
                Label descLabel = new Label("📝 " + event.getDescription());
                descLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #888;");
                descLabel.setWrapText(true);
                eventBox.getChildren().addAll(titleLabel, timeLabel, descLabel);
            } else {
                eventBox.getChildren().addAll(titleLabel, timeLabel);
            }

            content.getChildren().add(eventBox);
        }

        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(400);
        scrollPane.setPrefWidth(500);

        dialog.getDialogPane().setContent(scrollPane);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showCLIFormatViewDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("CLI Format Views");
        dialog.setHeaderText("Select View Type and Date");

        VBox content = new VBox(15);
        content.setPadding(new Insets(20));

        ComboBox<String> viewTypeBox = new ComboBox<>();
        viewTypeBox.getItems().addAll("Weekly List View", "Monthly Calendar View");
        viewTypeBox.setValue("Weekly List View");

        DatePicker datePicker = new DatePicker(LocalDate.now());

        content.getChildren().addAll(
            new Label("View Type:"), viewTypeBox,
            new Label("Select Date:"), datePicker
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                String viewType = viewTypeBox.getValue();
                LocalDate date = datePicker.getValue();
                showCLIFormatOutput(viewType, date);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showCLIFormatOutput(String viewType, LocalDate date) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("CLI Format - " + viewType);
        dialog.setHeaderText(viewType + " for " + date);

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(false);
        textArea.setPrefWidth(700);
        textArea.setPrefHeight(500);
        textArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12;");

        // Capture console output
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream old = System.out;
        System.setOut(ps);

        try {
            if (viewType.equals("Weekly List View")) {
                calendarView.displayWeeklyList(manager.getAllEventsExpanded(), date);
            } else if (viewType.equals("Monthly Calendar View")) {
                calendarView.displayMonthlyView(manager.getAllEventsExpanded(), 
                    date.getYear(), date.getMonthValue());
            }
            
            System.out.flush();
            System.setOut(old);
            
            textArea.setText(baos.toString());
        } catch (Exception e) {
            System.setOut(old);
            textArea.setText("Error generating view: " + e.getMessage());
        }

        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
    }

    private void showBackupRestoreMenu(Stage stage) {
        VBox vbox = new VBox(18);
        vbox.setPadding(new Insets(30));
        vbox.setAlignment(javafx.geometry.Pos.TOP_LEFT);
        vbox.setStyle("-fx-background-color: transparent;");

        // Add notification banner at the top
        VBox notificationBanner = NotificationManager.createNotificationBanner(manager);

        Label title = new Label("💾 BACKUP & RESTORE");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);");

        Button btnCreateBackup = createStyledButton("📦 Create Backup", "#4CAF50");
        btnCreateBackup.setOnAction(e -> createBackup());

        Button btnRestoreBackup = createStyledButton("♻️ Restore from Backup", "#2196F3");
        btnRestoreBackup.setOnAction(e -> restoreBackup());

        Button btnBack = createStyledButton("⬅️ Back to Main Menu", "#607D8B");
        btnBack.setOnAction(e -> {
            stage.setScene(new Scene(createMainMenu(stage), 1200, 800));
            stage.centerOnScreen();
        });

        vbox.getChildren().addAll(notificationBanner, title, new Separator(), btnCreateBackup, btnRestoreBackup, btnBack);

        StackPane root = new StackPane(vbox);
        root.setStyle(APP_GRADIENT);
        StackPane.setAlignment(vbox, javafx.geometry.Pos.TOP_LEFT);

        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.centerOnScreen();
    }

    private void createBackup() {
        TextInputDialog dialog = new TextInputDialog("backup_" + java.time.LocalDate.now().toString() + ".txt");
        dialog.setTitle("Create Backup");
        dialog.setHeaderText("Create Backup File");
        dialog.setContentText("Enter backup filename:");

        java.util.Optional<String> result = dialog.showAndWait();
        result.ifPresent(filename -> {
            try {
                // First save current state to CSV files
                CSVHandlerCompliant.saveEvents(manager);
                
                // Create backup directory if it doesn't exist
                java.io.File backupDir = new java.io.File("backups");
                if (!backupDir.exists()) {
                    backupDir.mkdir();
                }
                
                String backupPath = "backups/" + filename;
                
                // Create backup using BackupManager (copy from root folder logic)
                java.nio.file.Path backup = java.nio.file.Paths.get(backupPath);
                if (backup.getParent() != null) {
                    java.nio.file.Files.createDirectories(backup.getParent());
                }

                try (java.io.BufferedWriter writer = java.nio.file.Files.newBufferedWriter(backup, java.nio.charset.StandardCharsets.UTF_8)) {
                    writer.write("--- event.csv ---\n");
                    copyFileToBackup("event.csv", writer);
                    writer.write("\n--- recurrent.csv ---\n");
                    copyFileToBackup("recurrent.csv", writer);
                    writer.write("\n--- additional.csv ---\n");
                    copyFileToBackup("additional.csv", writer);
                    writer.flush();
                }
                
                showAlert(Alert.AlertType.INFORMATION, "Backup Created", 
                         "Backup successfully created at: " + backupPath);
            } catch (Exception ex) {
                showAlert(Alert.AlertType.ERROR, "Backup Failed", 
                         "Error creating backup: " + ex.getMessage());
            }
        });
    }

    private void copyFileToBackup(String sourceFile, java.io.BufferedWriter writer) throws java.io.IOException {
        java.nio.file.Path path = java.nio.file.Paths.get(sourceFile);
        if (!java.nio.file.Files.exists(path)) {
            writer.write("# (file missing)\n");
            return;
        }
        try (java.io.BufferedReader reader = java.nio.file.Files.newBufferedReader(path, java.nio.charset.StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.write('\n');
            }
        }
    }

    private void restoreBackup() {
        javafx.stage.FileChooser fileChooser = new javafx.stage.FileChooser();
        fileChooser.setTitle("Select Backup File");
        fileChooser.setInitialDirectory(new java.io.File("backups"));
        fileChooser.getExtensionFilters().add(
            new javafx.stage.FileChooser.ExtensionFilter("Backup Files", "*.txt")
        );
        
        java.io.File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            // Create dialog with Append/Overwrite options
            Dialog<String> restoreDialog = new Dialog<>();
            restoreDialog.setTitle("Restore Options");
            restoreDialog.setHeaderText("Choose how to restore the backup");
            
            VBox vbox = new VBox(15);
            vbox.setPadding(new Insets(20));
            
            ToggleGroup group = new ToggleGroup();
            RadioButton overwriteBtn = new RadioButton("Overwrite - Replace all current events");
            RadioButton appendBtn = new RadioButton("Append - Add to current events (merge)");
            overwriteBtn.setToggleGroup(group);
            appendBtn.setToggleGroup(group);
            overwriteBtn.setSelected(true);
            
            vbox.getChildren().addAll(
                new Label("Select restore mode:"),
                overwriteBtn,
                appendBtn
            );
            
            restoreDialog.getDialogPane().setContent(vbox);
            restoreDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            
            restoreDialog.setResultConverter(btn -> {
                if (btn == ButtonType.OK) {
                    return appendBtn.isSelected() ? "APPEND" : "OVERWRITE";
                }
                return null;
            });
            
            java.util.Optional<String> mode = restoreDialog.showAndWait();
            
            if (mode.isPresent()) {
                boolean appendMode = mode.get().equals("APPEND");
                
                try {
                    // Extract sections from backup file
                    java.util.List<String> allLines = java.nio.file.Files.readAllLines(
                        selectedFile.toPath(), java.nio.charset.StandardCharsets.UTF_8);
                    
                    java.util.Map<String, java.util.List<String>> sections = extractSectionsFromBackup(allLines);
                    
                    if (appendMode) {
                        // APPEND MODE: Merge with existing events
                        appendBackupToExisting(sections);
                    } else {
                        // OVERWRITE MODE: Replace all events
                        // Write event.csv
                        java.util.List<String> eventLines = sections.getOrDefault("event.csv", java.util.Collections.emptyList());
                        java.nio.file.Files.write(java.nio.file.Paths.get("event.csv"), eventLines, 
                                                java.nio.charset.StandardCharsets.UTF_8);
                        
                        // Write recurrent.csv
                        java.util.List<String> recurrentLines = sections.getOrDefault("recurrent.csv", java.util.Collections.emptyList());
                        java.nio.file.Files.write(java.nio.file.Paths.get("recurrent.csv"), recurrentLines, 
                                                java.nio.charset.StandardCharsets.UTF_8);
                        
                        // Write additional.csv
                        java.util.List<String> additionalLines = sections.getOrDefault("additional.csv", java.util.Collections.emptyList());
                        if (!additionalLines.isEmpty()) {
                            java.nio.file.Files.write(java.nio.file.Paths.get("additional.csv"), additionalLines, 
                                                    java.nio.charset.StandardCharsets.UTF_8);
                        }
                        
                        // Reload events from restored CSV files
                        manager.getAllEvents().clear();
                        CSVHandlerCompliant.loadEvents(manager);
                    }
                    
                    showAlert(Alert.AlertType.INFORMATION, "Restore Complete", 
                             "Events successfully restored from backup!");
                } catch (Exception ex) {
                    showAlert(Alert.AlertType.ERROR, "Restore Failed", 
                             "Error restoring backup: " + ex.getMessage());
                }
            }
        }
    }

    private java.util.Map<String, java.util.List<String>> extractSectionsFromBackup(java.util.List<String> lines) {
        java.util.Map<String, java.util.List<String>> sections = new java.util.HashMap<>();
        String currentSection = null;
        java.util.List<String> currentLines = new java.util.ArrayList<>();
        
        for (String line : lines) {
            if (line.startsWith("---") && line.endsWith("---")) {
                if (currentSection != null) {
                    sections.put(currentSection, currentLines);
                }
                currentSection = line.replace("---", "").trim();
                currentLines = new java.util.ArrayList<>();
            } else if (currentSection != null && !line.startsWith("#")) {
                currentLines.add(line);
            }
        }
        
        if (currentSection != null) {
            sections.put(currentSection, currentLines);
        }
        
        return sections;
    }

    private void appendBackupToExisting(java.util.Map<String, java.util.List<String>> backupSections) throws java.io.IOException {
        // Load backup data into a temporary EventManager
        EventManager tempManager = new EventManager();
        
        // Write backup to temporary CSV files
        java.nio.file.Path tempEventPath = java.nio.file.Paths.get("temp_event.csv");
        java.nio.file.Path tempRecurrentPath = java.nio.file.Paths.get("temp_recurrent.csv");
        java.nio.file.Path tempAdditionalPath = java.nio.file.Paths.get("temp_additional.csv");
        
        try {
            // Write temporary files
            java.util.List<String> eventLines = backupSections.getOrDefault("event.csv", java.util.Collections.emptyList());
            java.nio.file.Files.write(tempEventPath, eventLines, java.nio.charset.StandardCharsets.UTF_8);
            
            java.util.List<String> recurrentLines = backupSections.getOrDefault("recurrent.csv", java.util.Collections.emptyList());
            java.nio.file.Files.write(tempRecurrentPath, recurrentLines, java.nio.charset.StandardCharsets.UTF_8);
            
            java.util.List<String> additionalLines = backupSections.getOrDefault("additional.csv", java.util.Collections.emptyList());
            if (!additionalLines.isEmpty()) {
                java.nio.file.Files.write(tempAdditionalPath, additionalLines, java.nio.charset.StandardCharsets.UTF_8);
            }
            
            // Load from temporary files using CSVHandlerCompliant logic
            java.io.BufferedReader eventReader = new java.io.BufferedReader(
                new java.io.FileReader(tempEventPath.toFile()));
            eventReader.readLine(); // Skip header
            
            String line;
            while ((line = eventReader.readLine()) != null) {
                String[] parts = line.split(",", 5);
                if (parts.length >= 5) {
                    int eventId = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String description = parts[2];
                    LocalDateTime start = LocalDateTime.parse(parts[3]);
                    LocalDateTime end = LocalDateTime.parse(parts[4]);
                    
                    MainEvent event = new MainEvent(manager.generateEventId(), title, description, start, end);
                    manager.addEvent(event);
                }
            }
            eventReader.close();
            
            showAlert(Alert.AlertType.INFORMATION, "Append Complete", 
                     "Backup events successfully appended to current events!");
            
        } finally {
            // Clean up temporary files
            java.nio.file.Files.deleteIfExists(tempEventPath);
            java.nio.file.Files.deleteIfExists(tempRecurrentPath);
            java.nio.file.Files.deleteIfExists(tempAdditionalPath);
        }
    }

    private void showEventStatisticsDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("📊 Event Statistics");
        dialog.setHeaderText("Event Analytics & Insights");
        
        // Get all expanded events for accurate statistics
        List<MainEvent> allEvents = manager.getAllEventsExpanded();
        
        // Generate statistics report
        String report = EventStatistics.generateStatisticsReport(allEvents);
        
        // Create TextArea to display statistics
        TextArea statsArea = new TextArea(report);
        statsArea.setEditable(false);
        statsArea.setWrapText(true);
        statsArea.setPrefWidth(650);
        statsArea.setPrefHeight(550);
        statsArea.setStyle(
            "-fx-font-family: 'Consolas', 'Courier New', monospace;" +
            "-fx-font-size: 13px;" +
            "-fx-control-inner-background: #1e1e1e;" +
            "-fx-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-highlight-fill: #3d3d3d;" +
            "-fx-highlight-text-fill: " + TEXT_PRIMARY + ";" +
            "-fx-background-color: #1e1e1e;" +
            "-fx-border-color: #3d3d3d;" +
            "-fx-border-width: 1px;" +
            "-fx-border-radius: 5px;" +
            "-fx-background-radius: 5px;"
        );
        
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.getChildren().add(statsArea);
        
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        
        // Style the dialog
        dialog.getDialogPane().setStyle(
            "-fx-background-color: #142238;" +
            "-fx-border-color: #3d3d3d;" +
            "-fx-border-width: 2px;"
        );
        
        dialog.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

