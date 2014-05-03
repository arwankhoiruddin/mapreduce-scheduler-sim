/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *                Toolkit for Modeling and Simulation
 *                of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.infinispan;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.infinispan.InfiniSim;
import org.infinispan.Cache;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;

public class Initiator {
    public static void main(String[] args) {
        Cloud2SimEngine.startInfMapReduceSimulator();
        InfiniSim infiniSim = InfiniSim.getInfiniSim();
        @SuppressWarnings("unused")
        Cache defaultCache = infiniSim.getDefaultCache();
        Log.printConcatLine("Infinispan Initiator instance started..");
    }
}
