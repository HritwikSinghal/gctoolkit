package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.event.MemoryPoolSummary;

import java.util.ArrayList;

public class CMSTimeSummaryAggregation implements CMSTimeAggregation {

    private double totalInitialMarkTime;
    private ArrayList<String> mem_pool_list = new ArrayList<>();

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
    }

    @Override
    public void recordMemPool(MemoryPoolSummary memoryPoolSummary) {
        mem_pool_list.add(memoryPoolSummary.toString());
    }

    public double getTotalInitialMarkTime() {
        return totalInitialMarkTime;
    }

    public ArrayList<String> getMemPoolList() {
        return mem_pool_list;
    }
}
