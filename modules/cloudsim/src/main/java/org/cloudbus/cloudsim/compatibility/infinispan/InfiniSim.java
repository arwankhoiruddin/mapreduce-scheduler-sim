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
import pt.inesc_id.gsd.cloud2sim.infinispan.DistrInfiniSim;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The core class of Infinispan integration
 */
public class InfiniSim {
    private static InfiniSim infiniSim = null;
    protected static Cache<String, String> defaultCache;
    public static AtomicInteger mapInvocations;
    public static AtomicInteger reduceInvocations;
    public static AtomicInteger combineInvocations;
    public static AtomicInteger mappersOfTheJob;
    public static AtomicInteger reducersOfTheJob;
    public static AtomicInteger combinersOfTheJob;
    public static AtomicInteger numberOfMappers;
    public static AtomicInteger numberOfReducers;
    public static AtomicInteger numberOfCombiners;

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
        numberOfMappers = new AtomicInteger(0);
        numberOfCombiners = new AtomicInteger(0);
        numberOfReducers = new AtomicInteger(0);
        initializeThisJob();
    }

    public static void initializeThisJob() {
        initializeMethodInvocations();
        initMapperReducerCombiners();
    }

    public static void initMapperReducerCombiners() {
        mappersOfTheJob = new AtomicInteger(0);
        combinersOfTheJob = new AtomicInteger(0);
        reducersOfTheJob = new AtomicInteger(0);
    }

    public static void initializeMethodInvocations() {
        mapInvocations = new AtomicInteger(0);
        reduceInvocations = new AtomicInteger(0);
        combineInvocations = new AtomicInteger(0);
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
}
