package com.microsoft.gctoolkit.sample.aggregation;

public class CMSTimeSummaryAggregation extends CMSTimeAggregation {

    private double totalPauseTime;

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void recordCMSTimeDuration(double duration) {
        totalPauseTime += duration;
    }

    public double getTotalPauseTime() {
        return totalPauseTime;
    }

    public double getPercentPaused() {
        return (totalPauseTime / getRuntimeDuration()) * 100.0D;
    }

    public double getThroughput() {
        return 100 - getPercentPaused();
    }
}
