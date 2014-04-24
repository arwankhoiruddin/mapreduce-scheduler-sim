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

import org.infinispan.distexec.mapreduce.Reducer;

import java.util.Iterator;

public class WordCountReducer implements Reducer<String, Integer> {
    /** The serialVersionUID */
    private static final long serialVersionUID = 1901016598354633256L;

    @Override
    public Integer reduce(String key, Iterator<Integer> iter) {
        int sum = 0;
        while (iter.hasNext()) {
            Integer i = iter.next();
            sum += i;
        }
        return sum;
    }
}
