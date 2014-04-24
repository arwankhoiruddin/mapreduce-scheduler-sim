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
import org.infinispan.manager.DefaultCacheManager;

import java.io.IOException;

/**
 * The core class of Infinispan integration
 */
public class InfiniSim {
    private static InfiniSim infiniSim = null;
    private Cache<String, String> defaultCache;

    public Cache<String, String> getDefaultCache() {
        return defaultCache;
    }

    /**
     * Singleton. Prevents initialization from outside the class.
     * @throws IOException, if getting the cache failed.
     */
    protected InfiniSim() throws IOException {
        DefaultCacheManager manager = new DefaultCacheManager(Cloud2SimConstants.INFINISPAN_CONFIG_FILE);
        defaultCache = manager.getCache();
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
