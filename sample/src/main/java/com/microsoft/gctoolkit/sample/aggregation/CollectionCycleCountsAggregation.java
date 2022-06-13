package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregation;
import com.microsoft.gctoolkit.aggregator.Collates;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;

public interface CollectionCycleCountsAggregation extends Aggregation {

    void count(GarbageCollectionTypes gcType);

}
