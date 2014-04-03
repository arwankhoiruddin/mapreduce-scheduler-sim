/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.scale.core;

import com.hazelcast.config.Config;
import com.hazelcast.config.GroupConfig;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConstants;

public class ClusterConfig {
    /**
     * Gets the configuration for the cluster
     * @return hazelcast configuration
     */
    public static Config getSubClusterConfig() {
        HzConfigReader.readConfig();
        Config cfg = HazelSimCore.getCfg();
        GroupConfig groupConfig = new GroupConfig(HzConstants.SUB_HZ_CLUSTER);
        cfg.setGroupConfig(groupConfig);
        return cfg;
    }
}
