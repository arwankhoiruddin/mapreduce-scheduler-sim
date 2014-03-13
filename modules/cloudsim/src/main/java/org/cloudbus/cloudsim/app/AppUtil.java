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
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;

public class AppUtil {
    private static long startTime;
    private static boolean isMaster = false;
    private static boolean isPrimaryWorker = false;
    private static int vmsInit;
    private static int vmsFinal;
    private static int cloudletsInit;
    private static int cloudletsFinal;
    private static int noOfCloudlets;
    private static int noOfVms;


    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static int getOffset() {
        int size = HzObjectCollection.getDeploymentInformation().size();
        HzObjectCollection.getDeploymentInformation().
                put(startTime, HazelSimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANIOUSLY);
        return size;
    }

    public static int getNoOfCloudlets() {
        return noOfCloudlets;
    }

    public static void setNoOfCloudlets(int noOfCloudlets) {
        AppUtil.noOfCloudlets = noOfCloudlets;
    }

    public static int getNoOfVms() {
        return noOfVms;
    }

    public static void setNoOfVms(int noOfVms) {
        AppUtil.noOfVms = noOfVms;
    }

    public static boolean getIsMaster() {
        return isMaster;
    }

    public static void setIsMaster(boolean isMaster) {
        AppUtil.isMaster = isMaster;
    }

    public static int getVmsInit() {
        return vmsInit;
    }

    public static void setVmsInit(int vmsInit) {
        AppUtil.vmsInit = vmsInit;
    }

    public static int getVmsFinal() {
        return vmsFinal;
    }

    public static void setVmsFinal(int vmsFinal) {
        AppUtil.vmsFinal = vmsFinal;
    }

    public static int getCloudletsInit() {
        return cloudletsInit;
    }

    public static void setCloudletsInit(int cloudletsInit) {
        AppUtil.cloudletsInit = cloudletsInit;
    }

    public static int getCloudletsFinal() {
        return cloudletsFinal;
    }

    public static void setCloudletsFinal(int cloudletsFinal) {
        AppUtil.cloudletsFinal = cloudletsFinal;
    }

    public static boolean getIsPrimaryWorker() {
        return isPrimaryWorker;
    }

    public static void setIsPrimaryWorker(boolean isPrimaryWorker) {
        AppUtil.isPrimaryWorker = isPrimaryWorker;
    }

    public static void shutdown() {
        shutdownLogs();
        Hazelcast.shutdownAll();
    }

    public static void shutdownLogs() {
        long endTime = System.currentTimeMillis();
        double totalTimeTaken = (endTime - startTime)/1000.0;
        double timeTakenForSimulation = (endTime - CloudSim.getSimulationStartedTime())/1000.0;
        Log.printLine("The time taken for the simulation: " + timeTakenForSimulation + " s.");
        Log.printLine("The time taken for Hazelcast initialization: " + (totalTimeTaken - timeTakenForSimulation) +
                " s.");
        Log.printLine("The total time taken for the execution: " + totalTimeTaken + " s.");
    }
}
