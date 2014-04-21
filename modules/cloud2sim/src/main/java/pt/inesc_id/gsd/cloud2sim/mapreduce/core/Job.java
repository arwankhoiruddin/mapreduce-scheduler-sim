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

/**
 * The Map-Reduce Job interface.
 */
public interface Job {
    /**
     * Initialize the Job implementation class.
     */
    public void init();

    /**
     * Initialize the Job implementation class with an initial size.
     */
    public void init(int size);

    /**
     * Gets the job with <K,V> as <String, String>
     * @return the job
     */
    public com.hazelcast.mapreduce.Job<String, String> getJob();

    /**
     * Gets the job with <K,V> as <String, String>
     * @param size, size of the job
     * @return the job
     */
    public com.hazelcast.mapreduce.Job<String, String> getJob(int size);

    /**
     * Gets the size of the map reduce job
     * @return size
     */
    public int getSize();
}
