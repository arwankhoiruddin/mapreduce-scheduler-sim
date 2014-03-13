/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2009, The University of Melbourne, Australia
 */

package org.cloudbus.cloudsim.examples;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.app.AppBuilder;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.app.OutputLogger;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;
import org.cloudbus.cloudsim.examples.roundrobin.RoundRobinDatacenterBroker;
import org.cloudbus.cloudsim.examples.roundrobin.RoundRobinVmAllocationPolicy;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An example showing how to create
 * scalable simulations.
 */
public class CloudSimExample6 {
    private static int noOfVms = 2000;   //2000
    private static int noOfCloudlets = 200;  //200
    private static int noOfHosts = 200;  //200. // 2000 fills up the memory
    private static int noOfDatacenters = 1500; //1500
    private static int offset;

    public static boolean isRR = true;

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

        int init = AppBuilder.getPartitionInit(noOfVms, offset);
        int end = AppBuilder.getPartitionFinal(noOfVms, offset);

        for (int i = init; i < end; i++) {
            if (isRR) {
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

        int init = AppBuilder.getPartitionInit(noOfCloudlets, offset);
        int end = AppBuilder.getPartitionFinal(noOfCloudlets, offset);

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

            if (offset == HazelSimConstants.NO_OF_PARALLEL_EXECUTIONS - 1) {
                AppUtil.setIsPrimaryWorker(true);
            }

            if (offset == 0) {
                AppUtil.setIsMaster(true);
            }

            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at least one of them to run a CloudSim simulation

            @SuppressWarnings("unused")
            int init = AppBuilder.getPartitionInit(noOfDatacenters, offset);
            int end = AppBuilder.getPartitionFinal(noOfDatacenters, offset);

            for (int i = init; i< end; i++) {
                createDatacenter("Datacenter_" + i);
            }

            //Third step: Create Broker
            DatacenterBroker broker = createBroker("Broker_" + offset);
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            createVM(brokerId); //creating 20 vms //2000
            /* The cloudlet list. */
            createCloudlet(brokerId); //2000

            AppUtil.setNoOfCloudlets(noOfCloudlets);
            AppUtil.setNoOfVms(noOfVms);

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

    private static Datacenter createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<Host>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.

        int mips = 400000;
        List<Pe> peList1 = AppBuilder.createMachines(mips, 4);

        //Another list, for a dual-core machine
        List<Pe> peList2 = AppBuilder.createMachines(mips, 2);

        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int ram = 2048000; //host memory (MB)
        long storage = 400000000; //host storage
        int bw = 4000000;

        int init = AppBuilder.getPartitionInit(noOfHosts, offset);
        int end = AppBuilder.getPartitionFinal(noOfHosts, offset);

        for (int hostId = init; hostId < end; hostId++) {
            if (hostId % 2 == 0) {
                hostList.add(
                        new Host(
                                hostId,
                                new RamProvisionerSimple(ram),
                                new BwProvisionerSimple(bw),
                                storage,
                                peList1,
                                new VmSchedulerTimeShared(peList1)
                        )
                ); // This is our first machine
            } else {
                hostList.add(
                        new Host(
                                hostId,
                                new RamProvisionerSimple(ram),
                                new BwProvisionerSimple(bw),
                                storage,
                                peList2,
                                new VmSchedulerTimeShared(peList2)
                        )
                ); // Second machine
            }
        }


        // 5. Create a DatacenterCharacteristics object that stores the
        //    properties of a data center: architecture, OS, list of
        //    Machines, allocation policy: time- or space-shared, time zone
        //    and its price (G$/Pe time unit).
        String arch = "x86";      // system architecture
        String os = "Windows";          // operating system
        String vmm = "Xen";
        double time_zone = 10.0;         // time zone this resource located
        double cost = 3.0;              // the cost of using processing in this resource
        double costPerMem = 0.05;        // the cost of using memory in this resource
        double costPerStorage = 0.1;    // the cost of using storage in this resource
        double costPerBw = 0.1;            // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();    //we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


        // 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            if (isRR) {
                datacenter = new Datacenter(name, characteristics, new RoundRobinVmAllocationPolicy(hostList), storageList, 0);
            } else {
                datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

    private static DatacenterBroker createBroker(String name) throws Exception {
        if (isRR) {
            return new RoundRobinDatacenterBroker(name);
        } else {
            return new DatacenterBroker(name);
        }
    }

}
