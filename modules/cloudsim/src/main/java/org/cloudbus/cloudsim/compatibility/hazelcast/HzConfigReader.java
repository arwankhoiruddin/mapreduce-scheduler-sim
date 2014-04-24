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

/**
 * The class that reads the Hazelcast properties from the properties file.
 */
public class HzConfigReader extends ConfigReader {
    private static String hazelcastXml;
    private static int mapReduceSize;

    public static void readConfig() {
        ConfigReader.readConfig();
        hazelcastXml = prop.getProperty("hazelcastXml");
        mapReduceSize = Integer.parseInt(prop.getProperty("mapReduceSize"));
    }

    public static String getHazelcastXml() {
        return hazelcastXml;
    }

    public static int getMapReduceSize() {
        return mapReduceSize;
    }
}
