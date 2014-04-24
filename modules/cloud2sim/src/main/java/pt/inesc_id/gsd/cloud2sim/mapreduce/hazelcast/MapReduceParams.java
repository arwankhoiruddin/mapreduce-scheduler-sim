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

import com.hazelcast.core.IAtomicLong;
import org.cloudbus.cloudsim.Log;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

/**
 * Parameters of the map reduce simulation.
 */
public final class MapReduceParams {
    public static IAtomicLong numberOfMappers =
            HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong("numberOfMappers");

    public static IAtomicLong numberOfReducers =
            HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong("numberOfReducers");

    public static IAtomicLong numberOfCombiners =
            HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong("numberOfCombiners");

    public static IAtomicLong numberOfJobs =
            HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong("numberOfJobs");

    public static IAtomicLong mappersOfTheJob =
            HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong("mappersOfTheJob");

    public static IAtomicLong reducersOfTheJob =
            HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong("reducersOfTheJob");

    public static IAtomicLong combinersOfTheJob =
            HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong("combinersOfTheJob");

    /**
     * Printing the characteristics of the map reduce jobs.
     */
    public static void printStatus() {
        Log.printConcatLine("******Printing the number of jobs, mappers, combiners, and reducers executed************");
        Log.printConcatLine("Number of Jobs: " + numberOfJobs.get());
        Log.printConcatLine("Number of Mappers: " + numberOfMappers.get());
        Log.printConcatLine("Number of Combiners: " + numberOfCombiners.get());
        Log.printConcatLine("Number of Reducers: " + numberOfReducers.get());
        Log.printConcatLine("****************************************************************************************");
    }

    /**
     * Printing the characteristics of the current job.
     */
    public static void printJobStatus() {
        Log.printConcatLine("******Printing the number of jobs, mappers, combiners, and reducers executed************");
        Log.printConcatLine("Number of Mappers: " + mappersOfTheJob.get());
        Log.printConcatLine("Number of Combiners: " + combinersOfTheJob.get());
        Log.printConcatLine("Number of Reducers: " + reducersOfTheJob.get());
        Log.printConcatLine("****************************************************************************************");
    }

    /**
     * Initializes the job parameters to zero.
     */
    public static void initJobParams() {
        mappersOfTheJob.set(0);
        combinersOfTheJob.set(0);
        reducersOfTheJob.set(0);
    }
}
