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

import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.hazelcast.core.HazelSim;

public class HzVm extends Vm {
    private HazelSim objectCollection = HazelSim.getHazelSim();

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
            objectCollection.getHostForVm().put(this.getId(), host.getId());
        }
    }

    public int getHostId() {
        int id = -1;
        if (objectCollection.getHostForVm().get(super.getId())!= null) {
            id = objectCollection.getHostForVm().get(super.getId());
        }
        return id;
    }
}
