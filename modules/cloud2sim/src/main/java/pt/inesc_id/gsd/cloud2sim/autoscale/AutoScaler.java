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

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;

import java.util.ArrayList;
import java.util.List;

/**
 * The class responsible for scaling out to multiple hazelcast instances.
 */
public class AutoScaler {
    protected static List<HazelcastInstance> instances = new ArrayList<>();

    /**
     * Start a new hazelcast instance
     * @return true, if successfully spawned
     */
    public static boolean spawnInstance() {
        Log.printConcatLine("[AutoScaler] Initiating a Hazelcast instance.");
        AutoScaleConfigReader.readConfig();
        instances.add(Hazelcast.newHazelcastInstance(HazelSimCore.getCfg()));
        return true;
    }

    /**
     * Shutdown the last hazelcast instance
     * @return true, if successfully shutdown
     */
    public static boolean terminateInstance() {
        if (getSize() > 0) {
            Log.printConcatLine("[AutoScaler] Terminating a Hazelcast instance.");
            getLastInstance().shutdown();
            return true;
        }
        return false;
    }

    /**
     * Get the last among the spawned instances
     * @return the instance
     */
    public static HazelcastInstance getLastInstance() {
        return instances.get(getSize() - 1);
    }

    /**
     * Get the number of spawned instances
     * @return number of instances that are spawned
     */
    public static int getSize () {
        return instances.size();
    }
}
