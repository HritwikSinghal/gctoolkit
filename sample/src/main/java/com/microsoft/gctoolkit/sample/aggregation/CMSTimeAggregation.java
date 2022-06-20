package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregation;
import com.microsoft.gctoolkit.aggregator.Collates;

@Collates(CMSTimeAggregator.class)
public interface CMSTimeAggregation extends Aggregation {
    void recordInitialMarkDuration(double duration);
}
