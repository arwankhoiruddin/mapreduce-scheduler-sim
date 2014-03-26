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
import org.cloudbus.cloudsim.hazelcast.core.HazelSim;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class VmListSubmitter implements Callable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    private transient HazelSim hazelSim;

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Integer call() throws Exception {
        if (hazelSim == null) {
            hazelSim = HazelSim.getHazelSim();
        }

        while(hazelSim == null) {
            Thread.sleep(100);
        }

        IMap map = hazelSim.getUserVmList();
        for (Object key : map.localKeySet()) {
            hazelSim.getVmList().put(hazelSim.getUserVmList().get(key).getId(),
                    hazelSim.getUserVmList().get(key));
        }

        return map.localKeySet().size();
    }
}
