/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.compatibility.infinispan;

import org.cloudbus.cloudsim.compatibility.Cloud2SimConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Reading the infinispan configuration
 */
public class InfConfigReader {
    private static int mapReduceSize;
    protected static Properties prop;

    private static boolean loadProperties() {
        prop = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(Cloud2SimConstants.INFINISPAN_PROPERTIES_FILE);
            prop.load(input);
            return true;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void readConfig() {
        boolean loaded = loadProperties();
        if (loaded) {
            mapReduceSize = Integer.parseInt(prop.getProperty("mapReduceSize"));
        }
    }

    public static int getMapReduceSize() {
        return mapReduceSize;
    }
}
