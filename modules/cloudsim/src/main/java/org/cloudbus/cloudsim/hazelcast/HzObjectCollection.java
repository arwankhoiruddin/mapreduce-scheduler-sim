/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.examples.cloud2sim.core.SimulationEngine;

public class HzObjectCollection {
    protected static HazelcastInstance[] instances;

    private HzObjectCollection() {}

    public static boolean init() {
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");

        HazelSim hazelSim = HazelSim.getHazelSim(HazelSimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
        instances = hazelSim.getHazelcastInstances();
        SimulationEngine.offset = AppUtil.getOffset();
        return true;
    }

    public static HazelcastInstance[] getInstances() {
        return instances;
    }

    public static HazelcastInstance getFirstInstance() {
        return instances[HazelSimConstants.FIRST];
    }

    public static HazelcastInstance getLastInstance() {
        return instances[HazelSimConstants.LAST];
    }

    /**
     * Map: cloudletId -> cloudlet execution finished time
     * @return the map
     */
    public static IMap<Integer, Double> getCloudletFinishedTime() {
        return instances[HazelSimConstants.FIRST].getMap("finishedTime");
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
        return instances[HazelSimConstants.FIRST].getMap("userCloudletList");
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
        return instances[HazelSimConstants.FIRST].getMap("cloudletList");
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

    /**
     * Gets the cloudlet map. Always get from the FIRST instance.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public static IMap<Long, Integer> getDeploymentInformation() {
        return instances[HazelSimConstants.FIRST].getMap("deploymentList");
    }

    public static IMap<Integer, Datacenter> getDatacenterList() {
        return instances[HazelSimConstants.FIRST].getMap("datacenterList");
    }

}
