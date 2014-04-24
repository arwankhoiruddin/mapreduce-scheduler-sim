/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.infinispan.InfConfigReader;
import org.cloudbus.cloudsim.core.CloudSim;
import org.infinispan.Cache;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;

/**
 * Infinispan based implementation of map-reduce simulation
 */
public class MapReduceImpl {
    public static void startMapReduce(Cache defaultCache){
        CloudSim.setSimulationStartedTime(System.currentTimeMillis());

        Log.printConcatLine(InfConfigReader.getMapReduceSize());
        try {
            fillMapWithData(defaultCache);
//        c2.put("2", "Infinispan rules the world");

        MapReduceTask<String, String, String, Integer> t =
                new MapReduceTask<String, String, String, Integer>(defaultCache);
        t.mappedWith(new WordCountMapper()).reducedWith(new WordCountReducer());
        Map<String, Integer> wordCountMap = t.execute();

        } catch (Exception e) {
            Log.printConcatLine("Exception in starting the map reduce simulation with Infinispan", e);
        } finally {
            Cloud2SimEngine.shutdownLogs();
        }
    }

    private static void fillMapWithData(Cache defaultCache)
            throws Exception {

        File folder = new File(MapReduceConstants.LOAD_FOLDER);
        Log.printConcatLine("Filling the map with data..");
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
            while (((line = reader.readLine()) != null) && (lineNumber < InfConfigReader.getMapReduceSize())) {
                sb.append(line).append("\n");
                lineNumber++;
            }
            defaultCache.put(fileName, sb.toString());

            is.close();
            reader.close();
        }
    }
}
