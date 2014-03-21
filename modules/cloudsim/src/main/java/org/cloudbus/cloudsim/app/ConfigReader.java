/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.app;

import org.cloudbus.cloudsim.core.constants.HazelSimConstants;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {
    private static int noOfUsers;   // 200
    private static int noOfDatacenters; //1500
    private static int noOfHosts; // 1600. // 3200 fills up the memory
    private static int noOfVms; //2000
    private static int noOfCloudlets;  //20000
    private static boolean isRR; // true
    private static boolean withWorkload; //true
    private static int simultaneousInstances; // 1
    private static int noOfExecutions; // 2

    public static void readConfig() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(HazelSimConstants.CLOUD2SIM_PROPERTIES_FILE);
            prop.load(input);

            noOfUsers = Integer.parseInt(prop.getProperty("noOfUsers"));
            noOfDatacenters = Integer.parseInt(prop.getProperty("noOfDatacenters"));
            noOfHosts = Integer.parseInt(prop.getProperty("noOfHosts"));
            noOfVms = Integer.parseInt(prop.getProperty("noOfVms"));
            noOfCloudlets = Integer.parseInt(prop.getProperty("noOfCloudlets"));
            isRR = Boolean.parseBoolean(prop.getProperty("isRR"));
            withWorkload = Boolean.parseBoolean(prop.getProperty("withWorkload"));
            simultaneousInstances = Integer.parseInt(prop.getProperty("simultaneousInstances"));
            noOfExecutions = Integer.parseInt(prop.getProperty("noOfExecutions"));
        } catch (IOException ex) {
            ex.printStackTrace();
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

    public static int getNoOfUsers() {
        return noOfUsers;
    }

    public static int getNoOfDatacenters() {
        return noOfDatacenters;
    }

    public static int getNoOfHosts() {
        return noOfHosts;
    }

    public static int getNoOfVms() {
        return noOfVms;
    }

    public static int getNoOfCloudlets() {
        return noOfCloudlets;
    }

    public static boolean getIsRR() {
        return isRR;
    }

    public static boolean isWithWorkload() {
        return withWorkload;
    }

    public static int getSimultaneousInstances() {
        return simultaneousInstances;
    }

    public static int getNoOfExecutions() {
        return noOfExecutions;
    }
}
