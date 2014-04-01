package pt.inesc_id.gsd.cloud2sim.autoscale;

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

    public static void readConfig() {
        HzConfigReader.readConfig();
        maxNumberOfInstancesToBeSpawned = Integer.parseInt(prop.getProperty("maxNumberOfInstancesToBeSpawned"));
        highThresholdProcessCpuLoad = Double.parseDouble(prop.getProperty("highThresholdProcessCpuLoad"));
        lowThresholdProcessCpuLoad = Double.parseDouble(prop.getProperty("lowThresholdProcessCpuLoad"));
        timeBetweenScalingDecisions = Integer.parseInt(prop.getProperty("timeBetweenScalingDecisions"));
        timeBetweenHealthChecks = Integer.parseInt(prop.getProperty("timeBetweenHealthChecks"));
    }

    public static int getMaxNumberOfInstancesToBeSpawned() {
        return maxNumberOfInstancesToBeSpawned;
    }

    public static double getHighThresholdProcessCpuLoad() {
        return highThresholdProcessCpuLoad;
    }

    public static double getLowThresholdProcessCpuLoad() {
        return lowThresholdProcessCpuLoad;
    }

    public static int getTimeBetweenScalingDecisions() {
        return timeBetweenScalingDecisions;
    }

    public static int getTimeBetweenHealthChecks() {
        return timeBetweenHealthChecks;
    }
}
