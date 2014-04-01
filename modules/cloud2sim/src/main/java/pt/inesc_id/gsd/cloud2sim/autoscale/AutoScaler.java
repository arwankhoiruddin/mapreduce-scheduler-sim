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
import org.cloudbus.cloudsim.compatibility.ConfigReader;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;

import java.util.ArrayList;
import java.util.List;

public class AutoScaler {
    protected static List<HazelcastInstance> instances = new ArrayList<>();

    public static boolean spawnInstance() {
        Log.printConcatLine("[AutoScaler] Initiating a Hazelcast instance.");
        AutoScaleConfigReader.readConfig();
        instances.add(Hazelcast.newHazelcastInstance(HazelSimCore.getCfg()));
        return true;
    }

    public static boolean terminateInstance() {
        if (getSize() > 0) {
            Log.printConcatLine("[AutoScaler] Terminating a Hazelcast instance.");
            getLastInstance().shutdown();
            return true;
        }
        return false;
    }

    public static HazelcastInstance getLastInstance() {
        return instances.get(getSize() - 1);
    }

    public static int getSize () {
        return instances.size();
    }
}
