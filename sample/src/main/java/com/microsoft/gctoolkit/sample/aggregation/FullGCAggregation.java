package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregation;
import com.microsoft.gctoolkit.aggregator.Collates;
import com.microsoft.gctoolkit.event.GCCause;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.event.MemoryPoolSummary;
import com.microsoft.gctoolkit.time.DateTimeStamp;

@Collates(FullGCAggregator.class)
public interface FullGCAggregation extends Aggregation {
    void record_FullGC_Cause(DateTimeStamp timeStamp, GCCause cause, double pauseTime);

    void record_FullGC_Type(GarbageCollectionTypes garbageCollectionType);

    void record_FullGC_pauseTime(double pauseTime);

    void recordHeaps(MemoryPoolSummary young, MemoryPoolSummary tenured, MemoryPoolSummary heap);

    void record_GCPauseDuration_TimeRange_summary(double pauseTime);
}
