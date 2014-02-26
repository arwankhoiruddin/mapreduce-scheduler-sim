/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.core.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;

import java.util.List;
import java.util.Map;

public class HzObjectCollection {
    protected static HazelcastInstance[] instances;

    public static void init() {
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");

        HazelSim hazelSim = HazelSim.getHazelSim(HazelSimConstants.NO_OF_HAZELCAST_INSTANCES);
        instances = hazelSim.getHazelcastInstances();
    }

    /**
     * Map: cloudletId -> cloudlet execution finished time
     * @return the map
     */
    public static Map<Integer, Double> getCloudletFinishedTime() {
        return instances[0].getMap("finishedTime");
    }

    /**
     * Map: vmId -> hostId
     * @return the map
     */
    public static Map<Integer, Integer> getHostForVm() {
        return instances[0].getMap("hostForVm");
    }

    /**
     * Gets the list of vms created by the user.
     *
     * @param <T> the generic type
     * @return the vm list
     */
    @SuppressWarnings("unchecked")
    public static <T extends Vm> List<T> getUserVmList() {
        return instances[0].getList("userVmList");
    }

    /**
     * Gets the map of cloudlets created by the user.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet map
     */
    @SuppressWarnings("unchecked")
    public static Map <Integer, Cloudlet> getUserCloudletList() {
        return instances[0].getMap("userCloudletList");
    }

    /**
     * Gets the vm list.
     *
     * @param <T> the generic type
     * @return the vm list
     */
    @SuppressWarnings("unchecked")
    public static <T extends Vm> List<T> getVmList() {
        return instances[0].getList("vmList");
    }

    /**
     * Gets the VMs created list.
     *
     * @param <T> the generic type
     * @return the vm list
     */
    @SuppressWarnings("unchecked")
    public static <T extends Vm> List<T> getVmsCreatedList() {
        return instances[0].getList("vmCreatedList");
    }

    /**
     * Gets the cloudlet map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    @SuppressWarnings("unchecked")
    public static Map <Integer, Cloudlet> getCloudletList() {
        return instances[0].getMap("cloudletList");
    }

    /**
     * Gets the cloudlet submitted map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet submitted list
     */
    @SuppressWarnings("unchecked")
    public static Map <Integer, Cloudlet> getCloudletSubmittedList() {
        return instances[0].getMap("cloudletSubmittedList");
    }

    /**
     * Gets the cloudlet received list.
     *
     * @param <T> the generic type
     * @return the cloudlet received list
     */
    @SuppressWarnings("unchecked")
    public static <T extends Cloudlet> List<T> getCloudletReceivedList() {
        return instances[0].getList("cloudletReceivedList");
    }
}
