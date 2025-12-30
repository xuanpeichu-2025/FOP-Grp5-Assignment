# Calendar Application - Feature Implementation Guide

## 1. Reminders/Notifications Feature (1 mark)

### Description
The application now displays event reminders and notifications, with the ability to set custom reminder times before events.

### Features Implemented:

#### A. On-Program Launch Notifications
- When the application starts, it automatically checks for upcoming events
- Displays notifications for events happening within 24 hours
- Shows custom reminder messages like: "Your next event is coming soon in 2 hours"

#### B. Custom Reminder Settings
Users can set reminders for specific events with these options:
- 15 minutes before
- 30 minutes before  
- 1 hour before
- 2 hours before
- 1 day before
- Custom minutes (user-defined)

#### C. Reminder Management
- Set reminders for existing events (Event Management → Update Event or Manage Reminders)
- View all event reminders
- Remove reminders from events
- Check reminders on-demand

### Usage (Console Version):
1. From Main Menu, select **"4. Manage Reminders"**
2. Choose from:
   - Set Reminder for Event
   - View Event Reminders
   - Remove Reminder from Event
   - Check Reminders Now

### Data Persistence
- Reminders are saved to `events.csv` file
- Format: Event data + reminder minutes before event
- Automatically loaded when application starts

### How It Works:
```
NotificationManager.checkAndDisplayNotifications(manager)
- Checks all events in the EventManager
- Calculates time until each event starts
- Displays notification if event is within reminder time
- Shows user-friendly duration format (e.g., "2 hours", "30 minutes")
```

---

## 2. Graphical User Interface (JavaFX) (1 mark)

### Description
A complete JavaFX-based GUI providing an intuitive graphical interface for all calendar operations.

### GUI Components:

#### Main Menu Window
- Clean, centered layout with 5 main buttons
- Each button navigates to respective feature
- "Save & Exit" button with confirmation

#### Event Management Panel
- Add Event (normal single event)
- Add Recurring Event (with frequency options)
- View All Events (table view with all details)
- Update Event (modify existing events)
- Delete Event (remove events by ID)

#### Calendar View Panel
- Daily View (select date, see all events for that day)
- Monthly List View (select year/month, list all events)
- Month/Year picker with spinners

#### Search Event Panel
- Search by Date (single day events)
- Search by Date Range (events within a time period)
- Search by Event Title (partial matching)
- Results displayed in text areas

#### Manage Reminders Panel
- Set Reminder (with preset or custom options)
- View Event Reminders (table of all reminders)
- Remove Reminder (delete from specific event)
- Check Reminders Now (manual notification check)

### Technical Details:

#### JavaFX Libraries Used:
- `javafx.application.Application` - Base application class
- `javafx.scene.Scene` - Scene container
- `javafx.scene.control.*` - UI controls (buttons, text fields, etc.)
- `javafx.scene.layout.*` - Layout managers (VBox, GridPane, etc.)
- `javafx.geometry.Insets` - Spacing and padding

#### Maven Configuration:
The `pom.xml` includes:
```xml
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.2</version>
</dependency>
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-fxml</artifactId>
    <version>21.0.2</version>
</dependency>
```

#### Dialog-Based Navigation:
- Uses `Dialog<T>` for modal windows
- `DatePicker` for date selection
- `TableView` for displaying events
- `TextArea` for results display
- `ComboBox` for selection options
- `Spinner` for numeric input

### Running the GUI:

#### Method 1: Direct Java Execution
```bash
cd CalendarApp\src\main\java
javac -encoding UTF-8 com/mycompany/calendarapp/*.java
java --add-modules javafx.controls,javafx.fxml com.mycompany.calendarapp.CalendarAppGUI
```

#### Method 2: Using Batch File (Windows)
```bash
run-gui.bat
```

#### Method 3: Maven (if configured)
```bash
mvn javafx:run
```

### GUI Features:

1. **Window Management**
   - Main window (900x700) with resizable design
   - Modal dialogs for specific operations
   - Back buttons to navigate menus

2. **Input Validation**
   - Date/time format validation
   - Event ID validation
   - Error alerts with user-friendly messages

3. **Data Display**
   - TableView for structured event data
   - TextArea for search results
   - Real-time event listing

4. **User Experience**
   - Color-coded buttons (red for exit, green for back)
   - Clear button labels and instructions
   - Consistent padding and spacing (Insets 20-30)
   - Separated UI concerns with VBox layouts

---

## 3. Data Persistence

### CSV File Format:
```
ID,Type,Title,Description,StartDateTime,EndDateTime,[RecurrenceType,Occurrences],ReminderMinutes
1001,NORMAL,Meeting,Important discussion,2026-01-12 14:30,2026-01-12 15:30,,30
1002,RECURRING,Team Standup,Daily standup,2026-01-13 09:00,2026-01-13 09:30,DAILY,5,15
```

### Automatic Features:
- Save on Exit
- Load on Startup
- Reminder settings preserved
- Event types recognized correctly

---

## 4. Integration

All features seamlessly integrate:
- **Reminders** work with all event types (normal and recurring)
- **GUI** manages reminders through dedicated panel
- **Notifications** display on launch and on-demand
- **Data** persists across sessions

---

## 5. Files Created/Modified

### New Files:
- `CalendarAppGUI.java` - Main JavaFX application
- `Reminder.java` - Reminder model class
- `NotificationManager.java` - Notification logic
- `run-gui.bat` - Windows launcher script
- `.gitignore` - Git configuration

### Modified Files:
- `MainEvent.java` - Added reminder field and getter/setter
- `MainMenu.java` - Added reminder management menu
- `CSVHandler.java` - Updated to save/load reminders
- `pom.xml` - Added JavaFX dependencies and plugins

---

## 6. Future Enhancements

Possible improvements for future versions:
- Alarm sound notifications
- Email reminders
- Calendar exports (iCal format)
- Dark theme support
- Event categories/color coding
- Multi-user support
- Cloud synchronization

---

## Notes

- The console version (MainMenu.java) remains functional for backward compatibility
- The GUI version (CalendarAppGUI.java) is the recommended interface
- All business logic remains in separate classes (EventManager, CSVHandler, etc.)
- Clean separation of concerns: UI layer doesn't affect data layer

