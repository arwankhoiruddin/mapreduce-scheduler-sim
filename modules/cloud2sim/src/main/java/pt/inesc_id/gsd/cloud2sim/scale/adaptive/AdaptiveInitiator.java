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
import com.hazelcast.config.GroupConfig;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSim;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

public class AdaptiveInitiator {
    public static void main(String[] args) {
        initInstance();
    }

    /**
     * Initiates the empty hazelcast instances
     */
    public static void initInstance() {
        Log.printConcatLine("Initiating a Hazelcast instance.");
        Config cfg = getConfig();
        HazelSim.spawnInstance(cfg);

        printSize();
    }

    private static void printSize() {
        int size = HzObjectCollection.getHzObjectCollection().getFirstInstance().getCluster().getMembers().size();
        Log.printConcatLine("Number of instances in this cluster: " + size);
    }

    private static Config getConfig() {
        HzConfigReader.readConfig();
        Config cfg = HazelSimCore.getCfg();
        GroupConfig groupConfig = new GroupConfig("sub");
        cfg.setGroupConfig(groupConfig);
        return cfg;
    }
}
