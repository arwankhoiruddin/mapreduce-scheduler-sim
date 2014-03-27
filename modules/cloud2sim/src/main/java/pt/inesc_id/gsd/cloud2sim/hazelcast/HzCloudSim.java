/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.hazelcast;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.Cloud2SimConstants;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import pt.inesc_id.gsd.cloud2sim.util.AppBuilder;
import pt.inesc_id.gsd.cloud2sim.util.AppUtil;
import org.cloudbus.cloudsim.core.CloudSim;

import java.util.Calendar;

public class HzCloudSim extends CloudSim {
    private static int offset;

    public static void init(int numUser, Calendar cal, boolean traceFlag) {
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");
        HazelSimCore hazelSimCore = HazelSimCore.getHazelSim(Cloud2SimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);

        initOffset();

        CloudSim.init(numUser, cal, traceFlag);
        AppBuilder.initWorkers(offset);
    }

    public static int getOffset() {
        return offset;
    }

    public static void initOffset() {
        HzObjectCollection objectCollection = HzObjectCollection.getHzObjectCollection();
        offset = objectCollection.getDeploymentInformation().size();
        objectCollection.getDeploymentInformation().
                put(AppUtil.getStartTime(), Cloud2SimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
    }
}
