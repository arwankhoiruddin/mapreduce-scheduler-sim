/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.hazelcast.callables;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.hazelcast.HzObjectCollection;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class CloudletListSubmitter implements Callable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;

    @Override
    public Integer call() {
        IMap map = HzObjectCollection.getUserCloudletList();
        for (Object key : map.localKeySet()) {
            HzObjectCollection.getCloudletList().put(HzObjectCollection.getUserCloudletList().get(key).getCloudletId(),
                    HzObjectCollection.getUserCloudletList().get(key));
        }
        return map.localKeySet().size();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
