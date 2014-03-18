/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.examples.hazelcast.instances;

import com.hazelcast.core.HazelcastInstance;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.hazelcast.HazelSim;

import java.util.Map;

public class ObjectCollection {
    protected static HazelcastInstance[] instances;

    // To prevent instantiation
    private ObjectCollection(){}

    public static void init() {
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");

        HazelSim hazelSim = HazelSim.getHazelSim(HazelSimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
        instances = hazelSim.getHazelcastInstances();
    }

    /**
     * Gets the list of cloudlets created by the user.
     *
     * @return the list of cloudlets submitted by the users
     */
    @SuppressWarnings("unchecked")
    public static Map<Integer, String> getList() {
        return instances[0].getMap("userTestMap");
    }

    /**
     * Gets the list of cloudlets created by the user.
     *
     * @return the list of cloudlets submitted by the users
     */
    @SuppressWarnings("unchecked")
    public static Map<Integer, String> getTempList() {
        return instances[1].getMap("userTempMap");
    }

}
