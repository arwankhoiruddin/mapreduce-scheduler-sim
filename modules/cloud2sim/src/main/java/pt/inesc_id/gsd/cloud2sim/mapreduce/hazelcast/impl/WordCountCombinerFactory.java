/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.impl;

import com.hazelcast.mapreduce.Combiner;
import com.hazelcast.mapreduce.CombinerFactory;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.HzMapReduceParams;

/**
 * Adopted from Hazelcast source code. Optional in Hazelcast, though recommended to lower the traffic.
 */
public class WordCountCombinerFactory
        implements CombinerFactory<String, Long, Long> {

    @Override
    public Combiner<String, Long, Long> newCombiner(String key) {
        // Create a new Combiner for the given key
        HzMapReduceParams.numberOfCombiners.getAndIncrement();
        HzMapReduceParams.combinersOfTheJob.getAndIncrement();
        return new WordCountCombiner();
    }

    private class WordCountCombiner
            extends Combiner<String, Long, Long> {

        private long sum = 0;

        @Override
        public void combine(String key, Long value) {
            if (ConfigReader.getIsVerbose()) {
                HzMapReduceParams.combineInvocations.getAndIncrement();
                Log.printConcatLine("Combine..");
            }
            // Increment the per chunk sum
            sum++;
        }

        @Override
        public Long finalizeChunk() {
            // Store the current amount
            long chunk = sum;

            // Reset the per chunk sum
            sum = 0;

            // Return the previously stored sum
            return chunk;
        }
    }
}