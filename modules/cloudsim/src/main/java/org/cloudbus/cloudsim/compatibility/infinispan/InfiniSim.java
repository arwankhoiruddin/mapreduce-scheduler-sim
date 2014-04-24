/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.compatibility.infinispan;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.common.Cloud2SimConstants;
import org.infinispan.Cache;
import org.infinispan.atomic.AtomicMap;
import org.infinispan.atomic.AtomicMapLookup;
import org.infinispan.configuration.cache.Configuration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.transaction.TransactionMode;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

import java.io.IOException;

/**
 * The core class of Infinispan integration
 */
public class InfiniSim {
    private static InfiniSim infiniSim = null;
    private static Cache<String, String> defaultCache;
    private static AtomicMap<String, Long> defaultJobTracker;

    public Cache<String, String> getDefaultCache() {
        return defaultCache;
    }

    /**
     * Singleton. Prevents initialization from outside the class.
     *
     * @throws IOException, if getting the cache failed.
     */
    protected InfiniSim() throws IOException {
        DefaultCacheManager manager = new DefaultCacheManager(Cloud2SimConstants.INFINISPAN_CONFIG_FILE);
        defaultCache = manager.getCache(InfConstants.TRANSACTIONAL_CACHE);
        initializeDefaultJobTracker();
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
        defaultJobTracker.put(key, defaultJobTracker.get(key)+1);
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
     * Initializes Infinispan
     */
    public static InfiniSim getInfiniSim() {
        if (infiniSim == null) {
            try {
                infiniSim = new InfiniSim();
            } catch (IOException e) {
                Log.printConcatLine("Exception when trying to initialize Infinispan", e);
            }
        }
        return infiniSim;
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
