/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.concurrent.callables;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import com.hazelcast.core.IMap;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class VmListSubmitter implements Callable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    private transient HzObjectCollection hzObjectCollection;

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }

    @Override
    public Integer call() throws Exception {
        if (hzObjectCollection == null) {
            hzObjectCollection = HzObjectCollection.getHzObjectCollection();
        }

        while(hzObjectCollection == null) {
            Thread.sleep(100);
        }

        IMap map = hzObjectCollection.getUserVmList();
        for (Object key : map.localKeySet()) {
            hzObjectCollection.getVmList().put(hzObjectCollection.getUserVmList().get(key).getId(),
                    hzObjectCollection.getUserVmList().get(key));
        }

        return map.localKeySet().size();
    }
}
