/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.core;

import org.cloudbus.cloudsim.compatibility.hazelcast.HzConstants;

/**
 * Utility class for partitioning the executions.
 */
public class PartitionUtil {

    /**
     * Gets the initial value of the partition
     *
     * @param noOfParams total number of entities in the specific parameter.
     * @param offset,    the offset
     * @return the initial value of the partition
     */
    public static int getPartitionInit(int noOfParams, int offset) {
        return (int) (offset * Math.ceil((noOfParams / (double) HzConstants.NO_OF_PARALLEL_EXECUTIONS)));
    }

    /**
     * Gets the final value of the partition
     *
     * @param noOfParams total number of entities in the specific parameter.
     * @param offset,    the offset
     * @return the final value of the partition
     */
    public static int getPartitionFinal(int noOfParams, int offset) {
        int temp = (int) ((offset + 1) * Math.ceil((noOfParams /
                (double) HzConstants.NO_OF_PARALLEL_EXECUTIONS)));
        return temp < noOfParams ? temp : noOfParams;
    }

    /**
     * Gets the size of the specific partition
     *
     * @param noOfParams total number of entities in the specific parameter.
     * @return the size of the specific partition
     */
    public static int getPartitionSize(int noOfParams) {
        return (int) Math.ceil((noOfParams / (double) HzConstants.NO_OF_PARALLEL_EXECUTIONS));
    }
}
