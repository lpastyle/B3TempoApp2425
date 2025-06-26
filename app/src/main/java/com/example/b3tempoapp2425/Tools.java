package com.example.b3tempoapp2425;
import com.example.b3tempoapp2425.model.TempoDaysLeft;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Tools {

    // To prevent object instantiation
    private Tools() {
    }
    /*
     * --- Helpers methods ---
     *
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // return today date
    public static String getNowDate() {
        return formatDate(LocalDate.now());
    }

    // return tomorrow date
    public static String getTomorrowDate() {
        return formatDate(LocalDate.now().plusDays(1));
    }

    // return last year date at the same day as tomorrow
    public static String getLastYearDate() {
        return formatDate(LocalDate.now().minusYears(1).plusDays(1));
    }

    public static String getDaysLeftFromContent(TempoDaysLeft.Content item) {
        try {
            Integer nbDaysLeft = item.nombreJours - item.nombreJoursTires;
            return String.valueOf(nbDaysLeft);
        } catch (NumberFormatException e) {
            return "";
        }
    }

    // Common format method
    private static String formatDate(LocalDate date) {
        return date.format(FORMATTER);
    }

}
