package com.example.b3tempoapp2425;
import com.example.b3tempoapp2425.model.TempoDaysLeft;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Tools {
    // host the notification id generator
    private static AtomicInteger atomicInteger = null;
    private static final int INITIAL_GENERATOR_VALUE = 1970;

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

    /**
     * Format date sent by EDF API into something more pretty
     * @param apiDate : the tempo date string as sent by the EDF API
     * @return a pretty formatted date in the style of "Mon. 19 Dec. 2022"
     */
    public  static String formatTempoHistoryDate(String apiDate) {
        try {
            String[] ymdArray = apiDate.split("-");
            Date date = new Date(Integer.parseInt(ymdArray[0])-1900, Integer.parseInt(ymdArray[1])-1, Integer.parseInt(ymdArray[2]));
            SimpleDateFormat sdf = new SimpleDateFormat("E d MMM yyyy", Locale.FRANCE);
            return sdf.format(date);
        } catch (NumberFormatException e) {
            return "?";
        }
    }

    public static int getNextNotifId() {
        if (atomicInteger == null) {
            atomicInteger = new AtomicInteger(INITIAL_GENERATOR_VALUE);
            return atomicInteger.get();
        } else {
            return atomicInteger.incrementAndGet();
        }
    }


    // Common format method
    private static String formatDate(LocalDate date) {
        return date.format(FORMATTER);
    }

}
