/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.scale;

import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;

/**
 * The class that reads the auto-scaler properties from the properties file.
 */
public class AutoScaleConfigReader extends HzConfigReader {
    private static int maxNumberOfInstancesToBeSpawned;
    private static double highThresholdProcessCpuLoad;
    private static double lowThresholdProcessCpuLoad;
    private static int timeBetweenScalingDecisions;
    private static int timeBetweenHealthChecks;
    private static String scalingMode;

    /**
     * Read the config file for autoscaler properties
     */
    public static void readConfig() {
        String temp;
        HzConfigReader.readConfig();
        temp = prop.getProperty("maxNumberOfInstancesToBeSpawned");
        if (temp != null) {
            maxNumberOfInstancesToBeSpawned = Integer.parseInt(temp);
        }
        temp = prop.getProperty("highThresholdProcessCpuLoad");
        if (temp != null) {
            highThresholdProcessCpuLoad = Double.parseDouble(temp);
        }
        temp = prop.getProperty("lowThresholdProcessCpuLoad");
        if (temp != null) {
            lowThresholdProcessCpuLoad = Double.parseDouble(temp);
        }
        temp = prop.getProperty("timeBetweenScalingDecisions");
        if (temp != null) {
            timeBetweenScalingDecisions = Integer.parseInt(temp);
        }
        temp = prop.getProperty("timeBetweenHealthChecks");
        if (temp != null) {
            timeBetweenHealthChecks = Integer.parseInt(temp);
        }
        scalingMode = prop.getProperty("scalingMode");
    }

    /**
     * Get the Maximum number of instances that are spawned. The number of instances could go up to 1 + the maximum
     * number of instances to be spawned, including the simulator instance itself.
     *
     * @return the maximum number of instances.
     */
    public static int getMaxNumberOfInstancesToBeSpawned() {
        return maxNumberOfInstancesToBeSpawned;
    }

    /**
     * Gets the high threshold for the process cpu load
     *
     * @return high threshold for the process cpu load
     */
    public static double getHighThresholdProcessCpuLoad() {
        return highThresholdProcessCpuLoad;
    }

    /**
     * Gets the low threshold for the process cpu load
     *
     * @return low threshold for the process cpu load
     */
    public static double getLowThresholdProcessCpuLoad() {
        return lowThresholdProcessCpuLoad;
    }

    /**
     * Time between any two consecutive scaling decisions - scaling up or scaling down.
     *
     * @return time between scaling decisions, in seconds.
     */
    public static int getTimeBetweenScalingDecisions() {
        return timeBetweenScalingDecisions;
    }

    /**
     * Time between two consecutive health checks for the scaling decisions
     *
     * @return time between health checks, in seconds.
     */
    public static int getTimeBetweenHealthChecks() {
        return timeBetweenHealthChecks;
    }

    public static String getMode() {
        return scalingMode;
    }
}
