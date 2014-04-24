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
import org.infinispan.Cache;
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.MapReduceTask;
import org.infinispan.distexec.mapreduce.Mapper;
import org.infinispan.distexec.mapreduce.Reducer;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

public class InfMapReduceSimulator {

    private static int jobSize = 10;
    private static Cache<String, String> defaultCache;

    /**
     * In this example replace c1 and c2 with
     * real Cache references
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {
        EmbeddedCacheManager manager = new DefaultCacheManager("conf/infinispan.xml");
        defaultCache = manager.getCache();

        fillMapWithData();


//        c2.put("2", "Infinispan rules the world");

        MapReduceTask<String, String, String, Integer> t =
                new MapReduceTask<String, String, String, Integer>(defaultCache);
        t.mappedWith(new WordCountMapper())
                .reducedWith(new WordCountReducer());
        Map<String, Integer> wordCountMap = t.execute();
    }

    static class WordCountMapper implements Mapper<String,String,String,Integer> {
        /** The serialVersionUID */
        private static final long serialVersionUID = -5943370243108735560L;

        @Override
        public void map(String key, String value, Collector<String, Integer> c) {
            StringTokenizer tokens = new StringTokenizer(value);
            while (tokens.hasMoreElements()) {
                String s = (String) tokens.nextElement();
                c.emit(s, 1);
            }
        }
    }

    static class WordCountReducer implements Reducer<String, Integer> {
        /** The serialVersionUID */
        private static final long serialVersionUID = 1901016598354633256L;

        @Override
        public Integer reduce(String key, Iterator<Integer> iter) {
            int sum = 0;
            while (iter.hasNext()) {
                Integer i = iter.next();
                sum += i;
            }
            return sum;
        }
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
            while (((line = reader.readLine()) != null) && (lineNumber < jobSize)) {
                sb.append(line).append("\n");
                lineNumber++;
            }
            defaultCache.putAsync(fileName, sb.toString());

            is.close();
            reader.close();
        }
    }
}