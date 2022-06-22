package com.microsoft.gctoolkit.sample;

public class GCStats {
    private int initialMarkCount = 0;
    private int remarkCount = 0;
    private int defNewCount = 0;

    private double fullGC_throughput = 0;
    private double fullGC_avg_pause_time = 0;
    private double fullGC_max_pause_time = 0;
    private double throughput = 0.00;

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

    public double getThroughput() {
        return throughput;
    }

    public void setThroughput(double throughput) {
        this.throughput = throughput;
    }

}
