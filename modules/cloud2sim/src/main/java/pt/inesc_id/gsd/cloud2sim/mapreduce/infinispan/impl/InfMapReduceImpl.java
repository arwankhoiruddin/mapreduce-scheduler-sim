/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan.impl;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;
import pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan.InfJob;
import pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan.InfMapReduceTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;

/**
 * Infinispan based implementation of map-reduce simulation
 */
public class InfMapReduceImpl {
    private static InfJob infJob;
    private static int processedFiles = 0;

    /**
     * Initiate the map reduce simulation
     */
    public static void initiate(InfJob infJob1) {
        infJob = infJob1;
        try {
            fillMapWithData();

            Log.printConcatLine("Starting the Primary Map Reduce Job with size " + infJob.getSize());
            MapReduceTask<String, String, String, Long> t = InfMapReduceTask.getMapReduceTask();
            t.mappedWith(new WordCountMapper()).reducedWith(new WordCountReducer());
            Map<String, Long> wordCountMap = t.execute();
            if (ConfigReader.getIsVerbose()) {
                Log.printConcatLine("Counts per words over " + processedFiles + " files:");
                for (Map.Entry<String, Long> entry : wordCountMap.entrySet()) {
                    Log.printConcatLine("\tWord '" + entry.getKey() + "' occurred " + entry.getValue() + " times");
                }
            }
        } catch (Exception e) {
            Log.printConcatLine("Exception in starting the map reduce simulation with Infinispan. ", e);
        } finally {
            InfJob.printStatus();
            Cloud2SimEngine.shutdownLogs();
        }
    }

    private static void fillMapWithData()
            throws Exception {

        File folder = new File(MapReduceConstants.BULK_LOAD_FOLDER);
        Log.printConcatLine("Filling the map with data..");
        if (folder.listFiles() == null) {
            Log.printConcatLine("Empty load provided. Terminating the simulation.");
            return;
        }
        for (File file : folder.listFiles()) {
            String fileName = file.getName();
            InputStream is = new FileInputStream(MapReduceConstants.BULK_LOAD_FOLDER + File.separator + fileName);
            LineNumberReader reader = new LineNumberReader(new InputStreamReader(is));

            StringBuilder sb = new StringBuilder();
            String line;
            int lineNumber = 0;
            while (((line = reader.readLine()) != null) && (lineNumber < ConfigReader.getMapReduceSize())) {
                sb.append(line).append("\n");
                lineNumber++;
            }
            InfJob.getDefaultCache().put(fileName, sb.toString());

            is.close();
            reader.close();
            processedFiles++;
        }
    }
}
