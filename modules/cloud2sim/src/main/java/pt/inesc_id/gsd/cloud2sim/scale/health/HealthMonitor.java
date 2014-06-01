/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.scale.health;

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;
import org.cloudbus.cloudsim.Log;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;
import pt.inesc_id.gsd.cloud2sim.scale.AutoScaleConfigReader;
import pt.inesc_id.gsd.cloud2sim.scale.adaptive.AdaptiveScalerProbe;
import pt.inesc_id.gsd.cloud2sim.scale.auto.AutoScaler;

/**
 * The thread that monitors the health of the system.
 */
public class HealthMonitor implements Runnable {
    private Runtime runtime;
    private OperatingSystemMXBean osMxBean;
    private long memoryFree;
    private long memoryTotal;
    private long memoryUsed;
    private long memoryMax;
    private double memoryUsedOfTotalPercentage;
    private double memoryUsedOfMaxPercentage;
    private double systemCpuLoad;
    private double processCpuLoad;
    private double systemLoadAverage; // -1.0 in windows
    private static String healthLogs = "";

    public HealthMonitor() {
        AutoScaleConfigReader.readConfig();
        runtime = Runtime.getRuntime();
        osMxBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        if (AutoScaleConfigReader.getMode() != null && AutoScaleConfigReader.getMode().equalsIgnoreCase("adaptive")) {
            AdaptiveScalerProbe.startHealthAnnouncerInstance();
        }
    }

    /**
     * Periodically initiate the current values of the system parameters
     */
    private void init() {
        memoryFree = runtime.freeMemory();
        memoryTotal = runtime.totalMemory();
        memoryUsed = memoryTotal - memoryFree;
        memoryMax = runtime.maxMemory();
        memoryUsedOfTotalPercentage = 100d * memoryUsed / memoryTotal;
        memoryUsedOfMaxPercentage = 100d * memoryUsed / memoryMax;
        systemCpuLoad = osMxBean.getSystemCpuLoad(); // get(osMxBean, "getSystemCpuLoad", -1L);
        processCpuLoad = osMxBean.getProcessCpuLoad();
        systemLoadAverage = osMxBean.getSystemLoadAverage();
        healthLogs += "[HealthMonitor]: Memory Used of Total, as Percentage: " + memoryUsedOfTotalPercentage +
                ". Memory Used of Maximum, as Percentage: " + memoryUsedOfMaxPercentage +
                ". System CPU Load: " + systemCpuLoad + ". Process CPU Load: " + processCpuLoad +
                ". System Load Average: " + systemLoadAverage + ".\n";
    }

    /**
     * Gets the health logs that are stored.
     * @return health logs
     */
    public static String getHealthLogs() {
        return healthLogs;
    }

    public long getMemoryFree() {
        return memoryFree;
    }

    public long getMemoryTotal() {
        return memoryTotal;
    }

    public long getMemoryUsed() {
        return memoryUsed;
    }

    public long getMemoryMax() {
        return memoryMax;
    }

    public double getMemoryUsedOfTotalPercentage() {
        return memoryUsedOfTotalPercentage;
    }

    public double getMemoryUsedOfMaxPercentage() {
        return memoryUsedOfMaxPercentage;
    }

    public double getSystemCpuLoad() {
        return systemCpuLoad;
    }

    public double getProcessCpuLoad() {
        return processCpuLoad;
    }

    public double getSystemLoadAverage() {
        return systemLoadAverage;
    }

    /**
     * Scale out or scale in, based on the defined values and current health.
     *
     * @return true, if a scaling decision was carried ahead successfully.
     */
    private boolean scale() {
        if ((processCpuLoad > AutoScaleConfigReader.getHighThresholdProcessCpuLoad()) &&
                AutoScaler.getSize() < AutoScaleConfigReader.getMaxNumberOfInstancesToBeSpawned()) {
            Log.printConcatLine("[HealthMonitor] Process CPU Load: " + processCpuLoad +
                    ". Exceeds the allowed maximum.");
            if (AutoScaleConfigReader.getMode() != null) {
                if (AutoScaleConfigReader.getMode().equalsIgnoreCase("auto")) {
                    return AutoScaler.spawnInstance();
                } else {
                    return AdaptiveScalerProbe.addInstance();
                }
            }
        } else if (processCpuLoad < AutoScaleConfigReader.getLowThresholdProcessCpuLoad()) {
            Log.printConcatLine("[HealthMonitor] Process CPU Load: " + processCpuLoad + ". Falls below the minimum.");

            if (AutoScaleConfigReader.getMode() != null) {

                if (AutoScaleConfigReader.getMode().equalsIgnoreCase("auto")) {
                    return AutoScaler.terminateInstance();
                } else {
                    return AdaptiveScalerProbe.removeInstance();
                }
            }
        }
        return false;
    }

    @Override
    public void run() {
        boolean scaled;
        while (true) {
            init();
            scaled = scale();
            int size = HzObjectCollection.getHzObjectCollection().getFirstInstance().getCluster().getMembers().size();
            int waitTimeInMillis = AutoScaleConfigReader.getTimeBetweenHealthChecks() * 1000;
            if (scaled) {
                Log.printConcatLine("Number of instances in this cluster: " + size);
                waitTimeInMillis = AutoScaleConfigReader.getTimeBetweenScalingDecisions() * 1000;
            }
            try {
                Thread.sleep(waitTimeInMillis);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
