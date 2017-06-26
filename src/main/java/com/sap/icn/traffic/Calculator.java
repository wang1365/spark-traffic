package com.sap.icn.traffic;


import com.sap.traffic.foundation.traffic_lib.ITaxiMonitor;
import com.sap.traffic.foundation.traffic_lib.TaxiMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;


/**
 * Created by I321761 on 2017/5/4.
 */
public class Calculator implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(Calculator.class);
    private SpeedConfiguration config = new SpeedConfiguration();
    private ITaxiMonitor monitor = new TaxiMonitor();

    public SpeedResult execute(List<ITaxiMonitor.TaxiPoint> taxiPoints, long timestampFrom, long timestampTo) {
        if (taxiPoints == null) {
            logger.error("Invalid null input parameter");
            return null;
        }

        SpeedResult result = new SpeedResult();
        ITaxiMonitor.TaxiMonitorSlotsParams params = this.config.getParams();
        if (params == null) {
            logger.error("Invalid params input parameter");
            return null;
        }

        DateTimeUtil util = DateTimeUtil.of("UTC+8");
        String singleSlotTimestamp = util.format(timestampTo);
        params.timePointFrom = util.format(timestampFrom);
        params.timePointTo = util.format(timestampTo);
        params.enableDbgLog = true;
        params.timePointType = 1;

        String segmentsCsv = config.getSegmentsCsv();
        String segEdgesCsv = config.getSegEdgesCsv();
        String exRoutesCsv = config.getExRoutesCsv();
        if (segmentsCsv == null || segEdgesCsv == null || exRoutesCsv == null) {
            logger.error("Invalid null input parameter");
            return null;
        }

        logger.info("SpeedConfig: {}", config);

        boolean ok = monitor.monitorSlotsCsv(params, taxiPoints,
                segmentsCsv, segEdgesCsv, exRoutesCsv,
                result.getTaxiSlotStats(), result.getTimedSegmentSpeeds(), result.getTimedEdgeSpeeds(),
                result.getErrBuf());

        result.setOk(ok);
        String err = result.getErrBuf().toString();
        if (!err.isEmpty()) {
            logger.error("Cannot calculate speed, reason: {}", err);
        }

        return result;
    }
}
