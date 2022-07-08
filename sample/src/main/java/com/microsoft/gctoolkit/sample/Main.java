package com.microsoft.gctoolkit.sample;

import com.microsoft.gctoolkit.GCToolKit;
import com.microsoft.gctoolkit.io.GCLogFile;
import com.microsoft.gctoolkit.io.SingleGCLogFile;
import com.microsoft.gctoolkit.jvm.JavaVirtualMachine;
import com.microsoft.gctoolkit.sample.aggregation.CMSTimeSummaryAggregation;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {

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

        machine.getAggregation(CMSTimeSummaryAggregation.class).ifPresent(cmsTimeSummaryAggregation -> {
            System.out.printf("Total Time                  : %s sec\n", cmsTimeSummaryAggregation.total_duration);
            System.out.printf("Minimum duration:           :%s sec\n", cmsTimeSummaryAggregation.minEventTime);
            System.out.printf("Maximum duration:           :%s sec\n", cmsTimeSummaryAggregation.maxEventTime);
        });

    }


}
