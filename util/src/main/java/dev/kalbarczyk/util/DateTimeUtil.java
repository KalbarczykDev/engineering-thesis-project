package dev.kalbarczyk.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateTimeUtil {

    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_DATE_TIME;

    private DateTimeUtil() {
    }

    public static String toString(final LocalDateTime time) {
        return time == null ? null : time.format(ISO);
    }

    public static LocalDateTime toLocalDateTime(final String time) {
        return time == null ? null : LocalDateTime.parse(time, ISO);
    }
}