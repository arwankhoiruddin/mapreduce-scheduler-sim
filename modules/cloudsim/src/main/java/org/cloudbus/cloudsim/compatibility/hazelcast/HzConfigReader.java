/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.compatibility.hazelcast;

import org.cloudbus.cloudsim.compatibility.ConfigReader;

/**
 * The class that reads the Hazelcast properties from the properties file.
 */
public class HzConfigReader extends ConfigReader {
    private static String hazelcastXml;

    public static void readConfig() {
        ConfigReader.readConfig();
        hazelcastXml = prop.getProperty("hazelcastXml");
    }

    public static String getHazelcastXml() {
        return hazelcastXml;
    }
}
