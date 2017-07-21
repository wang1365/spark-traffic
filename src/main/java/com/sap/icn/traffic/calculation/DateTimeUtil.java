package com.sap.icn.traffic.calculation;


import java.time.*;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {
    public enum TimeType {
        LOCAL(0), UTC(1);
        private int type;

        TimeType(int type) {
            this.type = type;
        }

        public int value() {
            return this.type;
        }
    }

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private ZoneId zoneId;

    private DateTimeUtil() {
        this.zoneId = ZoneOffset.UTC;
    }

    private DateTimeUtil(String zoneId) {
        this.zoneId = ZoneId.of(zoneId);
    }

    public static DateTimeUtil of(String zoneId) {
        return new DateTimeUtil(zoneId);
    }

    public static long parseToMillSecond(String timestamp, String zoneId) {
        return parseToSecond(timestamp, zoneId) * 1000L;
    }

    public static long parseToSecond(String timestamp, String zoneId) {
        return ZonedDateTime.of(LocalDateTime.parse(timestamp, FORMATTER), ZoneId.of(zoneId)).toEpochSecond();
    }

    public static long ceil(long timeMillis, long windowMillis) {
        return timeMillis % windowMillis == 0L ? timeMillis : timeMillis - timeMillis % windowMillis + windowMillis;
    }

    public long parseToMillSecond(String timestamp) {
        return parseToSecond(timestamp) * 1000L;
    }

    public long parseToSecond(String timestamp) {
        return ZonedDateTime.of(LocalDateTime.parse(timestamp, FORMATTER), this.zoneId).toEpochSecond();
    }

    public String format(long millis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), this.zoneId).format(FORMATTER);
    }

    public static String formatToUTC(long millis) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneOffset.UTC).format(FORMATTER);
    }

    public ZoneId getZoneId() {
        return zoneId;
    }
}
