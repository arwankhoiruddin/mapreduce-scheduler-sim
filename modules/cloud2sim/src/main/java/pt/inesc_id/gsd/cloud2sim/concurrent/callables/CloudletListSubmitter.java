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
import org.cloudbus.cloudsim.compatibility.hazelcast.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.util.LoadGenerator;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudlet;

import java.io.Serializable;
import java.util.concurrent.Callable;

/**
 * The cloudlet list submitter callable
 */
public class CloudletListSubmitter implements Callable, Serializable, HazelcastInstanceAware {
    private transient HazelcastInstance hazelcastInstance;
    private transient HzObjectCollection hzObjectCollection;

    @Override
    public Integer call() throws Exception {
        if (hzObjectCollection == null) {
            hzObjectCollection = HzObjectCollection.getHzObjectCollection();
        }

        while (hzObjectCollection == null) {
            Thread.sleep(100);
        }

        IMap map = hzObjectCollection.getUserCloudletList();
        for (Object key : map.localKeySet()) {
            HzCloudlet cloudlet = (hzObjectCollection.getUserCloudletList().get(key));
            int cloudletId = cloudlet.getCloudletId();
            if (ConfigReader.isWithWorkload()) {
                int value = LoadGenerator.ifPrime(Double.valueOf(cloudletId));
                cloudlet.setCloudletLength(value);
            }
            hzObjectCollection.getCloudletList().put(cloudletId, cloudlet);
        }

        return map.localKeySet().size();
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
