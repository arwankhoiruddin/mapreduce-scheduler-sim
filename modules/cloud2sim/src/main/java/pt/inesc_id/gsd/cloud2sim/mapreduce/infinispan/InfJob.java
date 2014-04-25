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

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.infinispan.InfiniSim;
import org.infinispan.Cache;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

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

    /**
     * Printing the characteristics of the map reduce jobs.
     */
    public static void printStatus() {
        Log.printConcatLine("******Printing the number of jobs, mappers, combiners, and reducers executed************");
//        Log.printConcatLine("Number of Jobs: " + InfiniSim.getValue(MapReduceConstants.JOBS_FLAG));
        Log.printConcatLine("Invocations of Map(): " + InfiniSim.mapInvocations.get());
        Log.printConcatLine("Invocations of Combine() : " + InfiniSim.combineInvocations.get());
        Log.printConcatLine("Invocations of Reduce(): " + InfiniSim.reduceInvocations.get());
        Log.printConcatLine("Number of Mappers: " + InfiniSim.numberOfMappers.get());
        Log.printConcatLine("Number of Combiners: " + InfiniSim.numberOfCombiners.get());
        Log.printConcatLine("Number of Reducers: " + InfiniSim.numberOfReducers.get());
        Log.printConcatLine("****************************************************************************************");
        printJobStatus();
    }

    /**
     * Printing the characteristics of the current job.
     */
    public static void printJobStatus() {
        Log.printConcatLine("******Printing the number of jobs, mappers, combiners, and reducers executed************");
        Log.printConcatLine("Mappers of This Job: " + InfiniSim.mappersOfTheJob.get());
        Log.printConcatLine("Combiners of This Job: " + InfiniSim.combinersOfTheJob.get());
        Log.printConcatLine("Reducers of This Job: " + InfiniSim.reducersOfTheJob.get());
        Log.printConcatLine("****************************************************************************************");
    }


}
