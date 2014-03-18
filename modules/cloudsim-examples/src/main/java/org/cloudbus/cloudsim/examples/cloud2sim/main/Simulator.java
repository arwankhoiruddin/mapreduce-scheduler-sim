/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.examples.cloud2sim.main;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.app.AppBuilder;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.app.OutputLogger;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.hazelcast.HzObjectCollection;
import org.cloudbus.cloudsim.examples.cloud2sim.callables.DatacenterCreatorCallable;
import org.cloudbus.cloudsim.examples.cloud2sim.constants.SimulationConstants;
import org.cloudbus.cloudsim.examples.cloud2sim.core.SimulationEngine;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * An example showing how to create
 * scalable simulations.
 */
public class Simulator {

    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {
        AppUtil.start();
        Log.printLine("# Starting the Simulator...");

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(SimulationConstants.numUser, calendar, trace_flag);
            SimulationEngine.offset = AppUtil.getOffset();

            AppBuilder.initWorkers(SimulationEngine.offset);

            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at least one of them to run a CloudSim simulation

            List<Datacenter> datacenters = new ArrayList<Datacenter>();
            ExecutorService pool = Executors.newFixedThreadPool(SimulationConstants.noOfDatacenters);
            for (int i = 0; i < SimulationConstants.noOfDatacenters; i++) {
                Callable<Datacenter> callable = new DatacenterCreatorCallable("Datacenter_" + i);
                Future<Datacenter> future = pool.submit(callable);
                try {
                    datacenters.add(future.get());
                    //waits for the thread to complete, even if it hasn't started
                } catch (ExecutionException e) {
                    Log.printConcatLine("Execution Exception, waiting for the data center creation: ", e);
                    throw e;
                }
            }

            //Third step: Create Broker
            DatacenterBroker broker = SimulationEngine.createBroker("Broker_" + SimulationEngine.offset);
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            SimulationEngine.createVM(brokerId); //creating 20 vms //2000
            /* The cloudlet list. */
            SimulationEngine.createCloudlet(brokerId); //2000

            AppUtil.setNoOfCloudlets(SimulationConstants.noOfCloudlets);
            AppUtil.setNoOfVms(SimulationConstants.noOfVms);

            broker.submitCloudletsAndVms();

            if (AppUtil.getIsMaster()) {
                // Fifth step: Starts the simulation
                CloudSim.startSimulation();
                // Final step: Print results when simulation is over
                Map<Integer, Cloudlet> newList = HzObjectCollection.getCloudletReceivedList();
                CloudSim.stopSimulation();
                OutputLogger.printCloudletList(newList);
                Log.printLine("# Simulator execution finished!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("# The simulation has been terminated due to an unexpected error");
        }
        AppUtil.shutdownLogs();
    }
}
