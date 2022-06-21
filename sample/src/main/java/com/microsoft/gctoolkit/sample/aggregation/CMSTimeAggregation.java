package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregation;

//@Collates(CMSTimeAggregator.class)
public interface CMSTimeAggregation extends Aggregation {
    void recordEventDuration(double duration);
}
