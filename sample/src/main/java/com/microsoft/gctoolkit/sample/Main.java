package com.microsoft.gctoolkit.sample;

import com.microsoft.gctoolkit.GCToolKit;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.io.GCLogFile;
import com.microsoft.gctoolkit.io.SingleGCLogFile;
import com.microsoft.gctoolkit.jvm.JavaVirtualMachine;
import com.microsoft.gctoolkit.sample.aggregation.CollectionCycleCountsSummary;
import com.microsoft.gctoolkit.sample.aggregation.HeapOccupancyAfterCollectionSummary;
import com.microsoft.gctoolkit.sample.aggregation.PauseTimeSummary;
import com.microsoft.gctoolkit.sample.collections.XYDataSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

public class Main {
    private int initialMarkCount = 0;
    private int remarkCount = 0;
    private int defNewCount = 0;

    public int getInitialMarkCount() {
        return initialMarkCount;
    }

    public int getRemarkCount() {
        return remarkCount;
    }

    public int getDefNewCount() {
        return defNewCount;
    }

    public void analyze(String gcLogFile) throws IOException {
        /**
         * GC log files can come in  one of two types: single or series of rolling logs.
         * In this sample, we load a single log file.
         * The log files can be either in text, zip, or gzip format.
         */
        GCLogFile logFile = new SingleGCLogFile(Path.of(gcLogFile));
        GCToolKit gcToolKit = new GCToolKit();

        /**
         * This call will load all implementations of Aggregator that have been declared in module-info.java.
         * This mechanism makes use of Module SPI.
         */
        gcToolKit.loadAggregationsFromServiceLoader();

        /**
         * The JavaVirtualMachine contains the aggregations as filled out by the Aggregators.
         * It also contains configuration information about how the JVM was configured for the runtime.
         */
        JavaVirtualMachine machine = gcToolKit.analyze(logFile);

        // Retrieves the Aggregation for HeapOccupancyAfterCollectionSummary. This is a time-series aggregation.
        String message = "The XYDataSet for %s contains %s items.\n";

        Optional<HeapOccupancyAfterCollectionSummary> temp = machine.getAggregation(HeapOccupancyAfterCollectionSummary.class);
        temp.map(HeapOccupancyAfterCollectionSummary::get).ifPresent(summary -> {
            for (Map.Entry<GarbageCollectionTypes, XYDataSet> entry : summary.entrySet()) {
                GarbageCollectionTypes gcType = entry.getKey();
                XYDataSet dataSet = entry.getValue();
                System.out.printf(message, gcType, dataSet.size());
                switch (gcType) {
                    case DefNew:
                        defNewCount = dataSet.size();
                        break;
                    case InitialMark:
                        initialMarkCount = dataSet.size();
                        break;
                    case Remark:
                        remarkCount = dataSet.size();
                        break;
                    default:
                        System.out.println(gcType + " not managed");
                        break;
                }
            }
        });

        Optional<CollectionCycleCountsSummary> summary = machine.getAggregation(CollectionCycleCountsSummary.class);
        summary.ifPresent(s -> s.printOn(System.out));
        // Retrieves the Aggregation for PauseTimeSummary. This is a com.microsoft.gctoolkit.sample.aggregation.RuntimeAggregation.
        machine.getAggregation(PauseTimeSummary.class).ifPresent(pauseTimeSummary -> {
            System.out.println();
            System.out.printf("Total pause time                  : %.2f sec\n", pauseTimeSummary.getTotalPauseTime());
            System.out.printf("Total run time for the program    : %.2f sec\n", pauseTimeSummary.getRuntimeDuration());
            System.out.printf("Percent pause time                : %.3f %%\n", pauseTimeSummary.getPercentPaused());
            System.out.printf("Percent Throughput                : %.4f %%\n", pauseTimeSummary.getThroughput());
        });

    }

    public static void main(String[] args) throws IOException {
        String userInput = args.length > 0 ? args[0] : "";
        String gcLogFile = System.getProperty("gcLogFile", userInput);

        if (gcLogFile.isBlank()) {
            throw new IllegalArgumentException("This sample requires a path to a GC log file.");
        }

        if (Files.notExists(Path.of(gcLogFile))) {
            throw new IllegalArgumentException(String.format("File %s not found.", gcLogFile));
        }

        Main main = new Main();
        main.analyze(gcLogFile);
    }

}
