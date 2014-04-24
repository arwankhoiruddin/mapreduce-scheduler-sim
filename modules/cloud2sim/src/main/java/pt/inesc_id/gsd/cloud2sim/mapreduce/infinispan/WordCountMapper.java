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

import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;

import java.util.StringTokenizer;

public class WordCountMapper  implements Mapper<String,String,String,Integer> {
    /** The serialVersionUID */
    private static final long serialVersionUID = -5943370243108735560L;

    @Override
    public void map(String key, String value, Collector<String, Integer> c) {
        StringTokenizer tokens = new StringTokenizer(value);
        while (tokens.hasMoreElements()) {
            String s = (String) tokens.nextElement();
            c.emit(s, 1);
        }
    }
}
