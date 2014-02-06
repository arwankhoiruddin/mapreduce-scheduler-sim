/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */
package org.cloudbus.cloudsim.core;

/**
 * Public constants used in Cloud2Sim.
 */
public class Cloud2SimConstants {
    public static final int NO_OF_HAZELCAST_INSTANCES = 3;
    public static final String HAZELCAST_CONFIG_FILE = "conf/hazelcast.xml";
    public static final String HAZELCAST_CONFIG_FILE_NOT_FOUND_ERROR =
        "Hazelcast Configuration File not found. Using the default.";
    public static final String DEFAULT_HAZELCAST_ADDRESS = "127.0.0.1:5701";
}
