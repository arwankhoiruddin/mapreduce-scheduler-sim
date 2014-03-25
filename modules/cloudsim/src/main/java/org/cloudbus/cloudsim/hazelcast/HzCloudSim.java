/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.hazelcast;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.app.AppBuilder;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.cloud2sim.core.SimulationEngine;

import java.util.Calendar;

public class HzCloudSim extends CloudSim {
    public static void init(int numUser, Calendar cal, boolean traceFlag) {
        boolean initiated = HzObjectCollection.init();
        while (!initiated) {
            System.out.println("Not initiated");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Log.printConcatLine("Error in waiting for initializing HzCloudSim", e);
            }
        }
        System.out.println("Initiated***********************************");
        CloudSim.init(numUser, cal, traceFlag);
        AppBuilder.initWorkers(SimulationEngine.offset);
    }
}
