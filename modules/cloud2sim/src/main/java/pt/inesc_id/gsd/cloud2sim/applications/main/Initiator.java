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

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSim;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;

public class Initiator {
    public static void main(String[] args) {
        Log.printConcatLine("Initiating a Hazelcast instance for Cloud2Sim.");
        HazelSim.spawnInstance(HazelSimCore.getCfg());
    }
}