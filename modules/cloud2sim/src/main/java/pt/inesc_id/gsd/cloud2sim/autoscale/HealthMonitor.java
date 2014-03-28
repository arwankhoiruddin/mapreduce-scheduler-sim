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

public class HealthMonitor implements Runnable {
    private Runtime runtime;
    private OperatingSystemMXBean osMxBean;
    private double threshold = 70;
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

    public void init() {
        memoryFree = runtime.freeMemory();
        memoryTotal = runtime.totalMemory();
        memoryUsed = memoryTotal - memoryFree;
        memoryMax = runtime.maxMemory();
        memoryUsedOfTotalPercentage = 100d * memoryUsed / memoryTotal;
        memoryUsedOfMaxPercentage = 100d * memoryUsed / memoryMax;
        systemCpuLoad = osMxBean.getSystemCpuLoad(); // get(osMxBean, "getSystemCpuLoad", -1L);
        processCpuLoad = osMxBean.getProcessCpuLoad();
        systemLoadAverage = osMxBean.getSystemLoadAverage();
        System.out.println(processCpuLoad);
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
