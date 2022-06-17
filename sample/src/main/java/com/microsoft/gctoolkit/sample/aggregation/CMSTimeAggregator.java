package com.microsoft.gctoolkit.sample.aggregation;

import com.microsoft.gctoolkit.aggregator.Aggregates;
import com.microsoft.gctoolkit.aggregator.Aggregator;
import com.microsoft.gctoolkit.aggregator.EventSource;
import com.microsoft.gctoolkit.event.generational.CMSPauseEvent;


@Aggregates({EventSource.GENERATIONAL})
public class CMSTimeAggregator extends Aggregator<CMSTimeAggregation> {

    public CMSTimeAggregator(CMSTimeAggregation aggregation) {
        super(aggregation);
        register(CMSPauseEvent.class, this::process);
    }

    public void process(CMSPauseEvent event) {
        aggregation().recordInitialMarkDuration(event.getDuration());
        aggregation().recordMemPool(event.getTenured());
    }

}
