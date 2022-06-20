package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregation;
import com.microsoft.gctoolkit.aggregator.Collates;
import com.microsoft.gctoolkit.event.GCCause;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.time.DateTimeStamp;

import java.util.Map;

@Collates(CMSTimeAggregator.class)
public interface CMSTimeAggregation extends Aggregation {
    void recordInitialMarkDuration(double duration);

    void record_gc_summary(GarbageCollectionTypes gctype);
}
