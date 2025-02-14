package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.event.GarbageCollectionTypes;

import java.io.PrintStream;
import java.util.HashMap;

public class CollectionCycleCountsSummaryAggregation implements CollectionCycleCountsAggregation {

    private HashMap<GarbageCollectionTypes, Integer> collectionCycleCounts = new HashMap<>();
    private String format = "%s : %s\n";

    @Override
    public void count(GarbageCollectionTypes gcType) {
        // increments the values in map by 1 if not null else sets it to 1
        collectionCycleCounts.compute(gcType, (key, value) -> value == null ? 1 : ++value);
    }

    public void printOn(PrintStream printStream) {
        collectionCycleCounts.keySet().forEach(k -> printStream.printf(format, k, collectionCycleCounts.get(k)));
    }

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return collectionCycleCounts.isEmpty();
    }
}
