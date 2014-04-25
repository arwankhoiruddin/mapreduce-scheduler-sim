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
import org.infinispan.distexec.mapreduce.Collector;
import org.infinispan.distexec.mapreduce.Mapper;

import java.util.StringTokenizer;

/**
 * Word count mapper, Infinispan implementation.
 */
public class WordCountMapper  implements Mapper<String,String,String,Long> {
    private static final Long ONE = 1L;

    public WordCountMapper() {
        InfiniSim.mappersOfTheJob.getAndIncrement();
        InfiniSim.numberOfMappers.getAndIncrement();
    }

    @Override
    public void map(String key, String document, Collector<String, Long> context) {
        InfiniSim.mapInvocations.getAndIncrement();
        if (ConfigReader.getIsVerbose()) {
            Log.printConcatLine("Map..");
        }

        // Just splitting the text by whitespaces
        StringTokenizer tokenizer = new StringTokenizer(document.toLowerCase());

        // For every token in the text (=> per word)
        while (tokenizer.hasMoreTokens()) {
            // Emit a new value in the mapped results
            context.emit(tokenizer.nextToken(), ONE);
        }
    }
}
