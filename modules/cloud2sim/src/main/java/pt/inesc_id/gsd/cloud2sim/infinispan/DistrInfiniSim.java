/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.infinispan;

import org.cloudbus.cloudsim.compatibility.infinispan.InfiniSim;
import org.infinispan.atomic.AtomicMap;
import org.infinispan.atomic.AtomicMapLookup;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

import java.io.IOException;

public class DistrInfiniSim extends InfiniSim {
    private static AtomicMap<String, Long> defaultJobTracker;

    /**
     * Singleton. Prevents initialization from outside the class.
     *
     * @throws java.io.IOException, if getting the cache failed.
     */
    protected DistrInfiniSim() throws IOException {
    }

    private static void initializeDefaultJobTracker() {
        defaultJobTracker = getAtomicMap(MapReduceConstants.DEFAULT_JOB_TRACKER);
        defaultJobTracker.put(MapReduceConstants.MAPPERS_FLAG, 0L);
        defaultJobTracker.put(MapReduceConstants.REDUCERS_FLAG, 0L);
        defaultJobTracker.put(MapReduceConstants.COMBINERS_FLAG, 0L);
        initializeThisJob();
    }

    /**
     * Initializes the current job.
     */
    public static void initializeThisJob() {
        defaultJobTracker.put(MapReduceConstants.THIS_MAPPERS_FLAG, 0L);
        defaultJobTracker.put(MapReduceConstants.THIS_REDUCERS_FLAG, 0L);
        defaultJobTracker.put(MapReduceConstants.THIS_COMBINERS_FLAG, 0L);
    }

    /**
     * Increments the value of a given flag
     * @param key, name of the flag
     */
    public static void incrementFlagAtomically(String key) {
        defaultJobTracker.put(key, (defaultJobTracker.get(key)+1));
    }

    /**
     * Gets the value
     * @param key name of the flag
     * @return the value
     */
    public static Long getValue(String key) {
        return defaultJobTracker.get(key);
    }

    /**
     * Gets an atomic distributed infinispan map
     *
     * @param mapKey the key of the map
     * @return the atomic map
     */
    public static AtomicMap<String, Long> getAtomicMap(String mapKey) {
        while(defaultCache == null) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("sleeping...");
        }
        return AtomicMapLookup.getAtomicMap(defaultCache, mapKey);
    }
}
