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

/**
 * The Map-Reduce Job interface.
 */
public interface Job {
    /**
     * Initialize the Job implementation class.
     */
    public void init();

    /**
     * Gets the job with <K,V> as <String, String>
     * @return the job
     */
    public com.hazelcast.mapreduce.Job<String, String> getJob();
}
