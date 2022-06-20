package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregates;
import com.microsoft.gctoolkit.aggregator.Aggregator;
import com.microsoft.gctoolkit.aggregator.EventSource;
import com.microsoft.gctoolkit.event.GCEvent;
import com.microsoft.gctoolkit.event.generational.FullGC;
import com.microsoft.gctoolkit.event.generational.GenerationalGCEvent;
import com.microsoft.gctoolkit.event.generational.GenerationalGCPauseEvent;
import com.microsoft.gctoolkit.event.generational.YoungGC;

@Aggregates(EventSource.GENERATIONAL)
public class FullGCAggregator extends Aggregator<FullGCAggregation> {

    public FullGCAggregator(FullGCAggregation aggregation) {
        super(aggregation);
        register(GenerationalGCPauseEvent.class, this::process);
    }

    public void process(GenerationalGCPauseEvent event) {
        aggregation().recordFullGC(event.getDateTimeStamp(), event.getGCCause(), event.getDuration());
        aggregation().record_gc_summary(event.getGarbageCollectionType());
    }
}
