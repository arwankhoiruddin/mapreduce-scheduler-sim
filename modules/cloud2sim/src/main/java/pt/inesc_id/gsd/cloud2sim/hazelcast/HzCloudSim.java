/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.hazelcast;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConstants;
import org.cloudbus.cloudsim.core.CloudSim;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;

import java.util.Calendar;

/**
 * Extending CloudSim class to use in a distributed environment with hazelcast.
 */
public class HzCloudSim extends CloudSim {
    private static int offset;

    /**
     * Initiate Cloud2Sim/Hazelcast
     *
     * @param numUser,   number of users
     * @param cal,       calendar object
     * @param traceFlag, boolean
     */
    public static void init(int numUser, Calendar cal, boolean traceFlag) {
        HazelSimCore hazelSimCore = HazelSimCore.getHazelSimCore(ConfigReader.getSimultaneousInstances());
        initInstances(numUser, cal, traceFlag);
    }

    /**
     * Initiate Cloud2Sim/Hazelcast
     *
     * @param numUser,   number of users
     * @param cal,       calendar object
     * @param traceFlag, boolean
     */
    public static void init(int numUser, Calendar cal, boolean traceFlag, String clusterGroup) {
        HazelSimCore hazelSimCore = HazelSimCore.getHazelSimCore(ConfigReader.getSimultaneousInstances(), clusterGroup);
        initInstances(numUser, cal, traceFlag);
    }

    private static void initInstances(int numUser, Calendar cal, boolean traceFlag) {
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");
        initInstances();

        CloudSim.init(numUser, cal, traceFlag);
        Cloud2SimEngine.initWorkers(offset);
    }

    /**
     * Gets the offset value for the distributed objects
     *
     * @return offset
     */
    public static int getOffset() {
        return offset;
    }

    /**
     * Sets the offset value for the distributed objects, based on the deployment size
     */
    public static void initInstances() {
        HzObjectCollection objectCollection = HzObjectCollection.getHzObjectCollection();
        offset = objectCollection.getDeploymentInformation().size();
        objectCollection.getDeploymentInformation().
                put(objectCollection.getId(), HzConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
    }
}
