/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;
import org.cloudbus.cloudsim.core.CloudSim;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.impl.HzMapReduceImpl;

/**
 * The Map Reduce Simulator of Cloud2Sim
 */
public class HzMapReduceSimulator {

    public static void main(String[] args) throws Exception {
        simulateMapReduce();
    }

    /**
     * Simulates a map-reduce job
     * @throws Exception, if the simulation failed.
     */
    public static void simulateMapReduce() throws Exception {
        Cloud2SimEngine.startHz();
        Log.printLine("# Starting the Map Reduce Simulator...");
        initCloud2Sim();
        HzJob hzJob = new HzJob();
        // initiate the job with the size
        hzJob.init(HzConfigReader.getMapReduceSize());
        CloudSim.setSimulationStartedTime(System.currentTimeMillis());
        HzMapReduceImpl.initiate(hzJob);
    }

    /**
     * Initiate Cloud2Sim/Hazelcast
     *
     */
    private static void initCloud2Sim() {
        @SuppressWarnings("unused")
        HazelSimCore hazelSimCore = HazelSimCore.getHazelSimCore(ConfigReader.getSimultaneousInstances());

    }
}
