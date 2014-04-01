/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.util;

import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.compatibility.Cloud2SimConstants;
import org.cloudbus.cloudsim.compatibility.hazelcast.HzConstants;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;

import java.util.ArrayList;
import java.util.List;

public class AppBuilder {
    public static List<Pe> createMachines(int mips, int noOfCores) {
        List<Pe> peList = new ArrayList<Pe>();

        // Create PEs and add these into the list.
        //for a quad-hazelcast machine, a list of 4 PEs is required:
        for (int i = 0; i < noOfCores; i++) {
            peList.add(new Pe(i, new PeProvisionerSimple(mips)));
        }
        return peList;
    }

    public static int getPartitionInit(int noOfParams, int offset) {
        return (int) (offset * Math.ceil((noOfParams / (double) HzConstants.NO_OF_PARALLEL_EXECUTIONS)));
    }

    public static int getPartitionFinal(int noOfParams, int offset) {
        int temp = (int) ((offset + 1) * Math.ceil((noOfParams /
                (double) HzConstants.NO_OF_PARALLEL_EXECUTIONS)));
        return temp < noOfParams ? temp : noOfParams;
    }

    public static int getPartitionSize(int noOfParams) {
        return (int) Math.ceil((noOfParams / (double) HzConstants.NO_OF_PARALLEL_EXECUTIONS));
    }

    public static void initWorkers(int offset) {
        if (offset == HzConstants.NO_OF_PARALLEL_EXECUTIONS - 1) {
            AppUtil.setIsPrimaryWorker(true);
        }

        if (offset == 0) {
            AppUtil.setIsMaster(true);
        }
    }
}
