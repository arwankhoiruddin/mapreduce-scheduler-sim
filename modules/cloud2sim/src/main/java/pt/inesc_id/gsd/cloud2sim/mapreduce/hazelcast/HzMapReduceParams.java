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
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

/**
 * Parameters of the map reduce simulation.
 */
public final class HzMapReduceParams {
    public static IAtomicLong numberOfMappers = getParam(MapReduceConstants.MAPPERS_FLAG);

    public static IAtomicLong numberOfReducers = getParam(MapReduceConstants.REDUCERS_FLAG);

    public static IAtomicLong numberOfCombiners = getParam(MapReduceConstants.COMBINERS_FLAG);

    public static IAtomicLong numberOfJobs = getParam(MapReduceConstants.JOBS_FLAG);

    public static IAtomicLong mappersOfTheJob = getParam(MapReduceConstants.THIS_MAPPERS_FLAG);

    public static IAtomicLong reducersOfTheJob = getParam(MapReduceConstants.THIS_REDUCERS_FLAG);

    public static IAtomicLong combinersOfTheJob = getParam(MapReduceConstants.THIS_COMBINERS_FLAG);

    public static IAtomicLong mapInvocations = getParam(MapReduceConstants.MAP_FLAG);

    public static IAtomicLong reduceInvocations = getParam(MapReduceConstants.REDUCE_FLAG);

    public static IAtomicLong combineInvocations = getParam(MapReduceConstants.COMBINE_FLAG);

    /**
     * Create a distributed atomic long variable, with the given key
     * @param key, key of the variable.
     * @return the distributed variable.
     */
    public static IAtomicLong getParam(String key) {
        return HzObjectCollection.getHzObjectCollection().getFirstInstance().getAtomicLong(key);
    }

    /**
     * Printing the characteristics of the map reduce jobs.
     */
    public static void printStatus() {
        Log.printConcatLine("******Printing the number of jobs, mappers, combiners, and reducers executed************");
        Log.printConcatLine("Number of Jobs: " + numberOfJobs.get());
        Log.printConcatLine("Number of Mappers: " + numberOfMappers.get());
        Log.printConcatLine("Number of Combiners: " + numberOfCombiners.get());
        Log.printConcatLine("Number of Reducers: " + numberOfReducers.get());
        Log.printConcatLine("Invocations of Map(): " + mapInvocations.get());
        if (ConfigReader.getIsVerbose()) {
            Log.printConcatLine("Invocations of Combine() : " + combineInvocations.get());
            Log.printConcatLine("Invocations of Reduce(): " + reduceInvocations.get());
        }
        Log.printConcatLine("****************************************************************************************");
    }

    /**
     * Printing the characteristics of the current job.
     */
    public static void printJobStatus() {
        Log.printConcatLine("******Printing the parameters of the last executed job************");
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
