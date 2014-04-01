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

import java.util.ArrayList;
import java.util.List;

/**
 * A singleton that integrates Hazelcast into Cloud2Sim and initiates Hazelcast.
 */
public class HazelSim {
    private static HazelSim hazelSim = null;
    protected static List<HazelcastInstance> instances = new ArrayList<>();

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
        for (int i = 0; i < instanceCount; i++) {
            instances.add(Hazelcast.newHazelcastInstance(config));
        }
    }

    public static void spawnInstance(Config config) {
        instances.add(Hazelcast.newHazelcastInstance(config));
    }

    /**
     * Gets the compatibility instances.
     * @return the compatibility instances.
     */
    public List<HazelcastInstance> getHazelcastInstances() {
        return instances;
    }

    public HazelcastInstance getFirstInstance() {
        return getNthInstance(HzConstants.FIRST);
    }

    public HazelcastInstance getLastInstance() {
        return getNthInstance(HzConstants.LAST);
    }

    public HazelcastInstance getNthInstance(int i) {
        while (instances.size() <= i) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return instances.get(i);
    }

    /**
     * Map: cloudletId -> cloudlet execution finished time
     * @return the map
     */
    public IMap<Integer, Double> getCloudletFinishedTime() {
        return instances.get(HzConstants.FIRST).getMap("finishedTime");
    }
}

