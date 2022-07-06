package com.microsoft.gctoolkit.sample;

import java.util.HashMap;
import java.util.Map;

public class FullGCStats {
    private int initialMarkCount = 0;
    private int remarkCount = 0;
    private int defNewCount = 0;

    private double fullGC_throughput = 0;
    private double fullGC_avg_pause_time = 0;
    private double fullGC_max_pause_time = 0;
    private double GCPauseDuration_TimeInterval = 10; // in milliseconds

    private Map<Double, Integer> GCPauseDuration_TimeRange_summary = new HashMap<>();


    // ------------------------------------------------------------------------- //
    public int getInitialMarkCount() {
        return initialMarkCount;
    }

    public void setInitialMarkCount(int initialMarkCount) {
        this.initialMarkCount = initialMarkCount;
    }

    public int getRemarkCount() {
        return remarkCount;
    }

    public void setRemarkCount(int remarkCount) {
        this.remarkCount = remarkCount;
    }

    public int getDefNewCount() {
        return defNewCount;
    }

    public void setDefNewCount(int defNewCount) {
        this.defNewCount = defNewCount;
    }


    public double getFullGC_throughput() {
        return fullGC_throughput;
    }

    public void setFullGC_throughput(double fullGC_throughput) {
        this.fullGC_throughput = fullGC_throughput;
    }

    public double getFullGC_avg_pause_time() {
        return fullGC_avg_pause_time;
    }

    public void setFullGC_avg_pause_time(double fullGC_avg_pause_time) {
        this.fullGC_avg_pause_time = fullGC_avg_pause_time;
    }

    public double getFullGC_max_pause_time() {
        return fullGC_max_pause_time;
    }

    public void setFullGC_max_pause_time(double fullGC_max_pause_time) {
        this.fullGC_max_pause_time = fullGC_max_pause_time;
    }

    public double getGCPauseDuration_TimeInterval() {
        return GCPauseDuration_TimeInterval;
    }

    public void setGCPauseDuration_TimeInterval(double GCPauseDuration_TimeInterval) {
        this.GCPauseDuration_TimeInterval = GCPauseDuration_TimeInterval;
    }

    public Map<Double, Integer> getGCPauseDuration_TimeRange_summary() {
        return GCPauseDuration_TimeRange_summary;
    }

    public void setGCPauseDuration_TimeRange_summary(Map<Double, Integer> GCPauseDuration_TimeRange_summary) {
        this.GCPauseDuration_TimeRange_summary = GCPauseDuration_TimeRange_summary;
    }

    public void fill_GCPauseDuration_TimeRange_summary(Map<Double, Integer> GCPauseDuration_TimeRange) {
        // first need to convert every value to ms from sec
        GCPauseDuration_TimeRange.forEach((pause_time, count) -> {
            Double pause_time_ms = pause_time * 1000;       // convert to milliseconds

//            this.GCPauseDuration_TimeRange_summary.put(pause_time_ms, count);
//            System.out.printf("%.2f ms: %d\n", pause_time_ms % 10, count);
        });
    }
}
