/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.scale.adaptive;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Log;
import pt.inesc_id.gsd.cloud2sim.scale.AutoScaleConfigReader;
import pt.inesc_id.gsd.cloud2sim.scale.core.ClusterConfig;
import pt.inesc_id.gsd.cloud2sim.scale.health.HealthParams;

/**
 * Scale adaptively.
 */
public class AdaptiveScalerProbe implements Runnable {

    private static HazelcastInstance instance;

    /**
     * Spawns an instance in the "SUB" cluster.
     */
    public static void startHealthAnnouncerInstance() {
        Config config = ClusterConfig.getSubClusterConfig();
        Log.printConcatLine("[AdaptiveScalerProbe] Starting the middle man instance.");
        instance = Hazelcast.newHazelcastInstance(config);
    }

    public static boolean addInstance() {
        HealthParams.setToScaleOut(true);
        return true;
    }

    public static boolean removeInstance() {
        HealthParams.setToScaleIn(true);
        return true;
    }

    public IMap<String, Boolean> getNodeHealth() {
        return instance.getMap("nodeHealth");
    }

    @Override
    public void run() {
        while (true) {
            int waitTimeInMillis = AutoScaleConfigReader.getTimeBetweenHealthChecks() * 1000;
            try {
                Thread.sleep(waitTimeInMillis);
            } catch (InterruptedException e) {
                return;
            }
            probe();
        }
    }

    private void probe() {
        if (HealthParams.getToScaleOut()) {
            getNodeHealth().put("toScaleOut", true);
            HealthParams.setToScaleOut(false);
        } else if (HealthParams.getToScaleIn()) {
            getNodeHealth().put("toScaleIn", true);
            HealthParams.setToScaleIn(false);
        }
    }
}
