package ru.kestar.alisabot.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    private DateUtils() {}
    
    private static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy");

    public static String formatDateTime(LocalDateTime dateTime) {
        return DATE_TIME_FORMAT.format(dateTime);
    }
}
