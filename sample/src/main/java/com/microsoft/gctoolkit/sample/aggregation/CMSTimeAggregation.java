package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregation;
import com.microsoft.gctoolkit.aggregator.Collates;

@Collates(CMSTimeAggregator.class)
public abstract class CMSTimeAggregation extends RuntimeAggregation {
    public abstract void recordCMSTimeDuration(double duration);

}
