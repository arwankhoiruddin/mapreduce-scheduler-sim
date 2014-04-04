/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.scale.ias;

import com.hazelcast.config.Config;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSim;
import pt.inesc_id.gsd.cloud2sim.scale.core.ClusterConfig;

/**
 * IAS runs from SUB_HZ_CLUSTER cluster from all the nodes except the master node where the ScalableSimulator is run.
 * This listens to the changes that happen to the distributed map nodeHealth.
 * When toScaleOut is set to true, this spawns a new instance in the MAIN_HZ_CLUSTER.
 * When toScaleIn is set to true, this shuts down the existing instance in the MAIN_HZ_CLUSTER.
 */
public class IntelligentAdaptiveScaler {
    public static void main(String[] args) {
        initIAS();
        Thread thread = new Thread(new IasRunnable());
        thread.start();
    }

    /**
     * Initiates the IAS instance that belongs to the SUB cluster.
     */
    private static void initIAS() {
        Log.printConcatLine("Initiating a Hazelcast instance.");
        Config cfg = ClusterConfig.getSubClusterConfig();
        HazelSim.spawnInstance(cfg);
        IasRunnable.initHealthMap();
    }
}
