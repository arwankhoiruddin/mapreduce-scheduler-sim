/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.infinispan.InfiniSim;
import org.infinispan.Cache;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;

/**
 * Infinispan based map-reduce simulator.
 */
public class InfMapReduceSimulator {

    public static void main(String[] args) throws Exception {
        simulateMapReduce();
    }

    private static void simulateMapReduce() throws Exception {
        Cloud2SimEngine.startInfinispan();
        Log.printLine("# Starting the Infinispan Map Reduce Simulator...");
        InfiniSim infiniSim = InfiniSim.getInfiniSim();
        Cache defaultCache = infiniSim.getDefaultCache();
        MapReduceImpl.startMapReduce(defaultCache);
    }
}