package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.event.GCCause;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.event.MemoryPoolSummary;
import com.microsoft.gctoolkit.time.DateTimeStamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.OptionalDouble;

public class FullGCAggregationSummary implements FullGCAggregation {
    private Map<GCCause, Double> GCCause_total_pause_time_summary = new HashMap<>();

    private Map<GCCause, Double> GCCause_max_PauseTime_duration_summary = new HashMap<>();

    private Map<GCCause, Integer> GCCause_total_count_summary = new HashMap<>();

    private Map<GarbageCollectionTypes, Integer> GCType_total_count_summary = new HashMap<>();

    private ArrayList<Double> pause_time_summary = new ArrayList<>();

    private double max_GC_pause_time = 0.0;
    private double average_GC_pause_time = 0.0;

    // ------------------------------------------------------------------ //


    private ArrayList<Long> young_occupancyBeforeCollection = new ArrayList<>();
    private ArrayList<Long> young_occupancyAfterCollection = new ArrayList<>();

    private ArrayList<Long> tenured_occupancyBeforeCollection = new ArrayList<>();
    private ArrayList<Long> tenured_occupancyAfterCollection = new ArrayList<>();

    private ArrayList<Long> heap_occupancyBeforeCollection = new ArrayList<>();
    private ArrayList<Long> heap_occupancyAfterCollection = new ArrayList<>();

    // ------------------------------------------------------------------ //

    private HashMap<Double, Integer> GCPauseDuration_TimeRange_summary = new HashMap<>();

    // ------------------------------------------------------------------ //
    // ------------------------------------------------------------------ //
    // ------------------------------------------------------------------ //
    // ------------------------------------------------------------------ //
    // ------------------------------------------------------------------ //


    // ------------------------------------------- All record methods below (no set methods) ------------------------------------------- //

    @Override
    public void record_FullGC_Type(GarbageCollectionTypes garbageCollectionType) {
        GCType_total_count_summary.compute(garbageCollectionType, (key, value) -> value == null ? 1 : ++value);
    }

    @Override
    public void record_FullGC_Cause(DateTimeStamp timeStamp, GCCause cause, double pauseTime) {
        GCCause_total_count_summary.compute(cause, (key, value) -> value == null ? 1 : ++value);

        GCCause_max_PauseTime_duration_summary.compute(cause, (k, v) -> (v == null) ? pauseTime : Math.max(v, pauseTime));

        GCCause_total_pause_time_summary.compute(cause, (k, v) -> {
            if (v == null) {
                return (double) 0;
            } else {
                return v + pauseTime;
            }
        });

        GCCause_max_PauseTime_duration_summary.forEach((gcCause, max_time) -> {
            if (max_time > max_GC_pause_time)
                max_GC_pause_time = max_time;
        });
    }

    @Override
    public void recordHeaps(MemoryPoolSummary young, MemoryPoolSummary tenured, MemoryPoolSummary heap) {
        young_occupancyBeforeCollection.add(young.getOccupancyBeforeCollection());
        young_occupancyAfterCollection.add(young.getOccupancyAfterCollection());

        tenured_occupancyBeforeCollection.add(tenured.getOccupancyBeforeCollection());
        tenured_occupancyAfterCollection.add(tenured.getOccupancyAfterCollection());

        heap_occupancyBeforeCollection.add(heap.getOccupancyBeforeCollection());
        heap_occupancyAfterCollection.add(heap.getOccupancyAfterCollection());
    }

    @Override
    public void record_FullGC_pauseTime(double pauseTime) {
        pause_time_summary.add(pauseTime);
    }

    @Override
    public void record_GCPauseDuration_TimeRange_summary(double pauseTime) {
        // Attempts to compute a mapping for the specified key and its current mapped value
        // (or null if there is no current mapping). For example,
        // to either create or append a String msg to a value mapping:
        // map.compute(key, (k, v) -> (v == null) ? msg : v.concat(msg))

        GCPauseDuration_TimeRange_summary.compute(pauseTime, (key, value) -> value == null ? 1 : ++value);
    }
    // ------------------------------------------------------------------ //

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    // ------------------------------------------- Only Get methods below ------------------------------------------- //

    public double get_MaxGCPauseTime() {
        return max_GC_pause_time;
    }

    public double getAverage_GC_pause_time() {
        OptionalDouble average = pause_time_summary
                .stream()
                .mapToDouble(a -> a)
                .average();

        return average.isPresent() ? average.getAsDouble() : 0;
    }

    public Map<GCCause, Double> get_GCCause_max_PauseTime_duration_summary() {
        return GCCause_max_PauseTime_duration_summary;
    }

    public Map<GCCause, Double> get_GCCause_total_pause_time_summary() {
        return GCCause_total_pause_time_summary;
    }


    public Map<GCCause, Integer> get_GCCause_total_count_summary() {
        return GCCause_total_count_summary;
    }

    public Map<GarbageCollectionTypes, Integer> get_GCType_total_count_summary() {
        return GCType_total_count_summary;
    }

    // ------------------------------------------------------------------ //

    public ArrayList<Long> get_young_occupancyBeforeCollection() {
        return young_occupancyBeforeCollection;
    }

    public ArrayList<Long> get_young_occupancyAfterCollection() {
        return young_occupancyAfterCollection;
    }

    public ArrayList<Long> get_tenured_occupancyBeforeCollection() {
        return tenured_occupancyBeforeCollection;
    }

    public ArrayList<Long> get_tenured_occupancyAfterCollection() {
        return tenured_occupancyAfterCollection;
    }

    public ArrayList<Long> get_heap_occupancyBeforeCollection() {
        return heap_occupancyBeforeCollection;
    }

    public ArrayList<Long> get_heap_occupancyAfterCollection() {
        return heap_occupancyAfterCollection;
    }

    // ------------------------------------------------------------------ //

    public HashMap<Double, Integer> getGCPauseDuration_TimeRange_summary() {
        return GCPauseDuration_TimeRange_summary;
    }
}
