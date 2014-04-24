/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.compatibility.infinispan;

/**
 * The core class of Infinispan integration
 */
public class InfiniSim {
    private static InfiniSim infiniSim = null;

    protected InfiniSim() {}

    /**
     * Creates a HazelSim object and initializes an array of Hazelcast instances.
     * @return the hazelsim object.
     */
    public static InfiniSim getInfiniSim() {
        if (infiniSim == null) {
            infiniSim = new InfiniSim();
        }
        return infiniSim;
    }
}
