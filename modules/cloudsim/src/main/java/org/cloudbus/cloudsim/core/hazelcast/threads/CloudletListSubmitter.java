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

import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;

public class CloudletListSubmitter extends Thread {
    public void run() {
        for (int i = AppUtil.getCloudletsInit(); i <= AppUtil.getCloudletsFinal(); i++) {
            HzObjectCollection.getCloudletList().put(HzObjectCollection.getUserCloudletList().get(i).getCloudletId(),
                    HzObjectCollection.getUserCloudletList().get(i));
        }
    }
}
