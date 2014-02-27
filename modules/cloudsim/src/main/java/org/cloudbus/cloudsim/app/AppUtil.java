/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.app;

import com.hazelcast.core.Hazelcast;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.CloudSim;

public class AppUtil {
    private static long startTime;

    public static void start() {
        startTime = System.currentTimeMillis();
    }
    public static void shutdown() {
        shutdownLogs();
        Hazelcast.shutdownAll();
    }

    public static void shutdownLogs() {
        long endTime = System.currentTimeMillis();
        double totalTimeTaken = (endTime - startTime)/1000.0;
        double timeTakenForSimulation = (endTime - CloudSim.getSimulationStartedTime())/1000.0;
        Log.printLine("The total time taken for the execution: " + totalTimeTaken + " s.");
        Log.printLine("The time taken for the simulation: " + timeTakenForSimulation + " s.");
        Log.printLine("The time taken for Hazelcast initialization: " + (totalTimeTaken - timeTakenForSimulation) +
                " s.");
    }
}
