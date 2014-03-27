package org.cloudbus.cloudsim.compatibility.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.Cloud2SimConstants;

import java.io.FileNotFoundException;

public class HazelSimCore {
    private static HazelSimCore hazelSimCore = null;

    private HazelSimCore() {
        Config cfg = getCfg();
        HazelSim.spawnInstance(cfg);
    }

    public static Config getCfg() {
        Config cfg;
        try {
            cfg = new FileSystemXmlConfig(Cloud2SimConstants.HAZELCAST_CONFIG_FILE);
        } catch (FileNotFoundException e) {
            Log.printConcatLine(Cloud2SimConstants.HAZELCAST_CONFIG_FILE_NOT_FOUND_ERROR);
            cfg = new Config();
        }
        cfg.setProperty("hazelcast.initial.min.cluster.size", String.valueOf(Cloud2SimConstants.NO_OF_PARALLEL_EXECUTIONS));
        cfg.setProperty("hazelcast.operation.call.timeout.millis", "50000000");
        return cfg;
    }

    public static HazelSimCore getHazelSimCore() {
        if (hazelSimCore == null) {
            hazelSimCore = new HazelSimCore();
        }
        return hazelSimCore;
    }
}
