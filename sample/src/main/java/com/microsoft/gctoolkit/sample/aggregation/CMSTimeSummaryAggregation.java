package com.microsoft.gctoolkit.sample.aggregation;

public class CMSTimeSummaryAggregation implements CMSTimeAggregation {

    private double totalInitialMarkTime;
    private double maxInitialMarkTime;
    private double minInitialMarkTime;

    //    private ArrayList<String> mem_pool_list = new ArrayList<>();

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
}
