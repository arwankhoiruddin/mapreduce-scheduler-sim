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

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;

/**
 * Extending VM to use in a distributed environment with hazelcast.
 */
public class HzVm extends Vm {
    private HzObjectCollection hzObjectCollection = HzObjectCollection.getHzObjectCollection();

    public HzVm(int id, int userId, double mips, int numberOfPes, int ram, long bw, long size, String vmm,
                CloudletScheduler cloudletScheduler) {
        super(id, userId, mips, numberOfPes, ram, bw, size, vmm, cloudletScheduler);
    }

    /**
     * Sets the host that runs this VM.
     *
     * @param host Host running the VM
     * @pre host != $null
     * @post $none
     */
    public void setHost(Host host) {
        if (host != null) {
            super.setHost(host);
            hzObjectCollection.getHostForVm().put(this.getId(), host.getId());
        }
    }

    /**
     * Gets the id of the host that the vm is assigned to.
     * @return hostId.
     */
    public int getHostId() {
        int id = -1;
        if (hzObjectCollection.getHostForVm().get(getId())!= null) {
            id = hzObjectCollection.getHostForVm().get(getId());
        }
        return id;
    }
}
