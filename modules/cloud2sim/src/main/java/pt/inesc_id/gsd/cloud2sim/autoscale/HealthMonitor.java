/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.autoscale;

import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.ConfigReader;
import sun.util.logging.resources.logging;

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

    public HealthMonitor () {
        runtime = Runtime.getRuntime();
        osMxBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
    }

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
        scale();
    }

    private void scale() {
        if ((processCpuLoad > ConfigReader.getHighThresholdProcessCpuLoad()) && AutoScaler.getSize() <
                ConfigReader.getMaxNumberOfInstancesToBeSpawned()) {
            Log.printConcatLine("Process CPU Load: " + processCpuLoad + ". Exceeds the allowed maximum.");
            AutoScaler.spawnInstance();
        } else if (processCpuLoad < ConfigReader.getLowThresholdProcessCpuLoad()) {
            Log.printConcatLine("Process CPU Load: " + processCpuLoad + ". Falls below the minimum.");
            AutoScaler.terminateInstance();
        }
    }

    @Override
    public void run() {
        while (true) {
            init();
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                return;
            }
        }
    }
}
