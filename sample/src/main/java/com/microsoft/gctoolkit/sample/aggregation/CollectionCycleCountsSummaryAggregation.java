package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Collates;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;

import java.io.PrintStream;
import java.util.HashMap;

@Collates(CollectionCycleCountsAggregator.class)
public class CollectionCycleCountsSummaryAggregation implements CollectionCycleCountsAggregation {

    private HashMap<GarbageCollectionTypes,Integer> collectionCycleCounts = new HashMap<>();
    @Override
    public void count(GarbageCollectionTypes gcType) {
        collectionCycleCounts.compute(gcType, (key, value) -> value == null ? 1 : ++value);
    }

    private String format = "%s : %s\n";
    public void printOn(PrintStream printStream) {
        collectionCycleCounts.keySet().forEach(k -> printStream.printf(format,k, collectionCycleCounts.get(k)));
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
