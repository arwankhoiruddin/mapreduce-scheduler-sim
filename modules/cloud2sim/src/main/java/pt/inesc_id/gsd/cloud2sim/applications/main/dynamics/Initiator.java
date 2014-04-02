/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.applications.main.dynamics;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSim;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

/**
 * An empty hazelcast instance
 */
public class Initiator {
    public static void main(String[] args) {
        initInstance();
    }

    /**
     * Initiates the empty hazelcast instances
     */
    public static void initInstance() {
        Log.printConcatLine("Initiating a Hazelcast instance.");
        HzConfigReader.readConfig();
        HazelSim.spawnInstance(HazelSimCore.getCfg());
        int size = HzObjectCollection.getHzObjectCollection().getFirstInstance().getCluster().getMembers().size(); //this is not working.
        Log.printConcatLine("Size of the instances in this cluster: " + size);
    }
}
