/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.impl;

import com.hazelcast.core.ExecutionCallback;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.ICompletableFuture;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import pt.inesc_id.gsd.cloud2sim.mapreduce.MapReduceSimulator;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;

/**
 * The implementation class of map-reduce based on the hazelcast map-redeuce word-count sample,
 * calling the other map reduce implementation classes.
 */
public class MapReduceCore {
    private static final String[] DATA_RESOURCES_TO_LOAD = {"text1.txt", "text2.txt", "text3.txt"};

    /**
     * Initiate the map reduce simulation
     * @param hazelcastInstance, hazelcast instance to simulate the map-reduce application
     * @throws Exception, if the simulation failed.
     */
    public static void initiate(HazelcastInstance hazelcastInstance) throws Exception {
        try {
            fillMapWithData(hazelcastInstance);

            Map<String, Long> countsPerWord = mapReduce(hazelcastInstance);

            if (HzConfigReader.getIsVerbose()) {
                Log.printConcatLine("Counts per words over " + DATA_RESOURCES_TO_LOAD.length + " files:");
                for (Map.Entry<String, Long> entry : countsPerWord.entrySet()) {
                    Log.printConcatLine("\tWord '" + entry.getKey() + "' occurred " + entry.getValue() + " times");
                }
            }

            long wordCount = mapReduceCollate(hazelcastInstance);
            Log.printConcatLine("All content sums up to " + wordCount + " words.");

        } finally {
            Cloud2SimEngine.shutdown();
        }
    }

    private static Map<String, Long> mapReduce(HazelcastInstance hazelcastInstance)
            throws Exception {
        Log.printConcatLine("Starting the Map Reduce Job with size " + HzConfigReader.getMapReduceSize());

        // Retrieving the JobTracker by name
        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");

        // Creating the KeyValueSource for a Hazelcast IMap
        IMap<String, String> map = hazelcastInstance.getMap("articles");
        KeyValueSource<String, String> source = KeyValueSource.fromMap(map);

        Job<String, String> job = jobTracker.newJob(source);

        // Creating a new Job
        ICompletableFuture<Map<String, Long>> future = job // returned future
                .mapper(new TokenizerMapper())             // adding a mapper
                .combiner(new WordCountCombinerFactory())  // adding a combiner through the factory
                .reducer(new WordCountReducerFactory())    // adding a reducer through the factory
                .submit();                                 // submit the task

        // Attach a callback listener
        future.andThen(buildCallback());

        // Wait and retrieve the result
        return future.get();
    }

    private static long mapReduceCollate(HazelcastInstance hazelcastInstance)
            throws Exception {

        // Retrieving the JobTracker by name
        JobTracker jobTracker = hazelcastInstance.getJobTracker("default");

        // Creating the KeyValueSource for a Hazelcast IMap
        IMap<String, String> map = hazelcastInstance.getMap("articles");
        KeyValueSource<String, String> source = KeyValueSource.fromMap(map);

        // Creating a new Job
        Job<String, String> job = jobTracker.newJob(source);

        ICompletableFuture<Long> future = job // returned future
                .mapper(new TokenizerMapper())             // adding a mapper
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

    private static void fillMapWithData(HazelcastInstance hazelcastInstance)
            throws Exception {

        IMap<String, String> map = hazelcastInstance.getMap("articles");
        for (String file : DATA_RESOURCES_TO_LOAD) {
            InputStream is = MapReduceSimulator.class.getResourceAsStream("conf/" + file);
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
                lineNumber++;
                if (lineNumber == HzConfigReader.getMapReduceSize()) {
                    break;
                }
            }
            map.put(file, sb.toString());

            is.close();
            reader.close();
        }
    }
}
