/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.impl;

import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.HzJob;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;
import pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.HzMapReduceParams;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;

/**
 * The implementation class of map-reduce based on the hazelcast map-redeuce word-count sample,
 * calling the other map reduce implementation classes.
 */
public class HzMapReduceImpl {
    private static HzJob hzJob;
    private static int processedFiles = 0;

    /**
     * Initiate the map reduce simulation with Hazelcast
     */
    public static void initiate(HzJob hzJob1) {
        hzJob = hzJob1;
        try {
            fillMapWithData();

            Map<String, Long> countsPerWord = mapReduce();

            if (HzConfigReader.getIsVerbose()) {
                Log.printConcatLine("Counts per words over " + processedFiles + " files:");
                for (Map.Entry<String, Long> entry : countsPerWord.entrySet()) {
                    Log.printConcatLine("\tWord '" + entry.getKey() + "' occurred " + entry.getValue() + " times");
                }
            }

            Log.printConcatLine("Printing the params of the primary map reduce job..");
            HzMapReduceParams.printJobStatus();
            HzMapReduceParams.printStatus();
            Cloud2SimEngine.shutdownLogs();

            long wordCount = mapReduceCollate();
            Log.printConcatLine("All content sums up to " + wordCount + " words.");
        } catch (Exception e) {
            Log.printConcatLine("Exception in starting the map reduce simulation with Hazelcast", e);
        } finally {
            Log.printConcatLine("Printing the params of the collation map reduce job..");
            HzMapReduceParams.printJobStatus();
            HzMapReduceParams.printStatus();
            Cloud2SimEngine.shutdown();
        }
    }

    private static Map<String, Long> mapReduce()
            throws Exception {
        Log.printConcatLine("Starting the Primary Map Reduce Job with size " + hzJob.getSize());
        Job<String, String> job = hzJob.getJob();

        Log.printConcatLine("*** Starting the primary map reduce operations..");
        // Creating a new Job
        ICompletableFuture<Map<String, Long>> future = job // returned future
                .mapper(new WordCountMapper())             // adding a mapper
                .combiner(new WordCountCombinerFactory())  // adding a combiner through the factory
                .reducer(new WordCountReducerFactory())    // adding a reducer through the factory
                .submit();                                 // submit the task

        // Attach a callback listener
        future.andThen(buildCallback());

        Log.printConcatLine("Completing the primary map reduce task with size " + hzJob.getSize());
        // Wait and retrieve the result
        return future.get();
    }

    private static long mapReduceCollate()
            throws Exception {
        Log.printConcatLine("Starting the Collation Map Reduce Job");
        Job<String, String> job = hzJob.getJob();
        HzMapReduceParams.initJobParams();

        Log.printConcatLine("*** Starting the map reduce operations for collation..");
        ICompletableFuture<Long> future = job // returned future
                .mapper(new WordCountMapper())             // adding a mapper
                .combiner(new WordCountCombinerFactory())  // adding a combiner through the factory
                .reducer(new WordCountReducerFactory())    // adding a reducer through the factory
                .submit(new WordCountCollator());          // submit the task and supply a collator

        // Wait and retrieve the result
        return future.get();
    }

    private static ExecutionCallback<Map<String, Long>> buildCallback() {
        return new ExecutionCallback<Map<String, Long>>() {
            @Override
            public void onResponse(Map<String, Long> stringLongMap) {
                Log.printConcatLine("Calculation finished.");
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        };
    }

    private static void fillMapWithData()
            throws Exception {

        File folder = new File(MapReduceConstants.LOAD_FOLDER);
        Log.printConcatLine("Filling the map with data..");
        IMap<String, String> map = HzJob.getHazelcastInstance().getMap(MapReduceConstants.DEFAULT_KEY_VALUE_STORE);
        if (folder.listFiles() == null) {
            Log.printConcatLine("Empty load provided. Terminating the simulation.");
            return;
        }
        for (File file : folder.listFiles()) {
            String fileName = file.getName();
            InputStream is = new FileInputStream(MapReduceConstants.LOAD_FOLDER + File.separator + fileName) ;
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String line;
            int lineNumber = 0;
            while (((line = reader.readLine()) != null) && (lineNumber < hzJob.getSize())) {
                sb.append(line).append("\n");
                lineNumber++;
            }
            map.put(fileName, sb.toString());

            is.close();
            reader.close();
            processedFiles++;
        }
    }
}
