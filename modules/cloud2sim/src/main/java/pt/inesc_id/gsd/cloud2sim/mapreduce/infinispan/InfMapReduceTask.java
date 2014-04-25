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
import org.infinispan.distexec.mapreduce.MapReduceTask;

/**
 * Infinispan Map-Reduce Task Implementation
 */
public class InfMapReduceTask {
    /**
     * Creates a new Map Reduce Task
     * @return the map reduce task
     */
    public static MapReduceTask<String, String, String, Long> getMapReduceTask() {
        InfiniSim.numberOfJobs.getAndIncrement();
        return new MapReduceTask<String, String, String, Long>(InfJob.getDefaultCache());
    }
}
