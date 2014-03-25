/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.hazelcast.runnables;

import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.hazelcast.HazelSim;

@Deprecated
public class UserObjectsRemover implements Runnable {
    private transient HazelSim hazelSim = HazelSim.getHazelSim();

    @Override
    public void run() {
        IMap cloudletMap = hazelSim.getUserCloudletList();
        IMap vmMap = hazelSim.getUserVmList();
        for (Object key : cloudletMap.localKeySet()) {
            hazelSim.getUserCloudletList().remove(key);
        }
        for (Object key : vmMap.localKeySet()) {
            hazelSim.getUserVmList().remove(key);
        }
    }
}
