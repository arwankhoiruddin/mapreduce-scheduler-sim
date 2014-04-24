/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan;

import org.cloudbus.cloudsim.compatibility.infinispan.InfiniSim;
import org.infinispan.Cache;

/**
 * Infinispan representation of the Job
 */
public class InfJob implements pt.inesc_id.gsd.cloud2sim.mapreduce.core.Job {
    private static Cache defaultCache;
    private static int sizeOfTheCurrentJob;

    @Override
    public void init() {
        InfiniSim infiniSim = InfiniSim.getInfiniSim();
        defaultCache = infiniSim.getDefaultCache();
    }

    @Override
    public void init(int size) {
        init();
        sizeOfTheCurrentJob = size;
    }

    @Override
    public int getSize() {
        return sizeOfTheCurrentJob;
    }

    public static Cache getDefaultCache() {
        return defaultCache;
    }
}
