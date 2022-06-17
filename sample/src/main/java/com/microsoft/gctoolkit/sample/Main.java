package com.microsoft.gctoolkit.sample;

import com.microsoft.gctoolkit.GCToolKit;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.io.GCLogFile;
import com.microsoft.gctoolkit.io.SingleGCLogFile;
import com.microsoft.gctoolkit.jvm.JavaVirtualMachine;
import com.microsoft.gctoolkit.sample.aggregation.*;
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
        /*
          GC log files can come in  one of two types: single or series of rolling logs.
          In this sample, we load a single log file.
          The log files can be either in text, zip, or gzip format.
         */
        GCLogFile logFile = new SingleGCLogFile(Path.of(gcLogFile));
        GCToolKit gcToolKit = new GCToolKit();

        /*
         * This call will load all implementations of Aggregator that have been declared in module-info.java.
         * This mechanism makes use of Module SPI.
         */
        gcToolKit.loadAggregationsFromServiceLoader();

        /*
         * The JavaVirtualMachine contains the aggregations as filled out by the Aggregators.
         * It also contains configuration information about how the JVM was configured for the runtime.
         */
        JavaVirtualMachine machine = gcToolKit.analyze(logFile);


        // Retrieves the Aggregation for HeapOccupancyAfterCollectionSummary. This is a time-series aggregation.
        String message = "The XYDataSet for %s contains %s items.\n";


        //--------------------------------------------------------------------------------//
        //-----      Prints Heap Collection Summary using HeapCollection Classes     -----//
        //--------------------------------------------------------------------------------//
        System.out.println("-----         Prints Heap Collection Summary using HeapCollection Classes    -----");

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
        // default functions like 'map' (map() applies the Function argument to the value, then returns the result wrapped in an Optional).
        Optional<HeapOccupancyAfterCollectionSummaryAggregation> aggregation = machine.getAggregation(HeapOccupancyAfterCollectionSummaryAggregation.class);


        //----------------------------------------//
        //  Some Testing, Pls ignore. Thank you!  //
//        aggregation.ifPresent(x -> {
//            System.out.println("HELLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLOOOOOOOOOOOOOOOOOOO");
//            Map<GarbageCollectionTypes, XYDataSet> aggregations = x.get();
//            aggregations.forEach((aaa, bbb) -> {
//                System.out.printf("%s ||||\n--\n", aaa);
//                List<XYDataSet.Point> my_list = bbb.getItems();
//                my_list.forEach(System.out::println);
//            });
//        });

        //----------------------------------------//


        // The "HeapOccupancyAfterCollectionSummary::get" will return a 'Map<GarbageCollectionTypes, XYDataSet>' wrapped in 'Optional'
        // and we are using "map" function of "Optional" to convert one type to another.
        Optional<Map<GarbageCollectionTypes, XYDataSet>> data_from_aggregation = aggregation.map(HeapOccupancyAfterCollectionSummaryAggregation::get);

//      summary is of type "Map<GarbageCollectionTypes, XYDataSet>"
        data_from_aggregation.ifPresent(summary -> {
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
        });


        //--------------------------------------------------------------------------------//
        //-----         Prints Collection Summary using CollectionCycleCounts Classes    -----//
        //--------------------------------------------------------------------------------//
        System.out.println("-----         Prints Collection Summary using CollectionCycleCounts Classes    -----");

        Optional<CollectionCycleCountsSummaryAggregation> summary = machine.getAggregation(CollectionCycleCountsSummaryAggregation.class);
        // summary is of type CollectionCycleCountsSummary bcoz it's from that aggregation --^^^^
        summary.ifPresent(s -> s.printOn(System.out));

//        System.out.printf("getInitialMarkCount %d\n", getInitialMarkCount());
//        System.out.printf("getRemarkCount %d\n", getRemarkCount());
//        System.out.printf("getDefNewCount %d\n", getDefNewCount());


        //--------------------------------------------------------------------------------//
        //-----         Prints Pause Time Summary using PauseTime classes            -----//
        //--------------------------------------------------------------------------------//
        System.out.println("-----         Prints Pause Time Summary using PauseTime classes    -----");

        // machine.getAggregation(PauseTimeSummary.class) is of type PauseTimeSummary in Optional.
        // Retrieves the Aggregation for PauseTimeSummary. This is a RuntimeAggregation.

//        machine.getAggregation(PauseTimeSummary.class).ifPresent(pauseTimeSummary -> {
//            System.out.printf("Total pause time                  : %.2f sec\n", pauseTimeSummary.getTotalPauseTime());
//            System.out.printf("Total run time for the program    : %.2f sec\n", pauseTimeSummary.getRuntimeDuration());
//            System.out.printf("Percent pause time                : %.3f %%\n", pauseTimeSummary.getPercentPaused());
//            System.out.printf("Percent Throughput                : %.4f %%\n", pauseTimeSummary.getThroughput());
//        });

        Optional<PauseTimeSummaryAggregation> my_pause_time_aggregation = machine.getAggregation(PauseTimeSummaryAggregation.class);
        // pauseTimeSummary is of type PauseTimeSummary.
        my_pause_time_aggregation.ifPresent(pauseTimeSummaryAggregation -> {
            System.out.printf("Total pause time                  : %.2f sec\n", pauseTimeSummaryAggregation.getTotalPauseTime());
            System.out.printf("Total run time for the program    : %.2f sec\n", pauseTimeSummaryAggregation.getRuntimeDuration());
            System.out.printf("Percent pause time                : %.3f %%\n", pauseTimeSummaryAggregation.getPercentPaused());
            System.out.printf("Percent Throughput                : %.4f %%\n", pauseTimeSummaryAggregation.getThroughput());
        });


        //--------------------------------------------------------------------------------//
        //-----         Prints CMS Time Summary using CMSTime classes            -----//
        //--------------------------------------------------------------------------------//
        System.out.println("-----         Prints CMS Time Summary using CMSTime classes    -----");

        Optional<CMSTimeSummaryAggregation> my_cms_time_aggregation = machine.getAggregation(CMSTimeSummaryAggregation.class);
        my_cms_time_aggregation.ifPresent(cmsTimeSummaryAggregation -> {
            System.out.printf("Total Initial Mark Time                  : %f sec\n", cmsTimeSummaryAggregation.getTotalInitialMarkTime());
            System.out.printf("Minimum initial mark duration:           : %f sec\n", cmsTimeSummaryAggregation.getMinInitialMarkTime());
            System.out.printf("Maximum initial mark duration:           : %f sec\n", cmsTimeSummaryAggregation.getMaxInitialMarkTime());
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
