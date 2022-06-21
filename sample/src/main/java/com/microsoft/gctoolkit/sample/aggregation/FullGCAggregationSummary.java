package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.event.GCCause;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.time.DateTimeStamp;

import java.util.HashMap;
import java.util.Map;

public class FullGCAggregationSummary implements FullGCAggregation {

    private Map<GCCause, Double> maxPauseTime = new HashMap<>();
    private Map<GarbageCollectionTypes, Integer> summary_gctype = new HashMap<>();

//    private Map<GarbageCollectionTypes, Integer> summary_gctype = new HashMap<>();

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    public Map<GCCause, Double> get_MaxFullGCPauseTime() {
        return maxPauseTime;
    }

    public double get_MaxFullGCPauseTimeCause(GCCause cause) {
        return maxPauseTime.get(cause);
    }

    public Map<GarbageCollectionTypes, Integer> get_gc_summary() {
        return summary_gctype;
    }

    public void record_gc_summary(GarbageCollectionTypes gctype) {
        summary_gctype.compute(gctype, (key, value) -> value == null ? 1 : ++value);
    }
    @Override
    public void recordFullGC(DateTimeStamp timeStamp, GCCause cause, double pauseTime) {
        maxPauseTime.compute(cause, (k, v) -> (v == null) ? pauseTime : Math.max(v, pauseTime));
    }
}
