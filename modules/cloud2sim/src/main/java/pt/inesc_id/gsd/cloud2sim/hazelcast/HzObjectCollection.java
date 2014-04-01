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

import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.compatibility.hazelcast.HazelSim;

/**
 * Access to the hazelcast distributed objects.
 */
public class HzObjectCollection extends HazelSim {
    private static HzObjectCollection hzObjectCollection = null;

    private HzObjectCollection() {
    }

    /**
     * Gets the singleton object
     * @return the hzObjectCollection singleton object
     */
    public static HzObjectCollection getHzObjectCollection() {
        if(hzObjectCollection == null) {
            hzObjectCollection = new HzObjectCollection();
        }
        return hzObjectCollection;
    }

    /**
     * Map: vmId -> hostId
     * @return the map
     */
    public IMap<Integer, Integer> getHostForVm() {
        return getFirstInstance().getMap("hostForVm");
    }

    /**
     * Gets the map of Vms created by the user.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getUserVmList() {
        return getFirstInstance().getMap("userVmList");
    }

    /**
     * Gets the map of cloudlets created by the user.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet map
     */
    public IMap<Integer, HzCloudlet> getUserCloudletList() {
        return getFirstInstance().getMap("userCloudletList");
    }

    /**
     * Gets the vm map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getVmList() {
        return getFirstInstance().getMap("vmList");
    }

    /**
     * Gets the VMs created Map.
     * Map: Vm Id -> Vm
     * @return the vm list
     */
    public IMap<Integer, HzVm> getVmsCreatedList() {
        return getFirstInstance().getMap("vmCreatedList");
    }

    /**
     * Gets the cloudlet map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public IMap<Integer, HzCloudlet> getCloudletList() {
        return getFirstInstance().getMap("cloudletList");
    }

    /**
     * Gets the submitted cloudlets map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet submitted list
     */
    public IMap<Integer, HzCloudlet> getCloudletSubmittedList() {
        return getFirstInstance().getMap("cloudletSubmittedList");
    }

    /**
     * Gets the cloudlet received Map.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet received list
     */
    public IMap<Integer, HzCloudlet> getCloudletReceivedList() {
        return getFirstInstance().getMap("cloudletReceivedList");
    }

    /**
     * Gets the cloudlet map. Always get from the FIRST instance.
     * Map: Cloudlet Id -> Cloudlet
     * @return the cloudlet list
     */
    public IMap<Long, Integer> getDeploymentInformation() {
        return getFirstInstance().getMap("deploymentList");
    }

    public IMap<Integer, Datacenter> getDatacenterList() {
        return getFirstInstance().getMap("datacenterList");
    }
}
