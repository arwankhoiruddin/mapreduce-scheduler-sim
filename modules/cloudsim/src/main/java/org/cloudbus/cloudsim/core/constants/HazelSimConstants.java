/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.core.constants;

import org.cloudbus.cloudsim.app.ConfigReader;

public final class HazelSimConstants {

    /** Suppress instantiation. */
    private HazelSimConstants() {
    }

    public static final int HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY = ConfigReader.getSimultaneousInstances();
    public static final int NO_OF_PARALLEL_EXECUTIONS = ConfigReader.getNoOfExecutions();
    public static final int NO_OF_HAZELCAST_INSTANCES = NO_OF_PARALLEL_EXECUTIONS *
            HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY;

    public static final String HAZELCAST_CONFIG_FILE = "conf/hazelcast.xml";
    public static final String CLOUD2SIM_PROPERTIES_FILE = "conf/cloud2sim.properties";
    public static final String HAZELCAST_CONFIG_FILE_NOT_FOUND_ERROR =
        "Hazelcast Configuration File not found. Using the default.";
    public static final String DEFAULT_HAZELCAST_ADDRESS = "127.0.0.1:5701";
    public static final int FIRST = 0;
    public static final int LAST = HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY - 1;
}
