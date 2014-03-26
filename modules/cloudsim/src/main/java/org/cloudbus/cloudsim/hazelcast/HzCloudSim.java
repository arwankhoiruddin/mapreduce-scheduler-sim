/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.hazelcast;

import org.cloudbus.cloudsim.app.AppBuilder;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.hazelcast.core.HazelSim;

import java.util.Calendar;

public class HzCloudSim extends CloudSim {
    private static int offset;

    public static void init(int numUser, Calendar cal, boolean traceFlag) {
        HazelSim.init();
        initOffset();

        CloudSim.init(numUser, cal, traceFlag);
        AppBuilder.initWorkers(offset);
    }

    public static int getOffset() {
        return offset;
    }

    public static void initOffset() {
        HazelSim objectCollection = HazelSim.getHazelSim();
        offset = objectCollection.getDeploymentInformation().size();
        objectCollection.getDeploymentInformation().
                put(AppUtil.getStartTime(), HazelSimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
    }
}
