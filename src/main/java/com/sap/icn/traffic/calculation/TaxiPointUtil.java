package com.sap.icn.traffic.calculation;

import com.sap.traffic.foundation.traffic_lib.ITaxiMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public final class TaxiPointUtil {
    private static Logger logger = LoggerFactory.getLogger(TaxiPointUtil.class);
    private static final int TAXI_POINT_FIELD_NUM = 8;
    private DateTimeUtil dateTimeUtil;

    public TaxiPointUtil(String zoneId) {
        dateTimeUtil = DateTimeUtil.of(zoneId);
    }


    public static ITaxiMonitor.TaxiPoint parseTaxiPoint(String record) {
        if (record == null) {
            logger.error("parseTaxiPoint fail, invalid record or delimiter, [{}]", record);
            return null;
        }

        return parseTaxiPoint(record.split(","));
    }

    public static ITaxiMonitor.TaxiPoint parseTaxiPoint(String[] items) {
        if (items == null) {
            logger.error("parseTaxiPoint fail, invalid record or delimiter, [{}], [{}]", items);
            return null;
        }

        if (items.length < TAXI_POINT_FIELD_NUM) {
            logger.error("parseTaxiPoint fail, no enough fields, [delimiter:'{}'][{}]", items);
            return null;
        }

        ITaxiMonitor.TaxiPoint point;

        try {
            // DEVID,LAT,LNG,SPEED,GPS_TIME,HEADING,PASSENGER_STATE,RECEIVE_TIME
            point = new ITaxiMonitor.TaxiPoint();
            point.devId = items[0].trim();
            point.lat = Double.parseDouble(items[1].trim());
            point.lng = Double.parseDouble(items[2].trim());
            point.speed = Float.parseFloat(items[3].trim());
            point.gpsTime = items[4].trim();
            point.heading = Short.parseShort(items[5].trim());
            point.passengerState = Byte.parseByte(items[6].trim());
            point.receiveTime = items[7].trim();
            if (items.length > TAXI_POINT_FIELD_NUM) {
                point.plateColor = Byte.parseByte(items[8].trim());
            } else {
                point.plateColor = 0;
            }
        } catch (NumberFormatException e) {
            point = null;
            logger.error("parseTaxiPoint fail with NumberFormatException, [{}]", items);
        }

        return point;
    }


    public static List<ITaxiMonitor.TaxiPoint> parseTaxiPointList(List<String> records) {
        if (records == null) {
            logger.error("parseTaxiPointerList failed, invalid record or delimiter, [{}]", records);
            return null;
        }

        List<ITaxiMonitor.TaxiPoint> taxiPoints = new ArrayList<>();
        records.forEach(record -> {
            if (record != null) {
                ITaxiMonitor.TaxiPoint point = parseTaxiPoint(record);
                if (point != null) {
                    taxiPoints.add(point);
                }
            }
        });

        return taxiPoints;
    }


    public static String formatEdgeSpeed(long timestamp, ITaxiMonitor.EdgeSpeed t, String delimiter) {
        if (t == null || delimiter == null) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        s.append(t.edgeId).append(delimiter);
        s.append(timestamp).append(delimiter);
        s.append(t.speed).append(delimiter);
        s.append(t.flowCount).append(delimiter);
        s.append(t.vkt);
        return s.toString();
    }

    public static String formatTaxiPoint(ITaxiMonitor.TaxiPoint t, String delimiter) {
        if (t == null || delimiter == null) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        s.append(t.devId).append(delimiter);
        s.append(t.lng).append(delimiter);
        s.append(t.lat).append(delimiter);
        s.append(t.gpsTime).append(delimiter);
        s.append(t.receiveTime).append(delimiter);
        s.append(t.speed).append(delimiter);
        s.append(t.heading).append(delimiter);
        s.append(t.plateColor).append(delimiter);
        return s.toString();
    }

    public String formatSlotStats(ITaxiMonitor.TaxiSlotStats record, String delimiter) {
        if (record == null || delimiter == null) {
            return "";
        }

        long timestamp = 0;
        try {
            timestamp = dateTimeUtil.parseToMillSecond(record.timestamp);
        } catch (DateTimeParseException e) {
            return "";
        }

        StringBuilder s = new StringBuilder();
        s.append(timestamp).append(delimiter);
        s.append(record.devCount == -1 ? "" : record.devCount).append(delimiter);
        s.append(record.recordsCount == -1 ? "" : record.recordsCount).append(delimiter);
        s.append(record.taxiDevCount == -1 ? "" : record.taxiDevCount).append(delimiter);
        s.append(record.taxiRecordsCount == -1 ? "" : record.taxiRecordsCount).append(delimiter);
        s.append(record.loadedTaxiDevCount == -1 ? "" : record.loadedTaxiDevCount).append(delimiter);
        s.append(record.loadedTaxiRecordsCount == -1 ? "" : record.loadedTaxiRecordsCount).append(delimiter);

        s.append(record.dupCount == -1 ? "" : record.dupCount).append(delimiter);
        s.append(record.dupRatio < 0 ? "" : record.dupRatio).append(delimiter);
        s.append(record.invalidCount == -1 ? "" : record.invalidCount).append(delimiter);
        s.append(record.invalidRatio < 0 ? "" : record.invalidRatio).append(delimiter);

        s.append(record.avgSpeed).append(delimiter);
        s.append(record.avgNon0Speed).append(delimiter);
        s.append(record.zeroSpeedRatio).append(delimiter);
        s.append(record.avgInsertDelay).append(delimiter);
        s.append(record.avgInterval);

        return s.toString();
    }

    public long getTaxiPointReceiveTime(ITaxiMonitor.TaxiPoint point) {
        Objects.requireNonNull(point);

        try {
            return dateTimeUtil.parseToMillSecond(point.receiveTime);
        } catch (DateTimeParseException e) {
            return -1;
        }
    }

    public long getTaxiPointSlotTime(ITaxiMonitor.TaxiPoint point, long slotSize) {
        long receiveTime = getTaxiPointReceiveTime(point);
        return receiveTime > 0 ? DateTimeUtil.ceil(receiveTime, slotSize) : -1;
    }
}
