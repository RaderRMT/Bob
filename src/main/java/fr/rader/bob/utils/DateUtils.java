package fr.rader.bob.utils;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.TimeZone;

public class DateUtils {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat();

    /**
     * Format a Date to the given {@code pattern}
     *
     * @param pattern Date pattern
     * @return The formatted Date
     */
    public static String getFormattedDate(String pattern) {
        if (!dateFormat.toPattern().equals(pattern)) {
            dateFormat.applyPattern(pattern);
        }

        return dateFormat.format(Date.from(Instant.now()));
    }

    static {
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }
}
