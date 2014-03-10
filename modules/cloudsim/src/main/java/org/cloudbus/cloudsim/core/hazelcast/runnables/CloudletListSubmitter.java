/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.core.hazelcast.runnables;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;

import java.io.Serializable;

public class CloudletListSubmitter implements Runnable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    private final int id;

    public CloudletListSubmitter(int id) {
        this.id = id;
    }

    public void run() {
        HzObjectCollection.getCloudletList().put(HzObjectCollection.getUserCloudletList().get(id).getCloudletId(),
                HzObjectCollection.getUserCloudletList().get(id));
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
