package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregates;
import com.microsoft.gctoolkit.aggregator.Aggregator;
import com.microsoft.gctoolkit.aggregator.EventSource;
import com.microsoft.gctoolkit.event.GCEvent;
import com.microsoft.gctoolkit.event.generational.GenerationalGCEvent;

@Aggregates(EventSource.GENERATIONAL)
public class FullGCAggregator extends Aggregator<FullGCAggregation> {

    public FullGCAggregator(FullGCAggregation aggregation) {
        super(aggregation);
        register(GenerationalGCEvent.class, this::process);
    }

    public void process(GenerationalGCEvent event) {
        aggregation().recordFullGC(event.getDateTimeStamp(), event.getGCCause(), event.getDuration());
        aggregation().record_gc_summary(event.getGarbageCollectionType());
    }
}
