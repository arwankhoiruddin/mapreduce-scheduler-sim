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

public class HazelSim {
    public static void initHazelcast() {
        Config cfg;
        try {
            cfg = new FileSystemXmlConfig("conf/hazelcast.xml");
        } catch (FileNotFoundException e) {
            Log.printConcatLine("Hazelcast Configuration File not found. Using the default.");
            cfg = new Config();
        }
        HazelcastInstance instance = Hazelcast.newHazelcastInstance(cfg);
    }
}

