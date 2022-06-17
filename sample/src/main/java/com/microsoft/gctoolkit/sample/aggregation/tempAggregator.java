package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregates;
import com.microsoft.gctoolkit.aggregator.Aggregator;
import com.microsoft.gctoolkit.aggregator.EventSource;


//@Aggregates({EventSource.GENERATIONAL})
public class tempAggregator extends Aggregator<HeapOccupancyAfterCollectionAggregation> {

    protected tempAggregator(HeapOccupancyAfterCollectionAggregation aggregation) {
        super(aggregation);
    }

}
