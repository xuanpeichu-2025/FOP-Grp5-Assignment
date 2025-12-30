@echo off
REM JavaFX Calendar Application Launcher
REM This script runs the Calendar Application with JavaFX

setlocal enabledelayedexpansion

REM Get the directory where this script is located
set SCRIPT_DIR=%~dp0

REM Set up the classpath - adjust these paths based on your JavaFX installation
REM If JavaFX is installed via Maven, it will be in your .m2 directory
for /r "%USERPROFILE%\.m2\repository\org\openjfx" %%f in (javafx-*.jar) do (
    set "CLASSPATH=!CLASSPATH!;%%f"
)

REM Add the current directory and compiled classes
set "CLASSPATH=!CLASSPATH!;%SCRIPT_DIR%CalendarApp\src\main\java"
set "CLASSPATH=!CLASSPATH!;%SCRIPT_DIR%CalendarApp\target\classes"

REM Try to run with JavaFX modules if available
java --add-modules javafx.controls,javafx.fxml -cp "%CLASSPATH%" com.mycompany.calendarapp.CalendarAppGUI %*

REM If that fails, try running with classpath only
if errorlevel 1 (
    echo Attempting fallback method...
    java -cp "%CLASSPATH%" com.mycompany.calendarapp.CalendarAppGUI %*
)
