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

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import org.cloudbus.cloudsim.hazelcast.HazelSim;

import java.io.Serializable;

public class SubmittedCloudletsRemover implements Runnable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    private transient final int cloudletId;
    private transient HazelSim hazelSim = HazelSim.getHazelSim();

    public SubmittedCloudletsRemover(int cloudletId) {
        this.cloudletId = cloudletId;
    }

    @Override
    public void run() {
        hazelSim.getCloudletList().remove(cloudletId);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
