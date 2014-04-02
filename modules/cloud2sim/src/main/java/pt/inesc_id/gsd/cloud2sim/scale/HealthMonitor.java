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

import java.lang.management.ManagementFactory;

import com.sun.management.OperatingSystemMXBean;
import org.cloudbus.cloudsim.Log;
import pt.inesc_id.gsd.cloud2sim.scale.adaptive.AdaptiveScaler;
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

    public HealthMonitor() {
        runtime = Runtime.getRuntime();
        osMxBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
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
    }

    /**
     * Scale out or scale in, based on the defined values and current health.
     *
     * @return true, if a scaling decision was carried ahead successfully.
     */
    private boolean scale() {
//            System.out.println(AutoScaleConfigReader.getMode());
//        System.out.println(AutoScaleConfigReader.getHighThresholdProcessCpuLoad());
        if ((processCpuLoad > AutoScaleConfigReader.getHighThresholdProcessCpuLoad()) &&
                AutoScaler.getSize() < AutoScaleConfigReader.getMaxNumberOfInstancesToBeSpawned()) {
            Log.printConcatLine("[HealthMonitor] Process CPU Load: " + processCpuLoad +
                    ". Exceeds the allowed maximum.");
            if (AutoScaleConfigReader.getMode() == 0) {
                return AutoScaler.spawnInstance();
            } else {
                return AdaptiveScaler.addInstance();
            }
        } else if (processCpuLoad < AutoScaleConfigReader.getLowThresholdProcessCpuLoad()) {
            Log.printConcatLine("[HealthMonitor] Process CPU Load: " + processCpuLoad + ". Falls below the minimum.");
            if (AutoScaleConfigReader.getMode() == 0) {
                return AutoScaler.terminateInstance();
            } else {
                return AdaptiveScaler.removeInstance();
            }
        }
        return false;
    }

    @Override
    public void run() {
        boolean scaled = true;
        while (true) {
            int waitTimeInMillis = AutoScaleConfigReader.getTimeBetweenHealthChecks() * 1000;
            if (scaled)
                waitTimeInMillis = AutoScaleConfigReader.getTimeBetweenScalingDecisions() * 1000;

            try {
                Thread.sleep(waitTimeInMillis);
            } catch (InterruptedException e) {
                return;
            }
            init();
            scaled = scale();
        }
    }
}
