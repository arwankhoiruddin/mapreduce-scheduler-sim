/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.concurrent.runnables;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

import java.io.Serializable;

public class SubmittedCloudletsRemover implements Runnable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    private transient final int cloudletId;
    private transient HzObjectCollection hzObjectCollection = HzObjectCollection.getHzObjectCollection();

    public SubmittedCloudletsRemover(int cloudletId) {
        this.cloudletId = cloudletId;
    }

    @Override
    public void run() {
        hzObjectCollection.getCloudletList().remove(cloudletId);
        hzObjectCollection.getUserCloudletList().remove(cloudletId);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
