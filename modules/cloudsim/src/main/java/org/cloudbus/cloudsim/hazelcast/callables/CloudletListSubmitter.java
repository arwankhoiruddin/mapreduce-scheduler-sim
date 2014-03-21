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
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.app.ConfigReader;
import org.cloudbus.cloudsim.app.LoadGenerator;
import org.cloudbus.cloudsim.hazelcast.HzObjectCollection;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class CloudletListSubmitter implements Callable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;

    @Override
    public Integer call() throws Exception {
        IMap map = HzObjectCollection.getUserCloudletList();
        for (Object key : map.localKeySet()) {
            Cloudlet cloudlet = (HzObjectCollection.getUserCloudletList().get(key));
            int cloudletId = cloudlet.getCloudletId();
            if (ConfigReader.isWithWorkload()) {
                int value = LoadGenerator.ifPrime(Double.valueOf(cloudletId));
                cloudlet.setCloudletLength(value);
            }
            HzObjectCollection.getCloudletList().put(cloudletId, cloudlet);
        }

        return map.localKeySet().size();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
