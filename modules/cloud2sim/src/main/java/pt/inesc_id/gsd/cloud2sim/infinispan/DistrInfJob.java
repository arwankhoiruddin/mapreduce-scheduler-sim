/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.infinispan;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.infinispan.InfiniSim;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;
import pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan.InfJob;

public class DistrInfJob extends InfJob {
    /**
     * Printing the characteristics of the map reduce jobs.
     */
    public static void printStatus() {
        Log.printConcatLine("******Printing the number of jobs, mappers, combiners, and reducers executed************");
        Log.printConcatLine("Number of Jobs: " + DistrInfiniSim.getValue(MapReduceConstants.JOBS_FLAG));
        Log.printConcatLine("Number of Mappers: " + DistrInfiniSim.getValue(MapReduceConstants.MAPPERS_FLAG));
        Log.printConcatLine("Number of Combiners: " + DistrInfiniSim.getValue(MapReduceConstants.COMBINERS_FLAG));
        Log.printConcatLine("Number of Reducers: " + DistrInfiniSim.getValue(MapReduceConstants.REDUCERS_FLAG));
        Log.printConcatLine("****************************************************************************************");
    }

    /**
     * Printing the characteristics of the current job.
     */
    public static void printJobStatus() {
        Log.printConcatLine("******Printing the number of jobs, mappers, combiners, and reducers executed************");
        Log.printConcatLine("Number of Mappers: " + DistrInfiniSim.getValue(MapReduceConstants.THIS_MAPPERS_FLAG));
        Log.printConcatLine("Number of Combiners: " + DistrInfiniSim.getValue(MapReduceConstants.THIS_COMBINERS_FLAG));
        Log.printConcatLine("Number of Reducers: " + DistrInfiniSim.getValue(MapReduceConstants.THIS_REDUCERS_FLAG));
        Log.printConcatLine("****************************************************************************************");
    }

}
