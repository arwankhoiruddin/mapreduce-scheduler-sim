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
import org.infinispan.distexec.mapreduce.Reducer;

import java.util.Iterator;

public class WordCountReducer implements Reducer<String, Long> {
    private volatile long sum = 0;

    @Override
    public Long reduce(String key, Iterator<Long> iter) {
        if (ConfigReader.getIsVerbose()) {
            Log.printConcatLine("Reduce..");
        }

        Long sum = 0L;
        while (iter.hasNext()) {
            Long i = iter.next();
            sum += i;
        }
        return sum;
    }
}
