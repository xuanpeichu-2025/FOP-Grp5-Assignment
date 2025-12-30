package com.mycompany.calendarapp;

import java.time.LocalDate;

/**
 * Recurrent event data class for CSV compatibility
 * This matches the recurrent.csv format: eventId, recurrentInterval, recurrentTimes, recurrentEndDate
 */
public class RecurrentEventData {
    private int eventId;
    private String recurrentInterval;  // e.g., "1d", "1w", "1m"
    private int recurrentTimes;        // number of times to recur (0 if using endDate)
    private String recurrentEndDate;   // end date (0 or empty if using times)

    public RecurrentEventData(int eventId, String recurrentInterval, int recurrentTimes, String recurrentEndDate) {
        this.eventId = eventId;
        this.recurrentInterval = recurrentInterval;
        this.recurrentTimes = recurrentTimes;
        this.recurrentEndDate = recurrentEndDate;
    }

    public int getEventId() { return eventId; }
    public String getRecurrentInterval() { return recurrentInterval; }
    public int getRecurrentTimes() { return recurrentTimes; }
    public String getRecurrentEndDate() { return recurrentEndDate; }

    public void setEventId(int eventId) { this.eventId = eventId; }
    public void setRecurrentInterval(String recurrentInterval) { this.recurrentInterval = recurrentInterval; }
    public void setRecurrentTimes(int recurrentTimes) { this.recurrentTimes = recurrentTimes; }
    public void setRecurrentEndDate(String recurrentEndDate) { this.recurrentEndDate = recurrentEndDate; }

    /**
     * Convert to CSV line for recurrent.csv
     * Format: eventId, recurrentInterval, recurrentTimes, recurrentEndDate
     */
    public String toCSVLine() {
        return eventId + "," + recurrentInterval + "," + recurrentTimes + "," + recurrentEndDate;
    }

    @Override
    public String toString() {
        return "RecurrentEventData{" +
                "eventId=" + eventId +
                ", recurrentInterval='" + recurrentInterval + '\'' +
                ", recurrentTimes=" + recurrentTimes +
                ", recurrentEndDate='" + recurrentEndDate + '\'' +
                '}';
    }
}
