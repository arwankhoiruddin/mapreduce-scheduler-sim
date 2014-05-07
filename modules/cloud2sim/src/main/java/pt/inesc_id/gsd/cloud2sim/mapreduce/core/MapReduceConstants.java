/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.mapreduce.core;

/**
 * Constants of Map Reduce Simulation
 */
public class MapReduceConstants {

    public static final String DEFAULT_JOB_TRACKER = "default";

    public static final String DEFAULT_KEY_VALUE_STORE = "articles";

    public static final String MAP_FLAG = "numberOfMapInvocations";
    public static final String REDUCE_FLAG = "numberOfReduceInvocations";
    public static final String COMBINE_FLAG = "numberOfCombineInvocations";
    public static final String MAPPERS_FLAG = "numberOfMappers";
    public static final String REDUCERS_FLAG = "numberOfReducers";
    public static final String COMBINERS_FLAG = "numberOfCombiners";
    public static final String JOBS_FLAG = "numberOfJobs";
    public static final String THIS_MAPPERS_FLAG = "mappersOfTheJob";
    public static final String THIS_REDUCERS_FLAG = "reducersOfTheJob";
    public static final String THIS_COMBINERS_FLAG = "combinersOfTheJob";
}
