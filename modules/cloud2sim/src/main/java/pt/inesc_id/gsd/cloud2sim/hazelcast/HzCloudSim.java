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
import org.cloudbus.cloudsim.compatibility.ConfigReader;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConstants;
import pt.inesc_id.gsd.cloud2sim.util.AppBuilder;
import pt.inesc_id.gsd.cloud2sim.util.AppUtil;
import org.cloudbus.cloudsim.core.CloudSim;

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
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");
        HazelSimCore hazelSimCore = HazelSimCore.getHazelSimCore(ConfigReader.getSimultaneousInstances());

        initInstances();

        CloudSim.init(numUser, cal, traceFlag);
        AppBuilder.initWorkers(offset);
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
                put((long) offset, HzConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
    }
}
