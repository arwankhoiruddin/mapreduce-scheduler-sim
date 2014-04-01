/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.concurrent.runnables;

import com.hazelcast.core.IMap;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;

/**
 * The userObjectsRemover runnable, which is now deprecated.
 */
@Deprecated
public class UserObjectsRemover implements Runnable {
    private transient HzObjectCollection hzObjectCollection = HzObjectCollection.getHzObjectCollection();

    @Override
    public void run() {
        IMap cloudletMap = hzObjectCollection.getUserCloudletList();
        IMap vmMap = hzObjectCollection.getUserVmList();
        for (Object key : cloudletMap.localKeySet()) {
            hzObjectCollection.getUserCloudletList().remove(key);
        }
        for (Object key : vmMap.localKeySet()) {
            hzObjectCollection.getUserVmList().remove(key);
        }
    }
}
