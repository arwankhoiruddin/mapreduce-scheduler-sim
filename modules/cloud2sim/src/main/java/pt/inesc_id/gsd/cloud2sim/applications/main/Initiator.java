/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.applications.main;

import com.hazelcast.core.HazelcastInstance;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.Cloud2SimConstants;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;

public class Initiator {
    protected static HazelcastInstance[] instances;

    public static void main(String[] args) {
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");
        init();
    }

    public static void init() {
        HazelSimCore hazelSimCore = HazelSimCore.getHazelSim(Cloud2SimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
        instances = hazelSimCore.getHazelcastInstances();
    }

}
