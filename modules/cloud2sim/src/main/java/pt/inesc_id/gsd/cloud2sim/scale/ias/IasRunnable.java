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
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;
import pt.inesc_id.gsd.cloud2sim.scale.AutoScaleConfigReader;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread that is responsible for creating an Initiator instance in the node, by listening to the work load of the
 * master.
 */
public class IasRunnable implements Runnable {
    private static List<HazelcastInstance> instances = new ArrayList<>();

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

    public IMap<String, Boolean> getNodeHealth() {
        return HzObjectCollection.getHzObjectCollection().getFirstInstance().getMap("nodeHealth");
    }

    /**
     * Spawns an instances in the "MAIN" cluster.
     */
    private static void spawnInstance() {
        Config config = HazelSimCore.getCfg();
        Log.printConcatLine("[ISA Runnable] Starting the Initiator instances.");
        instances.add(Hazelcast.newHazelcastInstance(config));
    }

    /**
     * Probes to see whether it needs to spawn a new instance in the MAIN cluster.
     */
    private void probe() {
        if (instances.size() == 0) {
            if (getNodeHealth().get("toScaleOut")) {
                getNodeHealth().put("toScaleOut", false);
                spawnInstance();
            }
        } else {
            if (getNodeHealth().get("toScaleOut")) {
                getNodeHealth().put("toScaleIn", false);
                instances.get(0).shutdown();
                instances.remove(0);
            }
        }
    }
}
