package com.mycompany.calendarapp;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CalendarAppGUI extends Application {
    private EventManager manager;
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private CalendarView calendarView;
    private SearchEvent searchEvent;

    @Override
    public void start(Stage primaryStage) {
        manager = new EventManager();
        CSVHandler.loadEvents(manager);
        calendarView = new CalendarView();
        searchEvent = new SearchEvent();

        // Show notifications on startup
        NotificationManager.checkAndDisplayNotifications(manager);

        primaryStage.setTitle("Calendar Application");
        primaryStage.setWidth(900);
        primaryStage.setHeight(700);

        // Create main menu
        VBox mainMenuPane = createMainMenu(primaryStage);
        Scene scene = new Scene(mainMenuPane);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createMainMenu(Stage stage) {
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(30));
        vbox.setStyle("-fx-font-size: 14;");

        Label title = new Label("CALENDAR APPLICATION");
        title.setStyle("-fx-font-size: 28; -fx-font-weight: bold;");

        Button btnEventManagement = new Button("1. Event Management");
        btnEventManagement.setPrefWidth(250);
        btnEventManagement.setPrefHeight(50);
        btnEventManagement.setOnAction(e -> showEventManagementMenu(stage));

        Button btnCalendarView = new Button("2. Calendar View");
        btnCalendarView.setPrefWidth(250);
        btnCalendarView.setPrefHeight(50);
        btnCalendarView.setOnAction(e -> showCalendarViewMenu(stage));

        Button btnSearchEvent = new Button("3. Search Event");
        btnSearchEvent.setPrefWidth(250);
        btnSearchEvent.setPrefHeight(50);
        btnSearchEvent.setOnAction(e -> showSearchEventMenu(stage));

        Button btnManageReminders = new Button("4. Manage Reminders");
        btnManageReminders.setPrefWidth(250);
        btnManageReminders.setPrefHeight(50);
        btnManageReminders.setOnAction(e -> showManageRemindersMenu(stage));

        Button btnExit = new Button("5. Save & Exit");
        btnExit.setPrefWidth(250);
        btnExit.setPrefHeight(50);
        btnExit.setStyle("-fx-text-fill: white; -fx-background-color: #d9534f;");
        btnExit.setOnAction(e -> {
            CSVHandler.saveEvents(manager);
            stage.close();
        });

        VBox buttonBox = new VBox(15);
        buttonBox.setAlignment(javafx.geometry.Pos.CENTER);
        buttonBox.getChildren().addAll(btnEventManagement, btnCalendarView, btnSearchEvent, 
                                       btnManageReminders, btnExit);

        vbox.getChildren().addAll(title, new Separator(), buttonBox);
        return vbox;
    }

    private void showEventManagementMenu(Stage stage) {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        Label title = new Label("EVENT MANAGEMENT");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Button btnAddEvent = new Button("Add Event");
        btnAddEvent.setPrefWidth(200);
        btnAddEvent.setOnAction(e -> showAddEventDialog());

        Button btnAddRecurring = new Button("Add Recurring Event");
        btnAddRecurring.setPrefWidth(200);
        btnAddRecurring.setOnAction(e -> showAddRecurringEventDialog());

        Button btnViewAllEvents = new Button("View All Events");
        btnViewAllEvents.setPrefWidth(200);
        btnViewAllEvents.setOnAction(e -> showAllEventsDialog());

        Button btnUpdateEvent = new Button("Update Event");
        btnUpdateEvent.setPrefWidth(200);
        btnUpdateEvent.setOnAction(e -> showUpdateEventDialog());

        Button btnDeleteEvent = new Button("Delete Event");
        btnDeleteEvent.setPrefWidth(200);
        btnDeleteEvent.setOnAction(e -> showDeleteEventDialog());

        Button btnBack = new Button("Back to Main Menu");
        btnBack.setPrefWidth(200);
        btnBack.setStyle("-fx-text-fill: white; -fx-background-color: #5cb85c;");
        btnBack.setOnAction(e -> {
            ((Stage) vbox.getScene().getWindow()).setScene(
                new Scene(createMainMenu(stage))
            );
        });

        vbox.getChildren().addAll(title, new Separator(), btnAddEvent, btnAddRecurring, 
                                  btnViewAllEvents, btnUpdateEvent, btnDeleteEvent, btnBack);
        
        Scene scene = new Scene(scrollPane, 400, 500);
        stage.setScene(scene);
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
        TextField startField = new TextField();
        startField.setPromptText("Start (yyyy-MM-dd HH:mm)");
        TextField endField = new TextField();
        endField.setPromptText("End (yyyy-MM-dd HH:mm)");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Start DateTime:"), 0, 2);
        grid.add(startField, 1, 2);
        grid.add(new Label("End DateTime:"), 0, 3);
        grid.add(endField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String title = titleField.getText();
                    String description = descField.getText();
                    LocalDateTime start = LocalDateTime.parse(startField.getText(), dateTimeFormatter);
                    LocalDateTime end = LocalDateTime.parse(endField.getText(), dateTimeFormatter);

                    MainEvent event = new MainEvent(manager.generateEventId(), title, description, start, end);
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
        TextField startField = new TextField();
        startField.setPromptText("Start (yyyy-MM-dd HH:mm)");
        TextField endField = new TextField();
        endField.setPromptText("End (yyyy-MM-dd HH:mm)");
        ComboBox<String> recurrenceBox = new ComboBox<>();
        recurrenceBox.getItems().addAll("DAILY", "WEEKLY", "MONTHLY");
        TextField occurrencesField = new TextField();
        occurrencesField.setPromptText("Number of occurrences");

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Start DateTime:"), 0, 2);
        grid.add(startField, 1, 2);
        grid.add(new Label("End DateTime:"), 0, 3);
        grid.add(endField, 1, 3);
        grid.add(new Label("Recurrence Type:"), 0, 4);
        grid.add(recurrenceBox, 1, 4);
        grid.add(new Label("Occurrences:"), 0, 5);
        grid.add(occurrencesField, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    String title = titleField.getText();
                    String description = descField.getText();
                    LocalDateTime start = LocalDateTime.parse(startField.getText(), dateTimeFormatter);
                    LocalDateTime end = LocalDateTime.parse(endField.getText(), dateTimeFormatter);
                    String recurrence = recurrenceBox.getValue();
                    int occurrences = Integer.parseInt(occurrencesField.getText());

                    RecurringEvent event = new RecurringEvent(manager.generateEventId(), title, description, start, end, recurrence, occurrences);
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

        table.getColumns().addAll(idCol, titleCol, descCol, startCol, reminderCol);
        table.setItems(javafx.collections.FXCollections.observableArrayList(manager.getAllEvents()));
        table.setPrefHeight(400);

        content.getChildren().addAll(table);
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showUpdateEventDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Update Event");
        dialog.setHeaderText("Enter Event ID to Update");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("Event ID");

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(idField, 1, 0);

        dialog.getDialogPane().setContent(grid);
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
        TextField startField = new TextField(event.getStartDateTime().format(dateTimeFormatter));
        TextField endField = new TextField(event.getEndDateTime().format(dateTimeFormatter));

        grid.add(new Label("Title:"), 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(new Label("Description:"), 0, 1);
        grid.add(descField, 1, 1);
        grid.add(new Label("Start DateTime:"), 0, 2);
        grid.add(startField, 1, 2);
        grid.add(new Label("End DateTime:"), 0, 3);
        grid.add(endField, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    event.setTitle(titleField.getText());
                    event.setDescription(descField.getText());
                    event.setStartDateTime(LocalDateTime.parse(startField.getText(), dateTimeFormatter));
                    event.setEndDateTime(LocalDateTime.parse(endField.getText(), dateTimeFormatter));

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
        dialog.setHeaderText("Enter Event ID to Delete");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("Event ID");

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(idField, 1, 0);

        dialog.getDialogPane().setContent(grid);
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

    private void showCalendarViewMenu(Stage stage) {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label title = new Label("CALENDAR VIEW");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Button btnDailyView = new Button("Daily View");
        btnDailyView.setPrefWidth(200);
        btnDailyView.setOnAction(e -> showDailyViewDialog());

        Button btnMonthlyView = new Button("Monthly List View");
        btnMonthlyView.setPrefWidth(200);
        btnMonthlyView.setOnAction(e -> showMonthlyViewDialog());

        Button btnBack = new Button("Back to Main Menu");
        btnBack.setPrefWidth(200);
        btnBack.setStyle("-fx-text-fill: white; -fx-background-color: #5cb85c;");
        btnBack.setOnAction(e -> {
            stage.setScene(new Scene(createMainMenu(stage)));
        });

        vbox.getChildren().addAll(title, new Separator(), btnDailyView, btnMonthlyView, btnBack);
        
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
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

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefHeight(400);

        StringBuilder sb = new StringBuilder();
        boolean hasEvents = false;
        for (MainEvent event : manager.getAllEvents()) {
            if (event.getStartDateTime().toLocalDate().equals(date)) {
                hasEvents = true;
                sb.append("ID: ").append(event.getEventId()).append("\n");
                sb.append("Title: ").append(event.getTitle()).append("\n");
                sb.append("Description: ").append(event.getDescription()).append("\n");
                sb.append("Start: ").append(event.getStartDateTime().format(dateTimeFormatter)).append("\n");
                sb.append("End: ").append(event.getEndDateTime().format(dateTimeFormatter)).append("\n");
                sb.append("---\n");
            }
        }

        if (!hasEvents) {
            sb.append("No events on this date.");
        }

        textArea.setText(sb.toString());
        dialog.getDialogPane().setContent(textArea);
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
        dialog.setTitle("Monthly View - " + year + "-" + month);
        dialog.setHeaderText("Events for " + year + "-" + String.format("%02d", month));

        TextArea textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setPrefHeight(400);

        StringBuilder sb = new StringBuilder();
        boolean hasEvents = false;
        for (MainEvent event : manager.getAllEvents()) {
            if (event.getStartDateTime().getYear() == year && event.getStartDateTime().getMonthValue() == month) {
                hasEvents = true;
                sb.append("ID: ").append(event.getEventId()).append("\n");
                sb.append("Title: ").append(event.getTitle()).append("\n");
                sb.append("Date: ").append(event.getStartDateTime().format(dateTimeFormatter)).append("\n");
                sb.append("---\n");
            }
        }

        if (!hasEvents) {
            sb.append("No events this month.");
        }

        textArea.setText(sb.toString());
        dialog.getDialogPane().setContent(textArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    private void showSearchEventMenu(Stage stage) {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label title = new Label("SEARCH EVENT");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Button btnSearchByDate = new Button("Search by Date");
        btnSearchByDate.setPrefWidth(200);
        btnSearchByDate.setOnAction(e -> showSearchByDateDialog());

        Button btnSearchByDateRange = new Button("Search by Date Range");
        btnSearchByDateRange.setPrefWidth(200);
        btnSearchByDateRange.setOnAction(e -> showSearchByDateRangeDialog());

        Button btnSearchByTitle = new Button("Search by Event Title");
        btnSearchByTitle.setPrefWidth(200);
        btnSearchByTitle.setOnAction(e -> showSearchByTitleDialog());

        Button btnBack = new Button("Back to Main Menu");
        btnBack.setPrefWidth(200);
        btnBack.setStyle("-fx-text-fill: white; -fx-background-color: #5cb85c;");
        btnBack.setOnAction(e -> {
            stage.setScene(new Scene(createMainMenu(stage)));
        });

        vbox.getChildren().addAll(title, new Separator(), btnSearchByDate, btnSearchByDateRange, 
                                  btnSearchByTitle, btnBack);
        
        Scene scene = new Scene(vbox, 400, 400);
        stage.setScene(scene);
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
        for (MainEvent event : manager.getAllEvents()) {
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
        for (MainEvent event : manager.getAllEvents()) {
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
        for (MainEvent event : manager.getAllEvents()) {
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

    private void showManageRemindersMenu(Stage stage) {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label title = new Label("MANAGE REMINDERS");
        title.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

        Button btnSetReminder = new Button("Set Reminder for Event");
        btnSetReminder.setPrefWidth(220);
        btnSetReminder.setOnAction(e -> showSetReminderDialog());

        Button btnViewReminders = new Button("View Event Reminders");
        btnViewReminders.setPrefWidth(220);
        btnViewReminders.setOnAction(e -> showViewRemindersDialog());

        Button btnRemoveReminder = new Button("Remove Reminder");
        btnRemoveReminder.setPrefWidth(220);
        btnRemoveReminder.setOnAction(e -> showRemoveReminderDialog());

        Button btnCheckReminders = new Button("Check Reminders Now");
        btnCheckReminders.setPrefWidth(220);
        btnCheckReminders.setOnAction(e -> {
            NotificationManager.checkAndDisplayNotifications(manager);
            showAlert(Alert.AlertType.INFORMATION, "Reminders", "Reminders checked!");
        });

        Button btnBack = new Button("Back to Main Menu");
        btnBack.setPrefWidth(220);
        btnBack.setStyle("-fx-text-fill: white; -fx-background-color: #5cb85c;");
        btnBack.setOnAction(e -> {
            stage.setScene(new Scene(createMainMenu(stage)));
        });

        vbox.getChildren().addAll(title, new Separator(), btnSetReminder, btnViewReminders, 
                                  btnRemoveReminder, btnCheckReminders, btnBack);
        
        Scene scene = new Scene(vbox, 450, 450);
        stage.setScene(scene);
    }

    private void showSetReminderDialog() {
        Dialog<Boolean> dialog = new Dialog<>();
        dialog.setTitle("Set Reminder");
        dialog.setHeaderText("Set Reminder for Event");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("Event ID");
        ComboBox<String> reminderBox = new ComboBox<>();
        reminderBox.getItems().addAll("15 minutes before", "30 minutes before", "1 hour before", 
                                      "2 hours before", "1 day before", "Custom minutes");

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Reminder Time:"), 0, 1);
        grid.add(reminderBox, 1, 1);

        dialog.getDialogPane().setContent(grid);
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
        for (MainEvent event : manager.getAllEvents()) {
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

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        TextField idField = new TextField();
        idField.setPromptText("Event ID");

        grid.add(new Label("Event ID:"), 0, 0);
        grid.add(idField, 1, 0);

        dialog.getDialogPane().setContent(grid);
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

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
