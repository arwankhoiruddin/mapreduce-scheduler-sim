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
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

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
    private double processCpuLoad; // -100.0 in windows
    private double systemLoadAverage; // -1.0 in windows

    public HealthMonitor () {
        runtime = Runtime.getRuntime();
        osMxBean = ManagementFactory.getOperatingSystemMXBean();
    }

    public void init() {
        memoryFree = runtime.freeMemory();
        memoryTotal = runtime.totalMemory();
        memoryUsed = memoryTotal - memoryFree;
        memoryMax = runtime.maxMemory();
        memoryUsedOfTotalPercentage = 100d * memoryUsed / memoryTotal;
        memoryUsedOfMaxPercentage = 100d * memoryUsed / memoryMax;
        systemCpuLoad = get(osMxBean, "getSystemCpuLoad", -1L);
        processCpuLoad = get(osMxBean, "getProcessCpuLoad", -1L);
        systemLoadAverage = osMxBean.getSystemLoadAverage();
        System.out.println(memoryUsedOfTotalPercentage);
    }

    private Long get(OperatingSystemMXBean mbean, String methodName, Long defaultValue) {
        try {
            Method method = mbean.getClass().getMethod(methodName);
            method.setAccessible(true);

            Object value = method.invoke(mbean);
            if (value == null) {
                return defaultValue;
            }
            if (value instanceof Integer) {
                return (long) (Integer) value;
            }
            if (value instanceof Double) {
                double v = (Double) value;
                return Math.round(v * 100);
            }
            if (value instanceof Long) {
                return (Long) value;
            }
            return defaultValue;
        } catch (Exception e) {
            return defaultValue;
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

    public static void main(String[] args) {
        Thread t = new Thread(new HealthMonitor());
        t.start();
    }
}
