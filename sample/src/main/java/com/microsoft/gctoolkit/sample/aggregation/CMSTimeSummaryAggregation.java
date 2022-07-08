package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Collates;

@Collates(CMSTimeAggregator.class)
public class CMSTimeSummaryAggregation implements CMSTimeAggregation {

    public double total_duration;
    public double maxEventTime;
    public double minEventTime;

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void recordEventDuration(double duration) {
        total_duration += duration;

        if (duration > maxEventTime)
            maxEventTime = duration;
        if (duration < minEventTime)
            minEventTime = duration;

    }
}
