/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */
package org.cloudbus.cloudsim.compatibility.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.compatibility.Cloud2SimConstants;

/**
 * A singleton that integrates Hazelcast into Cloud2Sim and initiates Hazelcast.
 */
public class HazelSim {
    private static HazelSim hazelSim = null;
    protected static HazelcastInstance[] instances = new HazelcastInstance[0];

    /**
     * Protected constructor to avoid instantiation of the singleton class
     */
    protected HazelSim() {
    }

    /**
     * Creates a HazelSim object and initializes an array of Hazelcast instances.
     * @return the hazelsim object.
     */
    public static HazelSim getHazelSim() {
        if (hazelSim == null) {
            hazelSim = new HazelSim();
        }
        return hazelSim;
    }

    public static void spawnInstances(Config config, int instanceCount) {
        instances = new HazelcastInstance[instanceCount];
        for (int i = 0; i < instanceCount; i++) {
            instances[i] = Hazelcast.newHazelcastInstance(config);
        }
    }

    public static void spawnInstance(Config config) {
        instances = new HazelcastInstance[1];
        instances[0] = Hazelcast.newHazelcastInstance(config);
    }

    /**
     * Gets the compatibility instances.
     * @return the compatibility instances.
     */
    public HazelcastInstance[] getHazelcastInstances() {
        return instances;
    }

    /**
     * Map: cloudletId -> cloudlet execution finished time
     * @return the map
     */
    public IMap<Integer, Double> getCloudletFinishedTime() {
        return instances[Cloud2SimConstants.FIRST].getMap("finishedTime");
    }
}

