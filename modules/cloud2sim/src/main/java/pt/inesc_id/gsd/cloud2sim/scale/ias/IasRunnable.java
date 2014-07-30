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
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;
import pt.inesc_id.gsd.cloud2sim.scale.AutoScaleConfigReader;
import pt.inesc_id.gsd.cloud2sim.scale.DynamicScalingConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Thread that is responsible for creating an Initiator instance in the node, by listening to the work load of the
 * master.
 */
public class IasRunnable implements Runnable {
    private static List<HazelcastInstance> instances = new ArrayList<>();

    private static IAtomicLong key =
            HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong("scalingDecision");

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

    public static IMap<String, Boolean> getNodeHealth() {
        return HzObjectCollection.getHzObjectCollection().getFirstInstance().getMap("nodeHealth");
    }

    /**
     * Spawns an instances in the "MAIN" cluster.
     */
    private static void spawnInstance() {
        Config config = HazelSimCore.getCfg();
        Log.printConcatLine("[ISA Runnable] Starting the Initiator Instance, as master exceeded the maximum load.");
        instances.add(Hazelcast.newHazelcastInstance(config));
    }

    /**
     * Probes to see whether it needs to spawn a new instance in the MAIN cluster.
     */
    private static int probe() {
        if (instances.size() == 0) {
            if (getNodeHealth().get("toScaleOut")) {
                getNodeHealth().put("toScaleOut", false);
                long currentValue = key.getAndSet(1);
                if (currentValue != 0) {
                    return 0;
                } else {
                    spawnInstance();
                    try {
                        Thread.sleep(AutoScaleConfigReader.getTimeBetweenScalingDecisions() * 1000);
                    } catch (InterruptedException ignored) {
                    }
                    key.set(0);
                }
            }
            return 1;
        } else {
            if (getNodeHealth().get("toScaleIn")) {
                if (key.get() == DynamicScalingConstants.TERMINATE_ALL_FLAG) {
                    if (instances.get(0).getCluster().getMembers().size() == 2) {
                        for (Object o :
                                HzObjectCollection.getHzObjectCollection().getFirstInstance().getDistributedObjects()) {
                            HzObjectCollection.getHzObjectCollection().getFirstInstance().
                                    getDistributedObjects().remove(o);
                        }
                    }
                    instances.get(0).shutdown();
                    instances.remove(0);
                } else {
                    getNodeHealth().put("toScaleIn", false);
                    long currentValue = key.getAndSet(-1);
                    if (currentValue != 0) {
                        return 0;
                    } else {
                        Log.printConcatLine("[IAS Runnable] Terminating the Initiator Instance, " +
                                "due to minimum work load in the master");
                        instances.get(0).shutdown();
                        instances.remove(0);
                    }
                    try {
                        Thread.sleep(AutoScaleConfigReader.getTimeBetweenScalingDecisions() * 1000);
                    } catch (InterruptedException ignored) {
                    }
                    key.set(0);
                }
            }
            return -1;
        }
    }

    /**
     * Initialize the health map once.
     */
    public static void initHealthMap() {
        if (getNodeHealth().get("toScaleOut") == null) {
            getNodeHealth().put("toScaleOut", false);
        }
        if (getNodeHealth().get("toScaleIn") == null) {
            getNodeHealth().put("toScaleIn", false);
        }
        if (key.get() != 0) {
            key.set(0);
        }
    }
}
