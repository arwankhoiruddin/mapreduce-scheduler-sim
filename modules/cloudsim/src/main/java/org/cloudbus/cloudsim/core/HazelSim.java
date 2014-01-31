/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */
package org.cloudbus.cloudsim.core;

import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.cloudbus.cloudsim.Log;
import java.io.FileNotFoundException;

/**
 * A singleton that integrates Hazelcast into Cloud2Sim and initiates Hazelcast.
 */
public class HazelSim {
    private static HazelSim hazelSim;
    private static Config cfg;
    private static HazelcastInstance instance;
    private static HazelcastInstance[] instances;

    /**
     * Protected constructor to avoid instantiation of the singleton class
     */
    protected HazelSim() {
        try {
            cfg = new FileSystemXmlConfig(Cloud2SimConstants.HAZELCAST_CONFIG_FILE);
        } catch (FileNotFoundException e) {
            Log.printConcatLine(Cloud2SimConstants.HAZELCAST_CONFIG_FILE_NOT_FOUND_ERROR);
            cfg = new Config();
        }
    }

    /**
     * Creates a HazelSim object and initializes a Hazelcast instance.
     * @return the hazelsim object.
     */
    public static HazelSim getHazelSim() {
        if (hazelSim == null) {
            hazelSim = new HazelSim();
        }
        instance = Hazelcast.newHazelcastInstance(cfg);
        return hazelSim;
    }

    /**
     * Creates a HazelSim object and initializes an array of Hazelcast instances.
     * @param instanceCount No. of Hazelcast instances.
     * @return the hazelsim object.
     */
    public static HazelSim getHazelSim(int instanceCount) {
        if (hazelSim == null) {
            hazelSim = new HazelSim();
        }
        instances = new HazelcastInstance[instanceCount];
        for (int i = 0; i < instanceCount; i++) {
            instances[i] = Hazelcast.newHazelcastInstance(cfg);
        }
        return hazelSim;
    }

    /**
     * Gets the hazelcast instance.
     * @return the hazelcast instance.
     */
    public HazelcastInstance getHazelcastInstance() {
        return instance;
    }

    /**
     * Gets the hazelcast instances.
     * @return the hazelcast instances.
     */
    public HazelcastInstance[] getHazelcastInstances() {
        return instances;
    }
}

