package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregates;
import com.microsoft.gctoolkit.aggregator.Aggregator;
import com.microsoft.gctoolkit.aggregator.EventSource;
import com.microsoft.gctoolkit.event.generational.CMSRemark;
import com.microsoft.gctoolkit.event.generational.ConcurrentPreClean;
import com.microsoft.gctoolkit.event.generational.InitialMark;

@Aggregates({EventSource.GENERATIONAL})
public class CMSTimeAggregator extends Aggregator<CMSTimeAggregation> {

    public CMSTimeAggregator(CMSTimeAggregation aggregation) {
        super(aggregation);
//        register(InitialMark.class, this::process);
//        register(CMSRemark.class, this::process);
        register(ConcurrentPreClean.class, this::process);
    }

    public void process(InitialMark event) {
        aggregation().recordEventDuration(event.getDuration());
    }

    public void process(CMSRemark event) {
        aggregation().recordEventDuration(event.getDuration());
    }

    public void process(ConcurrentPreClean event) {
        aggregation().recordEventDuration(event.getDuration());
    }

}
