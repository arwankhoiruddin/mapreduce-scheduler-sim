/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.examples.cloud2sim;

import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.app.AppBuilder;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.app.OutputLogger;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;
import org.cloudbus.cloudsim.examples.cloud2sim.callables.DatacenterCreatorCallable;
import org.cloudbus.cloudsim.examples.cloud2sim.constants.SimulationConstants;
import org.cloudbus.cloudsim.examples.cloud2sim.roundrobin.RoundRobinDatacenterBroker;

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
public class CloudSimExample6 {

    private static int offset;

    private static void createVM(int userId) {

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 128; //vm memory (MB)
        int mips = 200;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        Vm vm;

        int init = AppBuilder.getPartitionInit(SimulationConstants.noOfVms, offset);
        int end = AppBuilder.getPartitionFinal(SimulationConstants.noOfVms, offset);

        AppUtil.setVmsInit(init);
        AppUtil.setVmsFinal(end);

        for (int i = init; i < end; i++) {
            if (SimulationConstants.isRR) {
                vm = new Vm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            } else {
                vm = new Vm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            }
            HzObjectCollection.getUserVmList().put(i, vm);
        }
    }

    private static void createCloudlet(int userId) {
        //cloudlet parameters
        long length = 10; //100
        long fileSize = 30; // 300
        long outputSize = 30; // 300
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        Cloudlet cloudlet;

        int init = AppBuilder.getPartitionInit(SimulationConstants.noOfCloudlets, offset);
        int end = AppBuilder.getPartitionFinal(SimulationConstants.noOfCloudlets, offset);

        AppUtil.setCloudletsInit(init);
        AppUtil.setCloudletsFinal(end);

        for (int i = init; i < end; i++) {
            int f = (int) ((Math.random() * 40) + 1);
            cloudlet = new Cloudlet(i, length * f, pesNumber, fileSize, outputSize, utilizationModel,
                    utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet.setUserId(userId);
            HzObjectCollection.getUserCloudletList().put(i, cloudlet);
        }
    }

    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {
        AppUtil.start();
        Log.printLine("# Starting CloudSimExample6...");

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            int num_user = 200;   // number of grid users
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(num_user, calendar, trace_flag);
            offset = AppUtil.getOffset();

            AppBuilder.initWorkers(offset);

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
            DatacenterBroker broker = createBroker("Broker_" + offset);
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            createVM(brokerId); //creating 20 vms //2000
            /* The cloudlet list. */
            createCloudlet(brokerId); //2000

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
                Log.printLine("# CloudSimExample6 finished!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("# The simulation has been terminated due to an unexpected error");
        }
        AppUtil.shutdownLogs();
    }

    private static DatacenterBroker createBroker(String name) throws Exception {
        if (SimulationConstants.isRR) {
            return new RoundRobinDatacenterBroker(name);
        } else {
            return new DatacenterBroker(name);
        }
    }
}
