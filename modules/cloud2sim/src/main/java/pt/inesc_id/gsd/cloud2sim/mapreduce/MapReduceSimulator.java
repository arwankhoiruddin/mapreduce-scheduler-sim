/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.ConfigReader;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudSim;
import pt.inesc_id.gsd.cloud2sim.mapreduce.impl.MapReduceCore;

/**
 * The Map Reduce Simulator of Cloud2Sim
 */
public class MapReduceSimulator {

    public static void main(String[] args) throws Exception {
        simulateMapReduce();
    }

    /**
     * Simulates a map-reduce job
     * @throws Exception, if the simulation failed.
     */
    public static void simulateMapReduce() throws Exception {
        Cloud2SimEngine.start();
        Log.printLine("# Starting the Map Reduce Simulator...");
        initCloud2Sim();
        HzJob hzJob = new HzJob();
        // initiate the job with the size
        hzJob.init(HzConfigReader.getMapReduceSize());
        MapReduceCore.initiate(hzJob);
    }

    /**
     * Initiate Cloud2Sim/Hazelcast
     *
     */
    private static void initCloud2Sim() {
        @SuppressWarnings("unused")
        HazelSimCore hazelSimCore = HazelSimCore.getHazelSimCore(ConfigReader.getSimultaneousInstances());
        HzCloudSim.initInstances();
        Cloud2SimEngine.initWorkers(HzCloudSim.getOffset());
        HzCloudSim.setSimulationStartedTime(System.currentTimeMillis());
    }
}
