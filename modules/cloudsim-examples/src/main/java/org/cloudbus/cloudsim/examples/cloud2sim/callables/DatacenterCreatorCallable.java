/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.examples.cloud2sim.callables;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.examples.cloud2sim.util.DatacenterCreator;
import java.util.concurrent.Callable;

public class DatacenterCreatorCallable implements Callable {
    private String name;

    public DatacenterCreatorCallable(String name) {
        this.name = name;
    }

    @Override
    public Datacenter call() throws Exception {
        return DatacenterCreator.createDatacenter(name);
    }
}
