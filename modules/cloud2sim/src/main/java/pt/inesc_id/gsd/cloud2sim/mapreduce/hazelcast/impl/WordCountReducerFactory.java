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

import com.hazelcast.mapreduce.Reducer;
import com.hazelcast.mapreduce.ReducerFactory;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.mapreduce.hazelcast.HzMapReduceParams;

/**
 * Adopted from Hazelcast source code.
 */
public class WordCountReducerFactory
        implements ReducerFactory<String, Long, Long> {



    @Override
    public Reducer<String, Long, Long> newReducer(String key) {
        // Create a new Reducer for the given key
        HzMapReduceParams.numberOfReducers.getAndIncrement();
        HzMapReduceParams.reducersOfTheJob.getAndIncrement();
        return new WordCountReducer();
    }

    private class WordCountReducer
            extends Reducer<String, Long, Long> {

        private volatile long sum = 0;

        @Override
        public void reduce(Long value) {
            if (ConfigReader.getIsVerbose()) {
                Log.printConcatLine("Reduce..");
            }
            // Just increment the sum by the pre combined chunk value
            sum += value;
        }

        @Override
        public Long finalizeReduce() {
            // Return the final reduced sum
            return sum;
        }
    }
}
