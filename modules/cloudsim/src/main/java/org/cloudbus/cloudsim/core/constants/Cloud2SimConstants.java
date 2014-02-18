/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */
package org.cloudbus.cloudsim.core.constants;

/**
 * Public constants attributes used in Cloud2Sim.
 */
public class Cloud2SimConstants {
    /** The Cloudlet has been created and added to the Cloudlet List object. */
    public static final int CREATED = 0;
    /** The Cloudlet has been assigned to a CloudResource object as planned. */
    public static final int READY = 1;
    /** The Cloudlet has moved to a Cloud node. */
    public static final int QUEUED = 2;
    /** The Cloudlet is in execution in a Cloud node. */
    public static final int INEXEC = 3;
    /** The Cloudlet has been executed successfully. */
    public static final int SUCCESS = 4;
    /** The Cloudlet is failed. */
    public static final int FAILED = 5;
    /** The Cloudlet has been canceled. */
    public static final int CANCELED = 6;
    /**
     * The Cloudlet has been paused. It can be resumed by changing the status into <tt>RESUMED</tt>.
     */
    public static final int PAUSED = 7;
    /** The Cloudlet has been resumed from <tt>PAUSED</tt> state. */
    public static final int RESUMED = 8;
    /** The cloudlet has failed due to a resource failure. */
    public static final int FAILED_RESOURCE_UNAVAILABLE = 9;
}
