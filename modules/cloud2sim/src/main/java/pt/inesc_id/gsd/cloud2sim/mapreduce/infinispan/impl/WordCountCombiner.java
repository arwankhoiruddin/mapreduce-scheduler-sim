/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.infinispan.impl;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import org.cloudbus.cloudsim.compatibility.infinispan.InfiniSim;
import org.infinispan.distexec.mapreduce.Reducer;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceConstants;

import java.util.Iterator;

/**
 * Implementing a combiner, extending the reducer.
 */
public class WordCountCombiner implements Reducer<String, Long> {
    private long sum = 0;

    public WordCountCombiner() {
        InfiniSim.incrementFlagAtomically(MapReduceConstants.THIS_COMBINERS_FLAG);
        InfiniSim.incrementFlagAtomically(MapReduceConstants.COMBINERS_FLAG);
    }

    @Override
    public Long reduce(String s, Iterator<Long> iter) {
        if (ConfigReader.getIsVerbose()) {
            Log.printConcatLine("Combine..");
        }
        while (iter.hasNext()) {
            Long i = iter.next();
            sum += i;
        }
        return sum;
    }
}
