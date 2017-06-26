package com.sap.icn.traffic;

import com.sap.traffic.foundation.traffic_lib.ITaxiMonitor;

import java.io.Serializable;
import java.util.ArrayList;

public final class SpeedResult implements Serializable {
    private ArrayList<ITaxiMonitor.TaxiSlotStats> taxiSlotStats = new ArrayList<>();
    private ArrayList<ITaxiMonitor.TimedSegmentSpeeds> timedSegmentSpeeds = new ArrayList<>();
    private ArrayList<ITaxiMonitor.TimedEdgeSpeeds> timedEdgeSpeeds = new ArrayList<>();
    private StringBuffer errBuf = new StringBuffer();
    private String group = "";
    private boolean ok = false;
    private String message = "";

    public ArrayList<ITaxiMonitor.TaxiSlotStats> getTaxiSlotStats() {
        return taxiSlotStats;
    }

    public ArrayList<ITaxiMonitor.TimedSegmentSpeeds> getTimedSegmentSpeeds() {
        return timedSegmentSpeeds;
    }

    public ArrayList<ITaxiMonitor.TimedEdgeSpeeds> getTimedEdgeSpeeds() {
        return timedEdgeSpeeds;
    }

    public StringBuffer getErrBuf() {
        return errBuf;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTaxiSlotStats(ArrayList<ITaxiMonitor.TaxiSlotStats> taxiSlotStats) {
        this.taxiSlotStats = taxiSlotStats;
    }

    public void setTimedSegmentSpeeds(ArrayList<ITaxiMonitor.TimedSegmentSpeeds> timedSegmentSpeeds) {
        this.timedSegmentSpeeds = timedSegmentSpeeds;
    }

    public void setTimedEdgeSpeeds(ArrayList<ITaxiMonitor.TimedEdgeSpeeds> timedEdgeSpeeds) {
        this.timedEdgeSpeeds = timedEdgeSpeeds;
    }

    public void setErrBuf(StringBuffer errBuf) {
        this.errBuf = errBuf;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}


