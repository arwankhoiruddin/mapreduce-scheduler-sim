/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *                Toolkit for Modeling and Simulation
 *                of Clouds - Enhanced version of CloudSim.
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Enabling Multi-tenanted adaptively scaling deployments.
 * This class has instances in multiple clusters that it wishes to coordinate.
 */
public class Coordinator implements Runnable {
    private static Map<String, List<HazelcastInstance>> instancesMap = new HashMap<>();
    private static Map<String, IAtomicLong> keyMap = new HashMap<>();


    /**
     * Initialize the instancesMap.
     */
    private static void initInstancesMap() {
        for (String key: instancesMap.keySet()) {
            List<HazelcastInstance> instances = new ArrayList<>();
            instancesMap.putIfAbsent(key, instances);
        }
    }

    /**
     * Initialize the key map for scaling decisions.
     */
    private static void initKeysMap(){
        for (String key: keyMap.keySet()) {
            IAtomicLong iKey = HzObjectCollection.getHzObjectCollection().getFirstInstance(key).
                    getAtomicLong("scalingDecision");
            keyMap.putIfAbsent(key, iKey);
        }
    }

    @Override
    public void run() {
        while (true) {
            for (String key : keyMap.keySet()) {
                int waitTimeInMillis = AutoScaleConfigReader.getTimeBetweenHealthChecks() * 1000;
                try {
                    Thread.sleep(waitTimeInMillis);
                } catch (InterruptedException e) {
                    return;
                }
                probe(key);
            }
        }
    }

    /**
     * Gets the health of the given cluster
     * @param iKey, cluster name
     * @return health map
     */
    public static IMap<String, Boolean> getNodeHealth(String iKey) {
        return HzObjectCollection.getHzObjectCollection().getFirstInstance(iKey).getMap("nodeHealth");
    }

    /**
     * Spawns an instances in the given cluster.
     */
    private static void spawnInstance(String mainCluster) {
        Config config = HazelSimCore.getCfg(mainCluster);
        Log.printConcatLine("[ISA Runnable] Starting the Initiator Instance, as master exceeded the maximum load.");
        List<HazelcastInstance> instances = instancesMap.get(mainCluster);
        instances.add(Hazelcast.newHazelcastInstance(config));
        instancesMap.put(mainCluster, instances);
    }

    /**
     * Probes to see whether it needs to spawn a new instance in the given cluster in the Multi-tenanted deployment
     */
    private static int probe(String iKey) {
        if (instancesMap.get(iKey).size() == 0) {
            if (getNodeHealth(iKey).get("toScaleOut")) {
                getNodeHealth(iKey).put("toScaleOut", false);
                long currentValue = keyMap.get(iKey).getAndSet(1);
                if (currentValue != 0) {
                    return 0;
                } else {
                    spawnInstance(iKey);
                    try {
                        Thread.sleep(AutoScaleConfigReader.getTimeBetweenScalingDecisions() * 1000);
                    } catch (InterruptedException ignored) {
                    }
                    keyMap.get(iKey).set(0);
                }
            }
            return 1;
        } else {
            if (getNodeHealth(iKey).get("toScaleIn")) {
                if (keyMap.get(iKey).get() == DynamicScalingConstants.TERMINATE_ALL_FLAG) {
                    if (instancesMap.get(iKey).get(0).getCluster().getMembers().size() == 2) {
                        for (Object o :
                                HzObjectCollection.getHzObjectCollection().getFirstInstance().getDistributedObjects()) {
                            HzObjectCollection.getHzObjectCollection().getFirstInstance().
                                    getDistributedObjects().remove(o);
                        }
                    }
                    instancesMap.get(iKey).get(0).shutdown();
                    instancesMap.get(iKey).remove(0);
                } else {
                    getNodeHealth(iKey).put("toScaleIn", false);
                    long currentValue = keyMap.get(iKey).getAndSet(-1);
                    if (currentValue != 0) {
                        return 0;
                    } else {
                        Log.printConcatLine("[IAS Runnable] Terminating the Initiator Instance, " +
                                "due to minimum work load in the master");
                        instancesMap.get(iKey).get(0).shutdown();
                        instancesMap.get(iKey).remove(0);
                    }
                    try {
                        Thread.sleep(AutoScaleConfigReader.getTimeBetweenScalingDecisions() * 1000);
                    } catch (InterruptedException ignored) {
                    }
                    keyMap.get(iKey).set(0);
                }
            }
            return -1;
        }
    }

    /**
     * Initialize the health map once for each of the clusters.
     */
    public static void initHealthMap(String iKey) {
        if (getNodeHealth(iKey).get("toScaleOut") == null) {
            getNodeHealth(iKey).put("toScaleOut", false);
        }
        if (getNodeHealth(iKey).get("toScaleIn") == null) {
            getNodeHealth(iKey).put("toScaleIn", false);
        }
        if (keyMap.get(iKey).get() != 0) {
            keyMap.get(iKey).set(0);
        }
    }
}
