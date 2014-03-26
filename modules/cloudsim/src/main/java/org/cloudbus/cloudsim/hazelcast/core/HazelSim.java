/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.hazelcast.core;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.hazelcast.HzCloudlet;
import org.cloudbus.cloudsim.hazelcast.HzVm;

public class HazelSim extends HazelSimCore {
    private static HazelSim hazelSim = null;

    public static boolean init() {
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");

        HazelSimCore hazelSimCore = HazelSimCore.getHazelSim(HazelSimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
        return true;
    }

    public static void waitForInitiation() {
        if (instances[HazelSimConstants.LAST] == null) {
            try {
                System.out.println("waiting for initiation");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    private HazelSim() {
    }

    public static HazelSim getHazelSim() {
        if(hazelSim == null) {
            hazelSim = new HazelSim();
        }
        return hazelSim;
    }

    public HazelcastInstance[] getInstances() {
        return instances;
    }

    public HazelcastInstance getFirstInstance() {
        return instances[HazelSimConstants.FIRST];
    }

    public HazelcastInstance getLastInstance() {
        return instances[HazelSimConstants.LAST];
    }

    /**
     * Map: cloudletId -> cloudlet execution finished time
     * @return the map
     */
    public IMap<Integer, Double> getCloudletFinishedTime() {
        return instances[HazelSimConstants.FIRST].getMap("finishedTime");
    }

    /**
     * Map: vmId -> hostId
     * @return the map
     */
    public IMap<Integer, Integer> getHostForVm() {
        return instances[HazelSimConstants.LAST].getMap("hostForVm");
    }

    /**
     * Gets the map of Vms created by the user.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getUserVmList() {
        while (instances[HazelSimConstants.FIRST] == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return instances[HazelSimConstants.FIRST].getMap("userVmList");
    }

    /**
     * Gets the map of cloudlets created by the user.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet map
     */
    public IMap<Integer, HzCloudlet> getUserCloudletList() {
        while (instances[HazelSimConstants.FIRST] == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return instances[HazelSimConstants.FIRST].getMap("userCloudletList");
    }

    /**
     * Gets the vm map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getVmList() {
        return instances[HazelSimConstants.LAST].getMap("vmList");
    }

    /**
     * Gets the VMs created Map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getVmsCreatedList() {
        return instances[HazelSimConstants.LAST].getMap("vmCreatedList");
    }

    /**
     * Gets the cloudlet map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public IMap<Integer, HzCloudlet> getCloudletList() {
        return instances[HazelSimConstants.FIRST].getMap("cloudletList");
    }

    /**
     * Gets the submitted cloudlets map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet submitted list
     */
    public IMap<Integer, HzCloudlet> getCloudletSubmittedList() {
        return instances[HazelSimConstants.LAST].getMap("cloudletSubmittedList");
    }

    /**
     * Gets the cloudlet received Map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet received list
     */
    public IMap<Integer, HzCloudlet> getCloudletReceivedList() {
        return instances[HazelSimConstants.LAST].getMap("cloudletReceivedList");
    }

    /**
     * Gets the cloudlet map. Always get from the FIRST instance.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public IMap<Long, Integer> getDeploymentInformation() {
        return instances[HazelSimConstants.FIRST].getMap("deploymentList");
    }

    public IMap<Integer, Datacenter> getDatacenterList() {
        return instances[HazelSimConstants.FIRST].getMap("datacenterList");
    }
}
