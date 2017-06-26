package com.sap.icn.traffic;

import com.sap.traffic.foundation.traffic_lib.ITaxiMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Objects;

public class SpeedConfiguration implements Cloneable, Serializable {
    private static final Logger logger = LoggerFactory.getLogger(SpeedConfiguration.class);
    private static final Double MAX_DOUBLE_DEVIATION = 1e-8;
    private static final String SEGMENTS_CSV = "/sap-traffic-foundation/apps/customer-apps/itm/data/sync_segment.csv";
    private static final String SEG_EDGES_CSV = "/sap-traffic-foundation/apps/customer-apps/itm/data/sync_edge_segment.csv";
    private static final String EX_ROUTES_CSV = "/sap-traffic-foundation/apps/customer-apps/itm/data/sync_exclude_route.csv";

    private static SpeedConfiguration singleton = new SpeedConfiguration();
    private ITaxiMonitor.TaxiMonitorSlotsParams params = new ITaxiMonitor.TaxiMonitorSlotsParams();
    private String segmentsCsv = SEGMENTS_CSV;
    private String segEdgesCsv = SEG_EDGES_CSV;
    private String exRoutesCsv = EX_ROUTES_CSV;
    private String zoneId = "UTC+0";

    public static final String HANA_CFG_CALC_WINDOW = "speed:calculate:time_window";
    public static final String HANA_CFG_CALC_WINDOW_FOR_SPEED = "speed:calculate:time_window_for_speed";
    public static final String HANA_CFG_CALC_PASSENGER_STATE_FILER = "speed:calculate:passenger_state_filter";
    public static final String HANA_CFG_CALC_MAX_TIME_INTERVAL = "speed:calculate:max_time_interval";
    public static final String HANA_CFG_CALC_MIN_DIST_INTERVAL = "speed:calculate:min_dist_interval";
    public static final String HANA_CFG_CALC_MAX_DIST_INTERVAL = "speed:calculate:max_dist_interval";
    public static final String HANA_CFG_CALC_OUTPUT_SEGMENT_SPEED = "speed:calculate:output_segment_speed";
    public static final String HANA_CFG_IMPUTATION_MODE = "speed:imputation_mode";
    public static final String HANA_CFG_PLATE_COLOR_EFFECTIVE = "speed:plate_color_effective";
    public static final String HANA_CFG_PLATE_COLOR = "speed:taxi:plate_color";
    public static final String HANA_CFG_PLATE_REGEX = "speed:taxi:plate_regex";
    public static final String HANA_CFG_TIME_POINT_TYPE = "speed:time_point_type";

    public SpeedConfiguration() {
        params.minLat = 31.23;
        params.minLng = 118.37;
        params.maxLat = 32.62;
        params.maxLng = 119.23;

        params.outputSegmentSpeed = true;
        params.calcEdgeSpeed = true;
        params.timePointType = 1; // 0: local time
        params.timePointFrom = "";
        params.timePointTo = "";
        params.timeWindow = 300;
        params.timeWindowForSpeed = 900;
        params.calcErrFlags = false;

        params.maxTimeInterval = 180;
        params.minDistInterval = 10;
        params.maxDistInterval = 3800;

        params.passengerStateFilter = 1; // 0: empty, 1: loaded, 2: both
        params.plateColorEffective = false;
        params.taxiPlateColor = 1;
        params.taxiPlateRegex = "";

        params.enableDbgLog = false;

        // GIS csv
        segmentsCsv = SEGMENTS_CSV;
        segEdgesCsv = SEG_EDGES_CSV;
        exRoutesCsv = EX_ROUTES_CSV;
    }


    public static SpeedConfiguration getInstance() {
        return singleton;
    }

    public ITaxiMonitor.TaxiMonitorSlotsParams getParams() {
        return params;
    }

    public String getSegmentsCsv() {
        return segmentsCsv;
    }

