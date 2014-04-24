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
import org.infinispan.Cache;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.manager.DefaultCacheManager;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Map;

public class InfMapReduceSimulator {

    private static Cache<String, String> defaultCache;

    /**
     * In this example replace c1 and c2 with
     * real Cache references
     *
     */
    public static void main(String[] args) throws Exception {
        simulateMapReduce();
    }

    private static void simulateMapReduce() throws Exception {
        Cloud2SimEngine.startInfinispan();
        Log.printLine("# Starting the Infinispan Map Reduce Simulator...");

        DefaultCacheManager manager = new DefaultCacheManager("conf/infinispan.xml");
        defaultCache = manager.getCache();

        Log.printConcatLine(InfConfigReader.getMapReduceSize());
        fillMapWithData();


//        c2.put("2", "Infinispan rules the world");

        MapReduceTask<String, String, String, Integer> t =
                new MapReduceTask<String, String, String, Integer>(defaultCache);
        t.mappedWith(new WordCountMapper())
                .reducedWith(new WordCountReducer());
        Map<String, Integer> wordCountMap = t.execute();
    }


    private static void fillMapWithData()
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
            defaultCache.putAsync(fileName, sb.toString());

            is.close();
            reader.close();
        }
    }
}