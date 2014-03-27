/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.applications.callables;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.HazelcastInstanceAware;
import org.cloudbus.cloudsim.Datacenter;
import pt.inesc_id.gsd.cloud2sim.applications.core.DatacenterCreator;

import java.io.Serializable;
import java.util.concurrent.Callable;

public class DatacenterCreatorCallable implements Callable<Datacenter>, Serializable, HazelcastInstanceAware {
    private String name;
    private transient HazelcastInstance hazelcastInstance;

    public DatacenterCreatorCallable(String name) {
        this.name = name;
    }

    @Override
    public Datacenter call() throws Exception {
        return DatacenterCreator.createDatacenter(name);
    }

    @Override
    public void setHazelcastInstance(HazelcastInstance hazelcastInstance) {
        this.hazelcastInstance = hazelcastInstance;
    }
}