    public String getSegEdgesCsv() {
        return segEdgesCsv;
    }

    public String getExRoutesCsv() {
        return exRoutesCsv;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    @Override
    public String toString() {
        return String.format("Speed configuration: bound: [%f, %f, %f, %f]"
                        + " * outputSegmentSpeed: [%b], calcEdgeSpeed: [%b], calcErrFlags: [%b], enableDbgLog: [%b]"
                        + " * timePointType: [%d], timePointFrom: [%s], timePointTo: [%s], timeWindow: [%d], timeWindowForSpeed: [%d]"
                        + " * maxTimeInterval: [%d], minDistInterval: [%f], maxDistInterval: [%f]"
                        + " * passengerStateFilter: [%d], plateColorEffective: [%b], taxiPlateColor: [%d], taxiPlateRegex: [%s]"
                        + " * segmentsCsv: [%s], segEdgesCsv: [%s], exRoutesCsv: [%s]",
                params.minLat, params.minLng, params.maxLat, params.maxLng,
                params.outputSegmentSpeed, params.calcEdgeSpeed, params.calcErrFlags, params.enableDbgLog,
                params.timePointType, params.timePointFrom, params.timePointTo, params.timeWindow, params.timeWindowForSpeed,
                params.maxTimeInterval, params.minDistInterval, params.maxDistInterval,
                params.passengerStateFilter, params.plateColorEffective, params.taxiPlateColor, params.taxiPlateRegex,
                segmentsCsv, segEdgesCsv, exRoutesCsv);
    }

    public void setHanaCfgItem(String k, String v) {
        Objects.requireNonNull(k);
        Objects.requireNonNull(v);

        if (HANA_CFG_CALC_WINDOW.equals(k)) {
            this.params.timeWindow = Integer.parseInt(v);
        } else if (HANA_CFG_CALC_WINDOW_FOR_SPEED.equals(k)) {
            this.params.timeWindowForSpeed = Integer.parseInt(v);
        } else if (HANA_CFG_CALC_PASSENGER_STATE_FILER.equals(k)) {
            this.params.passengerStateFilter = Integer.parseInt(v);
        } else if (HANA_CFG_CALC_MIN_DIST_INTERVAL.equals(k)) {
            this.params.minDistInterval = Double.parseDouble(v);
        } else if (HANA_CFG_CALC_MAX_DIST_INTERVAL.equals(k)) {
            this.params.maxDistInterval = Double.parseDouble(v);
        } else if (HANA_CFG_CALC_MAX_TIME_INTERVAL.equals(k)) {
            this.params.maxTimeInterval = Integer.parseInt(v);
        } else if (HANA_CFG_CALC_OUTPUT_SEGMENT_SPEED.equals(k)) {
            this.params.outputSegmentSpeed = "1".equals(v) ? true : false;
        } else if (HANA_CFG_PLATE_COLOR_EFFECTIVE.equals(k)) {
            this.params.plateColorEffective = "1".equals(v) ? true : false;
        } else if (HANA_CFG_PLATE_COLOR.equals(k)) {
            this.params.taxiPlateColor = Integer.parseInt(v);
        } else if (HANA_CFG_PLATE_REGEX.equals(k)) {
            this.params.taxiPlateRegex = v;
        } else if (HANA_CFG_TIME_POINT_TYPE.equals(k)) {
            this.params.timePointType = Integer.parseInt(v);
        }
    }


    public Object clone() throws CloneNotSupportedException {
        SpeedConfiguration cfg;
        try {
            cfg = (SpeedConfiguration) super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error("Not support clone");
            throw e;
        }

        cfg.segmentsCsv = segmentsCsv;
        cfg.segEdgesCsv = segEdgesCsv;
        cfg.exRoutesCsv = exRoutesCsv;
        cfg.zoneId = zoneId;

        cfg.params = new ITaxiMonitor.TaxiMonitorSlotsParams();
        cfg.params.minLat = params.minLat;
        cfg.params.minLng = params.minLng;
        cfg.params.maxLat = params.maxLat;
        cfg.params.maxLng = params.maxLng;

        cfg.params.outputSegmentSpeed = params.outputSegmentSpeed;
        cfg.params.calcEdgeSpeed = params.calcEdgeSpeed;
        cfg.params.timePointType = params.timePointType; // 0: local time
        cfg.params.timePointFrom = params.timePointFrom;
        cfg.params.timePointTo = params.timePointTo;
        cfg.params.timeWindow = params.timeWindow;
        cfg.params.timeWindowForSpeed = params.timeWindowForSpeed;
        cfg.params.calcErrFlags = params.calcErrFlags;

        cfg.params.maxTimeInterval = params.maxTimeInterval;
        cfg.params.minDistInterval = params.minDistInterval;
        cfg.params.maxDistInterval = params.maxDistInterval;

        cfg.params.passengerStateFilter = params.passengerStateFilter; // 0: empty, 1: loaded, 2: both
        cfg.params.plateColorEffective = params.plateColorEffective;
        cfg.params.taxiPlateColor = params.taxiPlateColor;
        cfg.params.taxiPlateRegex = params.taxiPlateRegex;

        cfg.params.enableDbgLog = params.enableDbgLog;

        return cfg;
    }

    public boolean same(SpeedConfiguration cfg) {
        if (this == cfg) {
            return true;
        }

        if (cfg == null) {
            return false;
        }

        if (!Objects.equals(segmentsCsv, cfg.segmentsCsv)
                || !Objects.equals(segEdgesCsv, cfg.segEdgesCsv)
                || !Objects.equals(exRoutesCsv, cfg.exRoutesCsv)
                || !Objects.equals(zoneId, cfg.zoneId)) {
            return false;
        }

        if (this.params != cfg.params) {
            if (Math.abs(cfg.params.minLat - params.minLat) > MAX_DOUBLE_DEVIATION
                    || Math.abs(cfg.params.minLng - params.minLng) > MAX_DOUBLE_DEVIATION
                    || Math.abs(cfg.params.maxLat - params.maxLat) > MAX_DOUBLE_DEVIATION
                    || Math.abs(cfg.params.maxLng - params.maxLng) > MAX_DOUBLE_DEVIATION
                    || !Objects.equals(cfg.params.outputSegmentSpeed, params.outputSegmentSpeed)
                    || !Objects.equals(cfg.params.calcEdgeSpeed, params.calcEdgeSpeed)
                    || !Objects.equals(cfg.params.timePointType, params.timePointType)
                    || !Objects.equals(cfg.params.timePointFrom, params.timePointFrom)
                    || !Objects.equals(cfg.params.timePointTo, params.timePointTo)
                    || !Objects.equals(cfg.params.timeWindow, params.timeWindow)
                    || !Objects.equals(cfg.params.timeWindowForSpeed, params.timeWindowForSpeed)
                    || !Objects.equals(cfg.params.calcErrFlags, params.calcErrFlags)
                    || !Objects.equals(cfg.params.maxTimeInterval, params.maxTimeInterval)
                    || Math.abs(cfg.params.minDistInterval - params.minDistInterval) > MAX_DOUBLE_DEVIATION
                    || Math.abs(cfg.params.maxDistInterval - params.maxDistInterval) > MAX_DOUBLE_DEVIATION
                    || !Objects.equals(cfg.params.passengerStateFilter, params.passengerStateFilter)
                    || !Objects.equals(cfg.params.plateColorEffective, params.plateColorEffective)
                    || !Objects.equals(cfg.params.taxiPlateColor, params.taxiPlateColor)
                    || !Objects.equals(cfg.params.taxiPlateRegex, params.taxiPlateRegex)
                    || !Objects.equals(cfg.params.enableDbgLog, params.enableDbgLog)) {
                return false;
            }
        }

        return true;
    }
}
