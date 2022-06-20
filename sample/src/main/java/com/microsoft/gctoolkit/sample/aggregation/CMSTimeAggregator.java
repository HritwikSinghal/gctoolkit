package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregates;
import com.microsoft.gctoolkit.aggregator.Aggregator;
import com.microsoft.gctoolkit.aggregator.EventSource;
import com.microsoft.gctoolkit.event.GCEvent;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.event.generational.*;

import java.util.HashMap;
import java.util.Map;


@Aggregates({EventSource.GENERATIONAL})
public class CMSTimeAggregator extends Aggregator<CMSTimeAggregation> {

    public CMSTimeAggregator(CMSTimeAggregation aggregation) {
        super(aggregation);
        register(InitialMark.class, this::process);
    }

    public void process(InitialMark event) {
        aggregation().recordInitialMarkDuration(event.getDuration());
        aggregation().record_gc_summary(event.getGarbageCollectionType());
    }

}
