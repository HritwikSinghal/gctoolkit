package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Collates;

@Collates(CMSTimeAggregator.class)
public class CMSTimeSummaryAggregation implements CMSTimeAggregation {

    private double total_duration;
    private double maxInitialMarkTime;
    private double minInitialMarkTime;

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public double getTotalInitialMarkTime() {
        return total_duration;
    }

    public double getMinInitialMarkTime() {
        return minInitialMarkTime;
    }

    public double getMaxInitialMarkTime() {
        return maxInitialMarkTime;
    }

    @Override
    public void recordInitialMarkDuration(double duration) {
        total_duration += duration;
        if (duration > maxInitialMarkTime) {
            maxInitialMarkTime = duration;
        }
        if (duration < minInitialMarkTime) {
            minInitialMarkTime = duration;
        }
    }
}
