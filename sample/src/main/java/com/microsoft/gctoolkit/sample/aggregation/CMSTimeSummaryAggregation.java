package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Collates;

@Collates(CMSTimeAggregator.class)
public class CMSTimeSummaryAggregation implements CMSTimeAggregation {

    private double total_duration;
    private double maxEventTime;
    private double minEventTime;

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public double getTotalEventTime() {
        return total_duration;
    }

    public double getMinEventTime() {
        return minEventTime;
    }

    public double getMaxEventTime() {
        return maxEventTime;
    }

    @Override
    public void recordEventDuration(double duration) {
        total_duration += duration;
        if (duration > maxEventTime) {
            maxEventTime = duration;
        }
        if (duration < minEventTime) {
            minEventTime = duration;
        }
    }
}
