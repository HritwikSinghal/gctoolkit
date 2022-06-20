package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.event.GCCause;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.time.DateTimeStamp;

import java.util.HashMap;
import java.util.Map;


public class CMSTimeSummaryAggregation implements CMSTimeAggregation {

    private double totalInitialMarkTime;
    private double maxInitialMarkTime;
    private double minInitialMarkTime;

    private Map<GarbageCollectionTypes, Integer> summary_gctype = new HashMap<>();

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void recordInitialMarkDuration(double duration) {
        totalInitialMarkTime += duration;
        if (duration > maxInitialMarkTime) {
            maxInitialMarkTime = duration;
        }
        if (duration < minInitialMarkTime) {
            minInitialMarkTime = duration;
        }
    }


    public double getTotalInitialMarkTime() {
        return totalInitialMarkTime;
    }

    public double getMinInitialMarkTime() {
        return minInitialMarkTime;
    }

    public double getMaxInitialMarkTime() {
        return maxInitialMarkTime;
    }

    public void record_gc_summary(GarbageCollectionTypes gctype) {
        summary_gctype.compute(gctype, (key, value) -> value == null ? 1 : ++value);
    }

    public Map<GarbageCollectionTypes, Integer> get_gc_summary() {
        return summary_gctype;
    }
}
