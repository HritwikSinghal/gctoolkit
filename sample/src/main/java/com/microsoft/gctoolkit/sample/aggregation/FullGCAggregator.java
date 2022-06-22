package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregates;
import com.microsoft.gctoolkit.aggregator.Aggregator;
import com.microsoft.gctoolkit.aggregator.EventSource;
import com.microsoft.gctoolkit.event.generational.GenerationalGCPauseEvent;

@Aggregates(EventSource.GENERATIONAL)
public class FullGCAggregator extends Aggregator<FullGCAggregation> {

    public FullGCAggregator(FullGCAggregation aggregation) {
        super(aggregation);
        register(GenerationalGCPauseEvent.class, this::process);
    }

    public void process(GenerationalGCPauseEvent event) {
//        System.out.println();
//        System.out.println(event.getDuration());
//        System.out.println(event.getGCCause());
//        System.out.println(event.getGarbageCollectionType());
//        System.out.println(event.getYoung().toString());
//        System.out.println(event.getTenured().toString());
//        System.out.println(event.getHeap().toString());

//        System.out.println(event.getClassspace().kBytesRecovered());

//        System.out.println(event.getPermOrMetaspace().toString());
//        System.out.println(event.getNonClassspace().toString());
//        System.out.println(event.getClassspace().toString());
//        System.out.println(event.getReferenceGCSummary().toString());
//        System.out.println(event.getCpuSummary().toString());


        aggregation().record_FullGC_Cause(event.getDateTimeStamp(), event.getGCCause(), event.getDuration());
        aggregation().record_FullGC_Type(event.getGarbageCollectionType());
    }
}
