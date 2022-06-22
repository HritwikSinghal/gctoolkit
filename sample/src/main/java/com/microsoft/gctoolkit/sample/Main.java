package com.microsoft.gctoolkit.sample;

import com.microsoft.gctoolkit.GCToolKit;
import com.microsoft.gctoolkit.io.GCLogFile;
import com.microsoft.gctoolkit.io.SingleGCLogFile;
import com.microsoft.gctoolkit.jvm.JavaVirtualMachine;
import com.microsoft.gctoolkit.sample.aggregation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class Main {

    private GCStats full_gc_stats = new GCStats();
    private int initialMarkCount = 0;
    private int remarkCount = 0;
    private int defNewCount = 0;

    private double throughput = 0.00;
    private Path GCLogFileProcessed_path;

    public int getInitialMarkCount() {
        return initialMarkCount;
    }

    public int getRemarkCount() {
        return remarkCount;
    }

    public int getDefNewCount() {
        return defNewCount;
    }

    public double getThroughput() {
        return throughput;
    }

    public void write_to_file(Path fileName, String text) {
        try {
            Files.writeString(fileName, text, StandardOpenOption.APPEND);
            System.out.print(text);
        } catch (IOException e) {
            System.out.println("Exiting since Cant write to file.");
            System.exit(0);
        }
    }

    public String read_from_file(Path fileName, String text) throws IOException {
        // Reading the content of the file
        return Files.readString(fileName);
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

    public void analyze(String gcLogFile) throws IOException {
        /*
          GC log files can come in  one of two types: single or series of rolling logs.
          In this sample, we load a single log file.
          The log files can be either in text, zip, or gzip format.
         */
        GCLogFileProcessed_path = Paths.get(gcLogFile + ".processed");
        GCLogFileProcessed_path.toFile().delete();
        GCLogFileProcessed_path.toFile().createNewFile();

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

        machine.getAggregation(HeapOccupancyAfterCollectionSummaryAggregation.class)
                .map(HeapOccupancyAfterCollectionSummaryAggregation::get).ifPresent(summary -> {
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

//        // 'aggregation' is 'HeapOccupancyAfterCollectionSummary' object but wrapped in 'Optional' keyword, so we have some
//        // default functions like 'map' (map() applies the Function argument to the value, then returns the result wrapped in an Optional).
//        Optional<HeapOccupancyAfterCollectionSummaryAggregation> aggregation = machine.getAggregation(HeapOccupancyAfterCollectionSummaryAggregation.class);
//        // The "HeapOccupancyAfterCollectionSummary::get" will return a 'Map<GarbageCollectionTypes, XYDataSet>' wrapped in 'Optional'
//        // and we are using "map" function of "Optional" to convert one type to another.
//        Optional<Map<GarbageCollectionTypes, XYDataSet>> data_from_aggregation = aggregation.map(HeapOccupancyAfterCollectionSummaryAggregation::get);
//        // summary is of type "Map<GarbageCollectionTypes, XYDataSet>"
//        data_from_aggregation.ifPresent(summary -> {
//            summary.forEach((gcType, dataSet) -> {
//                System.out.printf(message, gcType, dataSet.size());
//                switch (gcType) {
//                    case DefNew:
//                        defNewCount = dataSet.size();
//                        break;
//                    case InitialMark:
//                        initialMarkCount = dataSet.size();
//                        break;
//                    case Remark:
//                        remarkCount = dataSet.size();
//                        break;
//                    default:
//                        System.out.println(gcType + " not managed");
//                        break;
//                }
//            });
//        });


        //--------------------------------------------------------------------------------//
        //-----         Prints Collection Summary using CollectionCycleCounts Classes    -----//
        //--------------------------------------------------------------------------------//
        System.out.println("-----         Prints Collection Summary using CollectionCycleCounts Classes    -----");

        // summary is of type CollectionCycleCountsSummary bcoz it's from that aggregation --^^^^
        machine.getAggregation(CollectionCycleCountsSummaryAggregation.class).ifPresent(s -> {
            s.printOn(System.out);
        });

        //--------------------------------------------------------------------------------//
        //-----         Prints Pause Time Summary using PauseTime classes            -----//
        //--------------------------------------------------------------------------------//
        System.out.println("-----         Prints Pause Time Summary using PauseTime classes    -----");

        // machine.getAggregation(PauseTimeSummary.class) is of type PauseTimeSummary in Optional.
        // Retrieves the Aggregation for PauseTimeSummary. This is a RuntimeAggregation.

        machine.getAggregation(PauseTimeSummaryAggregation.class).ifPresent(pauseTimeSummary -> {
            System.out.printf("Total pause time                  : %.2f sec\n", pauseTimeSummary.getTotalPauseTime());
            System.out.printf("Total run time for the program    : %.2f sec\n", pauseTimeSummary.getRuntimeDuration());
            System.out.printf("Percent pause time                : %.3f %%\n", pauseTimeSummary.getPercentPaused());
            System.out.printf("Percent Throughput                : %.4f %%\n", pauseTimeSummary.getThroughput());
            throughput = pauseTimeSummary.getThroughput();
        });

//        Optional<PauseTimeSummaryAggregation> my_pause_time_aggregation = machine.getAggregation(PauseTimeSummaryAggregation.class);
//        // pauseTimeSummary is of type PauseTimeSummary.
//        my_pause_time_aggregation.ifPresent(pauseTimeSummaryAggregation -> {
//            System.out.printf("Total pause time                  : %.2f sec\n", pauseTimeSummaryAggregation.getTotalPauseTime());
//            System.out.printf("Total run time for the program    : %.2f sec\n", pauseTimeSummaryAggregation.getRuntimeDuration());
//            System.out.printf("Percent pause time                : %.3f %%\n", pauseTimeSummaryAggregation.getPercentPaused());
//            System.out.printf("Percent Throughput                : %.4f %%\n", pauseTimeSummaryAggregation.getThroughput());
//        });

        //--------------------------------------------------------------------------------//
        //-----         Prints CMS Time Summary using CMSTime classes            -----//
        //--------------------------------------------------------------------------------//
        System.out.println("-----         Prints CMS Time Summary using CMSTime classes    -----");

        machine.getAggregation(CMSTimeSummaryAggregation.class).ifPresent(cmsTimeSummaryAggregation -> {
            System.out.printf("Total Initial Mark Time                  : %s sec\n", cmsTimeSummaryAggregation.getTotalEventTime());
            System.out.printf("Minimum initial mark duration:           :%s sec\n", cmsTimeSummaryAggregation.getMinEventTime());
            System.out.printf("Maximum initial mark duration:           :%s sec\n", cmsTimeSummaryAggregation.getMaxEventTime());
        });

        //--------------------------------------------------------------------------------//
        //-----         Prints Full GC Time Summary             -----//
        //--------------------------------------------------------------------------------//
        System.out.println("-----         Prints Full GC Summary    -----");

        machine.getAggregation(FullGCAggregationSummary.class).ifPresent(fullGCAggregationSummary -> {

            System.out.println();
            System.out.println("Total Pause Time for GC cause");
            fullGCAggregationSummary.get_GCCause_total_pause_time_summary().forEach((gcCause, aDouble) -> {
                System.out.printf("%s = %f sec\n", gcCause, aDouble);
            });

            System.out.println();
            System.out.println("MAX Duration for GC cause");
            fullGCAggregationSummary.get_GCCause_max_PauseTime_duration_summary().forEach((gc_cause, duration) -> {
                System.out.printf("%s: %f sec\n", gc_cause, duration);
            });

            System.out.println();
            System.out.println("GC Cause Total Count");
            fullGCAggregationSummary.get_GCCause_total_count_summary().forEach((gcCause, integer) -> {
                System.out.printf("%s count = %d\n", gcCause, integer);
            });

            System.out.println();
            System.out.println("GC Type Total Count");
            fullGCAggregationSummary.get_GCType_total_count_summary().forEach((gc_type, count) -> {
                System.out.printf("Total count of %s = %d\n", gc_type, count);
            });

        });

        // -------------------------------------------------------------------------------------------------- //
        // Write to file from below this line.
        // -------------------------------------------------------------------------------------------------- //

        machine.getAggregation(FullGCAggregationSummary.class).ifPresent(fullGCAggregationSummary -> {
            write_to_file(GCLogFileProcessed_path, "\n");
            write_to_file(GCLogFileProcessed_path, "============= Key Performance Indicators =============\n");
            write_to_file(GCLogFileProcessed_path, String.format("Throughput: %f %%\n", throughput));
            write_to_file(GCLogFileProcessed_path, String.format("Max Pause time : %f sec\n", fullGCAggregationSummary.get_MaxPauseTime()));
        });
    }

    public GCStats getFull_gc_stats() {
        return full_gc_stats;
    }
}
