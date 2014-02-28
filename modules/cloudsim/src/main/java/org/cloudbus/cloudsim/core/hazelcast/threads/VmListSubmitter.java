/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.core.hazelcast.threads;

import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;

public class VmListSubmitter extends Thread {
    public void run() {
        HzObjectCollection.getVmList().putAll(HzObjectCollection.getUserVmList());
    }
}
