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
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;

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
    public static IMap<Integer, Double> getCloudletFinishedTime() {
        return instances[0].getMap("finishedTime");
    }

    /**
     * Map: vmId -> hostId
     * @return the map
     */
    public static IMap<Integer, Integer> getHostForVm() {
        return instances[HazelSimConstants.LAST].getMap("hostForVm");
    }

    /**
     * Gets the map of Vms created by the user.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public static IMap<Integer, Vm> getUserVmList() {
        return instances[HazelSimConstants.LAST].getMap("userVmList");
    }

    /**
     * Gets the map of cloudlets created by the user.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet map
     */
    public static IMap<Integer, Cloudlet> getUserCloudletList() {
        return instances[0].getMap("userCloudletList");
    }

    /**
     * Gets the vm map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public static IMap<Integer, Vm> getVmList() {
        return instances[HazelSimConstants.LAST].getMap("vmList");
    }

    /**
     * Gets the VMs created Map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public static IMap<Integer, Vm> getVmsCreatedList() {
        return instances[HazelSimConstants.LAST].getMap("vmCreatedList");
    }

    /**
     * Gets the cloudlet map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public static IMap<Integer, Cloudlet> getCloudletList() {
        return instances[0].getMap("cloudletList");
    }

    /**
     * Gets the submitted cloudlets map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet submitted list
     */
    public static IMap<Integer, Cloudlet> getCloudletSubmittedList() {
        return instances[HazelSimConstants.LAST].getMap("cloudletSubmittedList");
    }

    /**
     * Gets the cloudlet received Map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet received list
     */
    public static IMap<Integer, Cloudlet> getCloudletReceivedList() {
        return instances[HazelSimConstants.LAST].getMap("cloudletReceivedList");
    }
}
