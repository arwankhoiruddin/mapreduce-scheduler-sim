package pt.inesc_id.gsd.cloud2sim.autoscale;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;

public class HealthMonitor {
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
    private double systemLoadAverage;

    public HealthMonitor() {
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
        systemLoadAverage = osMxBean.getSystemLoadAverage();
        systemCpuLoad = get(osMxBean, "getSystemCpuLoad", -1L);
        processCpuLoad = get(osMxBean, "getProcessCpuLoad", -1L);
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

    public static void main(String[] args) {
        HealthMonitor healthMonitor = new HealthMonitor();
        healthMonitor.init();
        System.out.println(healthMonitor.systemCpuLoad);
        System.out.println(healthMonitor.processCpuLoad);
        System.out.println(healthMonitor.systemLoadAverage);

    }
}
