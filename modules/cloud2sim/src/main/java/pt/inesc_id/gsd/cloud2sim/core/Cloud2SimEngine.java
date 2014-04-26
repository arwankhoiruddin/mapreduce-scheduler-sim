/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.core;

import com.hazelcast.core.Hazelcast;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConstants;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import pt.inesc_id.gsd.cloud2sim.scale.AutoScaleConfigReader;
import pt.inesc_id.gsd.cloud2sim.scale.adaptive.AdaptiveScalerProbe;
import pt.inesc_id.gsd.cloud2sim.scale.health.HealthMonitor;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;

import java.util.ArrayList;
import java.util.List;

/**
 * The Cloud2Sim Engine that starts Cloud2Sim simulations.
 */
public class Cloud2SimEngine {
    private static long startTime;
    private static boolean isMaster = false;
    private static boolean isPrimaryWorker = false;
    private static int vmsInit;
    private static int vmsFinal;
    private static int cloudletsInit;
    private static int cloudletsFinal;
    private static int noOfCloudlets;
    private static int noOfVms;


    /**
     * Starts the executions and threads.
     */
    public static void start() {
        startHzMapReduceSimulator();
        if (AutoScaleConfigReader.getTimeBetweenHealthChecks() > 0) {
            Thread t = new Thread(new HealthMonitor());
            t.start();

            if (AutoScaleConfigReader.getMode() != null && AutoScaleConfigReader.getMode().equalsIgnoreCase("adaptive")) {
                AdaptiveScalerProbe.startHealthAnnouncerInstance();
                Thread t2 = new Thread(new AdaptiveScalerProbe());
                t2.start();
            }
        }
    }

    public static void startHzMapReduceSimulator() {
        startTime = System.currentTimeMillis();
        HzConfigReader.readConfig();
    }

    public static void startInfMapReduceSimulator() {
        startTime = System.currentTimeMillis();
        ConfigReader.readConfig();
    }

    public static int getNoOfCloudlets() {
        return noOfCloudlets;
    }

    public static void setNoOfCloudlets(int noOfCloudlets) {
        Cloud2SimEngine.noOfCloudlets = noOfCloudlets;
    }

    public static int getNoOfVms() {
        return noOfVms;
    }

    public static void setNoOfVms(int noOfVms) {
        Cloud2SimEngine.noOfVms = noOfVms;
    }

    public static boolean getIsMaster() {
        return isMaster;
    }

    public static void setIsMaster(boolean isMaster) {
        Cloud2SimEngine.isMaster = isMaster;
    }

    public static int getVmsInit() {
        return vmsInit;
    }

    public static void setVmsInit(int vmsInit) {
        Cloud2SimEngine.vmsInit = vmsInit;
    }

    public static int getVmsFinal() {
        return vmsFinal;
    }

    public static void setVmsFinal(int vmsFinal) {
        Cloud2SimEngine.vmsFinal = vmsFinal;
    }

    public static int getCloudletsInit() {
        return cloudletsInit;
    }

    public static void setCloudletsInit(int cloudletsInit) {
        Cloud2SimEngine.cloudletsInit = cloudletsInit;
    }

    public static int getCloudletsFinal() {
        return cloudletsFinal;
    }

    public static void setCloudletsFinal(int cloudletsFinal) {
        Cloud2SimEngine.cloudletsFinal = cloudletsFinal;
    }

    public static boolean getIsPrimaryWorker() {
        return isPrimaryWorker;
    }

    public static void setIsPrimaryWorker(boolean isPrimaryWorker) {
        Cloud2SimEngine.isPrimaryWorker = isPrimaryWorker;
    }

    /**
     * Shutdown the hazelcast instances
     */
    public static void shutdown() {
        shutdownLogs();
        if (AutoScaleConfigReader.getMode() != null && AutoScaleConfigReader.getMode().equalsIgnoreCase("adaptive")) {
            AdaptiveScalerProbe.setTerminateAllKey();
        }
        Hazelcast.shutdownAll();
    }

    /**
     * Final logs, marking the termination of simulation.
     */
    public static void shutdownLogs() {
        long endTime = System.currentTimeMillis();
        double totalTimeTaken = (endTime - startTime) / 1000.0;
        double timeTakenForSimulation = (endTime - CloudSim.getSimulationStartedTime()) / 1000.0;
        Log.printLine("The time taken for the simulation: " + timeTakenForSimulation + " s.");
        Log.printLine("The time taken for the initialization: " + (totalTimeTaken - timeTakenForSimulation) +
                " s.");
        Log.printLine("The total time taken for the execution: " + totalTimeTaken + " s.");
    }

    /**
     * Final logs, marking the termination of simulation.
     */
    public static void logTotalExecTime() {
        long endTime = System.currentTimeMillis();
        double totalTimeTaken = (endTime - startTime) / 1000.0;
        Log.printLine("The total time taken for the execution: " + totalTimeTaken + " s.");
    }

    public static long getStartTime() {
        return startTime;
    }

    /**
     * Initialize the master and the primary worker.
     *
     * @param offset the offset.
     */
    public static void initWorkers(int offset) {
        if (offset == HzConstants.NO_OF_PARALLEL_EXECUTIONS - 1) {
            setIsPrimaryWorker(true);
        }

        if (offset == 0) {
            setIsMaster(true);
        }
    }

    /**
     * Create machines
     *
     * @param mips,     millions of instructions per second
     * @param noOfCores number of cores
     * @return machines, the machines that are created with number of cores.
     */
    public static List<Pe> createMachines(int mips, int noOfCores) {
        List<Pe> peList = new ArrayList<Pe>();

        // Create PEs and add these into the list.
        //for a quad-hazelcast machine, a list of 4 PEs is required:
        for (int i = 0; i < noOfCores; i++) {
            peList.add(new Pe(i, new PeProvisionerSimple(mips)));
        }
        return peList;
    }
}
