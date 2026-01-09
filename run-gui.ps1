$pathToFx = "C:\Users\Win11\FOP G5 Assignment\FOP-Grp5-Assignment\libs\javafx-sdk-17.0.2\lib"
Set-Location "$PSScriptRoot\CalendarApp\src\main\java"
Write-Host "Compiling Java files..." -ForegroundColor Yellow
javac -encoding UTF-8 --module-path "$pathToFx" --add-modules javafx.controls,javafx.fxml com/mycompany/calendarapp/*.java
if ($LASTEXITCODE -eq 0) {
    Write-Host "Starting Calendar GUI Application..." -ForegroundColor Green
    java --module-path "$pathToFx" --add-modules javafx.controls,javafx.fxml com.mycompany.calendarapp.CalendarAppGUI
} else {
    Write-Host "Compilation failed!" -ForegroundColor Red
}
