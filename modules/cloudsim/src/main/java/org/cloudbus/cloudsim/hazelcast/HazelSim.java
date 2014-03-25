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
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;

public class HazelSim extends HazelSimCore {
    private static HazelSim hazelSim = null;

    public static boolean init() {
        Log.printConcatLine("Initiating the Hazelcast instances for Cloud2Sim.");

        HazelSimCore hazelSimCore = HazelSimCore.getHazelSim(HazelSimConstants.HAZELCAST_INSTANCES_STARTED_SIMULTANEOUSLY);
        return true;
    }

//    private static void waitForInitiation() {
//        if (instances[HazelSimConstants.FIRST] == null) {
//            try {
//                Thread.sleep(100);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }
//
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
    public IMap<Integer, Vm> getUserVmList() {
//        waitForInitiation();
        return instances[HazelSimConstants.LAST].getMap("userVmList");
    }

    /**
     * Gets the map of cloudlets created by the user.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet map
     */
    public IMap<Integer, Cloudlet> getUserCloudletList() {
//        waitForInitiation();
        return instances[HazelSimConstants.FIRST].getMap("userCloudletList");
    }

    /**
     * Gets the vm map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, Vm> getVmList() {
        return instances[HazelSimConstants.LAST].getMap("vmList");
    }

    /**
     * Gets the VMs created Map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, Vm> getVmsCreatedList() {
        return instances[HazelSimConstants.LAST].getMap("vmCreatedList");
    }

    /**
     * Gets the cloudlet map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public IMap<Integer, Cloudlet> getCloudletList() {
        return instances[HazelSimConstants.FIRST].getMap("cloudletList");
    }

    /**
     * Gets the submitted cloudlets map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet submitted list
     */
    public IMap<Integer, Cloudlet> getCloudletSubmittedList() {
        return instances[HazelSimConstants.LAST].getMap("cloudletSubmittedList");
    }

    /**
     * Gets the cloudlet received Map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet received list
     */
    public IMap<Integer, Cloudlet> getCloudletReceivedList() {
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
