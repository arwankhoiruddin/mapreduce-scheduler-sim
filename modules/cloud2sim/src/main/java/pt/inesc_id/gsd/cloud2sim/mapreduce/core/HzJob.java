/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.core;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.mapreduce.Job;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.mapreduce.KeyValueSource;
import org.cloudbus.cloudsim.Log;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

/**
 * Cloud2Sim representation of the Job
 */
public class HzJob implements pt.inesc_id.gsd.cloud2sim.mapreduce.core.Job {

    private static HazelcastInstance hazelcastInstance;

    private static int sizeOfTheCurrentJob;

    /**
     * Initialize the HzJob.
     */
    @Override
    public void init() {
        hazelcastInstance = HzObjectCollection.getHzObjectCollection().getFirstInstance();
    }

    /**
     * Initialize the HzJob.
     */
    @Override
    public void init(int size) {
        init();
        sizeOfTheCurrentJob = size;
    }

    /**
     * Get a map-reduce job
     * @return the map-reduce job.
     */
    @Override
    public Job<String, String> getJob() {
        // Retrieving the JobTracker by name
        JobTracker jobTracker = hazelcastInstance.getJobTracker(MapReduceConstants.DEFAULT_JOB_TRACKER);

        // Creating the KeyValueSource for a Hazelcast IMap
        IMap<String, String> map = hazelcastInstance.getMap(MapReduceConstants.DEFAULT_KEY_VALUE_STORE);
        KeyValueSource<String, String> source = KeyValueSource.fromMap(map);

        Log.printConcatLine("Creating a new job for the map-reduce task..");
        MapReduceParams.numberOfJobs.getAndIncrement();
        return jobTracker.newJob(source);
    }

    @Override
    public Job<String, String> getJob(int size) {
        sizeOfTheCurrentJob = size;
        return getJob();
    }

    @Override
    public int getSize() {
        return sizeOfTheCurrentJob;
    }

    public static HazelcastInstance getHazelcastInstance() {
        return hazelcastInstance;
    }
}
