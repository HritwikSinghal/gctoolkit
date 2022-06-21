package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.event.GCCause;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.event.MemoryPoolSummary;
import com.microsoft.gctoolkit.time.DateTimeStamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FullGCAggregationSummary implements FullGCAggregation {

    private Map<GCCause, Double> maxPauseTime = new HashMap<>();
    private Map<GarbageCollectionTypes, Integer> gctype_summary = new HashMap<>();

    private Map<GCCause, Integer> gccause_summary = new HashMap<>();

    private ArrayList<Long> young_occupancyBeforeCollection = new ArrayList<>();
    private ArrayList<Long> young_occupancyAfterCollection = new ArrayList<>();

    private ArrayList<Long> tenured_occupancyBeforeCollection = new ArrayList<>();
    private ArrayList<Long> tenured_occupancyAfterCollection = new ArrayList<>();

    private ArrayList<Long> heap_occupancyBeforeCollection = new ArrayList<>();
    private ArrayList<Long> heap_occupancyAfterCollection = new ArrayList<>();


    @Override
    public void record_gc_summary(GarbageCollectionTypes garbageCollectionType) {
        gctype_summary.compute(garbageCollectionType, (key, value) -> value == null ? 1 : ++value);
    }

    @Override
    public void recordFullGC(DateTimeStamp timeStamp, GCCause cause, double pauseTime) {
        maxPauseTime.compute(cause, (k, v) -> (v == null) ? pauseTime : Math.max(v, pauseTime));
        gccause_summary.compute(cause, (key, value) -> value == null ? 1 : ++value);
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

    public Map<GarbageCollectionTypes, Integer> get_gcTypeSummary() {
        return gctype_summary;
    }

    public Map<GCCause, Integer> getGccause_summary() {
        return gccause_summary;
    }

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

}
