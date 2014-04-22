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

package pt.inesc_id.gsd.cloud2sim.mapreduce.impl;

import com.hazelcast.mapreduce.Context;
import com.hazelcast.mapreduce.Mapper;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConfigReader;
import pt.inesc_id.gsd.cloud2sim.mapreduce.core.MapReduceParams;

import java.util.StringTokenizer;

/**
 * Adopted from Hazelcast source code.
 */
public class TokenizerMapper
        implements Mapper<String, String, String, Long> {

    private static final Long ONE = 1L;

    public TokenizerMapper() {
        MapReduceParams.numberOfMappers.getAndIncrement();
        MapReduceParams.mappersOfTheJob.getAndIncrement();
    }

    @Override

    public void map(String key, String document, Context<String, Long> context) {
        if (HzConfigReader.getIsVerbose()) {
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