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
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSim;

/**
 * Scale adaptively.
 */
public class AdaptiveScaler {

    public static void startMiddleManInstance() {
        Config config = AdaptiveInitiator.getConfig();
        Log.printConcatLine("[AdaptiveScaler] Starting the middle man instance.");
        HazelSim.spawnInstance(config);
    }

    public static boolean addInstance() {
        return true;
    }

    public static boolean removeInstance() {
        return true;
    }
}
