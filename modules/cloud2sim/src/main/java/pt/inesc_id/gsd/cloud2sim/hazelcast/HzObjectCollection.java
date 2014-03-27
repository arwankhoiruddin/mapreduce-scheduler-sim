/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.hazelcast;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.compatibility.Cloud2SimConstants;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSimCore;

public class HzObjectCollection extends HazelSimCore {
    private static HzObjectCollection hzObjectCollection = null;

    private HzObjectCollection() {
        System.out.println("HzObjectCollection");
    }

    public static HzObjectCollection getHzObjectCollection() {
        if(hzObjectCollection == null) {
            hzObjectCollection = new HzObjectCollection();
        }
        return hzObjectCollection;
    }

    public HazelcastInstance getFirstInstance() {
        return instances[Cloud2SimConstants.FIRST];
    }

    public HazelcastInstance getLastInstance() {
        return instances[Cloud2SimConstants.LAST];
    }

    /**
     * Map: vmId -> hostId
     * @return the map
     */
    public IMap<Integer, Integer> getHostForVm() {
        return instances[Cloud2SimConstants.LAST].getMap("hostForVm");
    }

    /**
     * Gets the map of Vms created by the user.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getUserVmList() {
        while (instances[Cloud2SimConstants.FIRST] == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return instances[Cloud2SimConstants.FIRST].getMap("userVmList");
    }

    /**
     * Gets the map of cloudlets created by the user.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet map
     */
    public IMap<Integer, HzCloudlet> getUserCloudletList() {
        while (instances[Cloud2SimConstants.FIRST] == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return instances[Cloud2SimConstants.FIRST].getMap("userCloudletList");
    }

    /**
     * Gets the vm map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getVmList() {
        return instances[Cloud2SimConstants.LAST].getMap("vmList");
    }

    /**
     * Gets the VMs created Map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getVmsCreatedList() {
        return instances[Cloud2SimConstants.LAST].getMap("vmCreatedList");
    }

    /**
     * Gets the cloudlet map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public IMap<Integer, HzCloudlet> getCloudletList() {
        return instances[Cloud2SimConstants.FIRST].getMap("cloudletList");
    }

    /**
     * Gets the submitted cloudlets map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet submitted list
     */
    public IMap<Integer, HzCloudlet> getCloudletSubmittedList() {
        return instances[Cloud2SimConstants.LAST].getMap("cloudletSubmittedList");
    }

    /**
     * Gets the cloudlet received Map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet received list
     */
    public IMap<Integer, HzCloudlet> getCloudletReceivedList() {
        return instances[Cloud2SimConstants.LAST].getMap("cloudletReceivedList");
    }

    /**
     * Gets the cloudlet map. Always get from the FIRST instance.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public IMap<Long, Integer> getDeploymentInformation() {
        return instances[Cloud2SimConstants.FIRST].getMap("deploymentList");
    }

    public IMap<Integer, Datacenter> getDatacenterList() {
        return instances[Cloud2SimConstants.FIRST].getMap("datacenterList");
    }
}

