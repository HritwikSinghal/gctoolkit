package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregation;
import com.microsoft.gctoolkit.aggregator.Collates;
import com.microsoft.gctoolkit.event.GCCause;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.time.DateTimeStamp;

@Collates(FullGCAggregator.class)
public interface FullGCAggregation extends Aggregation {
    void recordFullGC(DateTimeStamp timeStamp, GCCause cause, double pauseTime);

    void record_gc_summary(GarbageCollectionTypes gctype);

}
