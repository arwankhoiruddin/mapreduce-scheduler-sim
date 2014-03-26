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
import org.cloudbus.cloudsim.app.ConfigReader;
import org.cloudbus.cloudsim.app.LoadGenerator;
import org.cloudbus.cloudsim.hazelcast.core.HazelSim;
import org.cloudbus.cloudsim.hazelcast.HzCloudlet;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class CloudletListSubmitter implements Callable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    private transient HazelSim hazelSim;

    @Override
    public Integer call() throws Exception {
        if (hazelSim == null) {
            hazelSim = HazelSim.getHazelSim();
        }

        while (hazelSim == null) {
            Thread.sleep(100);
        }

        IMap map = hazelSim.getUserCloudletList();
        for (Object key : map.localKeySet()) {
            HzCloudlet cloudlet = (hazelSim.getUserCloudletList().get(key));
            int cloudletId = cloudlet.getCloudletId();
            if (ConfigReader.isWithWorkload()) {
                int value = LoadGenerator.ifPrime(Double.valueOf(cloudletId));
                cloudlet.setCloudletLength(value);
            }
            hazelSim.getCloudletList().put(cloudletId, cloudlet);
        }

        return map.localKeySet().size();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
