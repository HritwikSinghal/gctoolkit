package com.microsoft.gctoolkit.sample;

import com.microsoft.gctoolkit.GCToolKit;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.io.GCLogFile;
import com.microsoft.gctoolkit.io.SingleGCLogFile;
import com.microsoft.gctoolkit.jvm.JavaVirtualMachine;
import com.microsoft.gctoolkit.sample.aggregation.CollectionCycleCountsSummary;
import com.microsoft.gctoolkit.sample.aggregation.HeapOccupancyAfterCollectionSummaryAggregation;
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

        //----- Prints Heap Collection Summary using HeapCollection Classes -----//


//        machine.getAggregation(HeapOccupancyAfterCollectionSummary.class)
//                .map(HeapOccupancyAfterCollectionSummary::get).ifPresent(summary -> {
//                    summary.forEach((gcType, dataSet) -> {
//                        System.out.printf(message, gcType, dataSet.size());
//                        switch (gcType) {
//                            case DefNew:
//                                defNewCount = dataSet.size();
//                                break;
//                            case InitialMark:
//                                initialMarkCount = dataSet.size();
//                                break;
//                            case Remark:
//                                remarkCount = dataSet.size();
//                                break;
//                            default:
//                                System.out.println(gcType + " not managed");
//                                break;
//                        }
//                    });
//                });

        // 'aggregation' is 'HeapOccupancyAfterCollectionSummary' object but wrapped in 'Optional' keyword, so we have some
        // default functions like 'map'.
        Optional<HeapOccupancyAfterCollectionSummaryAggregation> aggregation = machine.getAggregation(HeapOccupancyAfterCollectionSummaryAggregation.class);

        // The "HeapOccupancyAfterCollectionSummary::get" will return a 'Map<GarbageCollectionTypes, XYDataSet>' wrapped in 'Optional'
        // and we are using "map" function of "Optional" to map it.

        Optional<Map<GarbageCollectionTypes, XYDataSet>> data_from_aggregation = aggregation.map(HeapOccupancyAfterCollectionSummaryAggregation::get);

        if (data_from_aggregation.isPresent()) {
            Map<GarbageCollectionTypes, XYDataSet> summary = data_from_aggregation.get();

            summary.forEach((gcType, dataSet) -> {
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
            });
        }

        // Example of lambda function in Java with arraylist.
//        ArrayList<Integer> my_new_arr = new ArrayList<>();
//        my_new_arr.add(1);
//        my_new_arr.add(10);
//        my_new_arr.add(100);
//        my_new_arr.add(122);
//        my_new_arr.add(134);
//
//        my_new_arr.forEach(integer -> {
//            System.out.printf("Hello there, the current element is %d\n", integer);
//        });


        //----- Prints Collection Summary using CollectionSummary Classes    -----//
        Optional<CollectionCycleCountsSummary> summary = machine.getAggregation(CollectionCycleCountsSummary.class);
        summary.ifPresent(s -> s.printOn(System.out));

//        System.out.printf("getInitialMarkCount %d\n", getInitialMarkCount());
//        System.out.printf("getRemarkCount %d\n", getRemarkCount());
//        System.out.printf("getDefNewCount %d\n", getDefNewCount());


        //----- Prints Pause Time Summary using PauseTime classes            -----//

        // Retrieves the Aggregation for PauseTimeSummary. This is a RuntimeAggregation.
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
