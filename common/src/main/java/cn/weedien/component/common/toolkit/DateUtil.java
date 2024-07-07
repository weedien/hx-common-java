package cn.weedien.component.common.toolkit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.*;
import java.time.format.DateTimeFormatter;

// TODO
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DateUtil {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // 使用 ThreadLocal 来确保线程安全，同时避免频繁创建对象
    private static final ThreadLocal<Clock> CLOCK_THREAD_LOCAL = ThreadLocal.withInitial(Clock::systemDefaultZone);

    // 日期相关方法
    public static String formatDate(LocalDate date) {
        return date.format(DATE_FORMATTER);
    }

    public static String formatTime(LocalTime time) {
        return time.format(TIME_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(DATE_TIME_FORMATTER);
    }

    public static LocalDate parseDate(String dateStr) {
        return LocalDate.parse(dateStr, DATE_FORMATTER);
    }

    public static LocalTime parseTime(String timeStr) {
        return LocalTime.parse(timeStr, TIME_FORMATTER);
    }

    public static LocalDateTime parseDateTime(String dateTimeStr) {
        return LocalDateTime.parse(dateTimeStr, DATE_TIME_FORMATTER);
    }

    public static LocalDate getCurrentDate() {
        return LocalDate.now(CLOCK_THREAD_LOCAL.get());
    }

    public static LocalTime getCurrentTime() {
        return LocalTime.now(CLOCK_THREAD_LOCAL.get());
    }

    public static LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now(CLOCK_THREAD_LOCAL.get());
    }

    public static String formatNanos(long timestamp) {
        Instant instant = Instant.ofEpochSecond(0, timestamp);
        return TIMESTAMP_FORMATTER.format(instant.atZone(ZoneId.systemDefault()));
    }

    public static String formatDuration(long duration) {
        if (duration < 1000) {
            return String.format("%d ns", duration);
        } else if (duration < 1_000_000) {
            long microSeconds = duration / 1000;
            return String.format("%d µs", microSeconds);
        } else {
            long milliSeconds = duration / 1_000_000;
            return String.format("%d ms", milliSeconds);
        }
    }

}
