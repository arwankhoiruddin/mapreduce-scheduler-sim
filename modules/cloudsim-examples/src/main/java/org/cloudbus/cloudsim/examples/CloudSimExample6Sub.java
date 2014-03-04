/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.examples;

import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.constants.Cloud2SimConstants;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * An example showing how to create
 * scalable simulations.
 */
public class CloudSimExample6Sub {
    private static int noOfCloudlets = 2000;
    private static int noOfVms = 2000;

	private static void createVM(int userId, int vms) {

		//VM Parameters
		long size = 10000; //image size (MB)
		int ram = 128; //vm memory (MB)
		int mips = 200;
		long bw = 1000;
		int pesNumber = 1; //number of cpus
		String vmm = "Xen"; //VMM name

        int vmsHere = vms / HazelSimConstants.EXECUTIONS_PER_NODE;
		//create VMs
		Vm[] vm = new Vm[vmsHere];
        AppUtil.setVmsInit(0);
        AppUtil.setVmsFinal(vmsHere - 1);

		for(int i=0;i<vmsHere;i++){
			vm[i] = new Vm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
  			HzObjectCollection.getUserVmList().put(i, vm[i]);
		}
	}


	private static void createCloudlet(int userId, int cloudlets){
		// Creates a container to store Cloudlets

		//cloudlet parameters
		long length = 10; //100
		long fileSize = 30; // 300
		long outputSize = 30; // 300
		int pesNumber = 1;
		UtilizationModel utilizationModel = new UtilizationModelFull();

        int cloudletsHere = cloudlets / HazelSimConstants.EXECUTIONS_PER_NODE;

        Cloudlet[] cloudlet = new Cloudlet[cloudletsHere];

        AppUtil.setCloudletsInit(0);
        AppUtil.setCloudletsFinal(cloudletsHere - 1);

		for(int i=0;i<cloudletsHere;i++){
            int f = (int) ((Math.random() * 40) + 1);
			cloudlet[i] = new Cloudlet(i, length*f, pesNumber, fileSize, outputSize, utilizationModel,
                    utilizationModel, utilizationModel);
			// setting the owner of these Cloudlets
			cloudlet[i].setUserId(userId);
            HzObjectCollection.getUserCloudletList().put(i, cloudlet[i]);
		}
	}

	////////////////////////// STATIC METHODS ///////////////////////

	/**
	 * Creates main() to run this example
	 */
	public static void main(String[] args) {
        AppUtil.start();
        AppUtil.setIsMaster(false);
        AppUtil.setIsPrimaryWorker(true);
		Log.printLine("# Starting CloudSimExample6...");

		try {
			// First step: Initialize the CloudSim package. It should be called
			// before creating any entities.
			int num_user = 200;   // number of grid users
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;  // mean trace events

			// Initialize the CloudSim library
			CloudSim.init(num_user, calendar, trace_flag);

			// Second step: Create Datacenters
			//Datacenters are the resource providers in CloudSim. We need at least one of them to run a CloudSim simulation
			@SuppressWarnings("unused")
			Datacenter datacenter0 = createDatacenter("Datacenter_0");
//			@SuppressWarnings("unused")
//			Datacenter datacenter1 = createDatacenter("Datacenter_1");
//
			//Third step: Create Broker
			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			//Fourth step: Create VMs and Cloudlets and send them to broker
			createVM(brokerId,noOfVms); //creating 20 vms //2000
			/* The cloudlet list. */
            createCloudlet(brokerId, noOfCloudlets); //2000

            AppUtil.setNoOfCloudlets(noOfCloudlets);
            AppUtil.setNoOfVms(noOfVms);

//
//            long startTime = System.currentTimeMillis();
//
            broker.submitCloudletsAndVms();
//
//            long endTime = System.currentTimeMillis();
//            double totalTimeTaken = (endTime - startTime)/1000.0;
//            System.out.println("Total time taken for submitting the lists: " + totalTimeTaken);
//
//			// Fifth step: Starts the simulation
//			CloudSim.startSimulation();
//
//			// Final step: Print results when simulation is over
//			Map<Integer, Cloudlet> newList = HzObjectCollection.getCloudletReceivedList();
//
//			CloudSim.stopSimulation();
//
//			printCloudletList(newList);
//
			Log.printLine("# CloudSimExample6 finished!");
		} catch (Exception e) {
            e.printStackTrace();
            Log.printLine("# The simulation has been terminated due to an unexpected error");
        }
        AppUtil.shutdownLogs();
    }

	private static Datacenter createDatacenter(String name){

		// Here are the steps needed to create a PowerDatacenter:
		// 1. We need to create a list to store one or more
		//    Machines
		List<Host> hostList = new ArrayList<Host>();

		// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
		//    create a list to store these PEs before creating
		//    a Machine.
		List<Pe> peList1 = new ArrayList<Pe>();

		int mips = 400000;

		// 3. Create PEs and add these into the list.
		//for a quad-core machine, a list of 4 PEs is required:
		peList1.add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
		peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
		peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

		//Another list, for a dual-core machine
		List<Pe> peList2 = new ArrayList<Pe>();

		peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
		peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

		//4. Create Hosts with its id and list of PEs and add them to the list of machines
		int hostId=0;
		int ram = 2048000; //host memory (MB)
		long storage = 400000000; //host storage
		int bw = 4000000;

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

		hostId++;

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


//		To create a host with a space-shared allocation policy for PEs to VMs:
/*		hostList.add(
    			new Host(
    				hostId,
    				new RamProvisionerSimple(ram),
    				new BwProvisionerSimple(bw),
    				storage,
                    peList1,
    				new VmSchedulerSpaceShared(peList1)
    			)
    		);
		hostId++;

        hostList.add(
            new Host(
                hostId,
                new RamProvisionerSimple(ram),
                new BwProvisionerSimple(bw),
                storage,
                peList2,
                new VmSchedulerSpaceShared(peList2)
            )
        );
 */
        // 5. Create a DatacenterCharacteristics object that stores the
		//    properties of a data center: architecture, OS, list of
		//    Machines, allocation policy: time- or space-shared, time zone
		//    and its price (G$/Pe time unit).
		String arch = "x86";      // system architecture
		String os = "Windows";          // operating system
		String vmm = "Xen";
		double time_zone = 10.0;         // time zone this resource located
		double cost = 3.0;              // the cost of using processing in this resource
		double costPerMem = 0.05;		// the cost of using memory in this resource
		double costPerStorage = 0.1;	// the cost of using storage in this resource
		double costPerBw = 0.1;			// the cost of using bw in this resource
		LinkedList<Storage> storageList = new LinkedList<Storage>();	//we are not adding SAN devices by now

		DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


		// 6. Finally, we need to create a PowerDatacenter object.
		Datacenter datacenter = null;
		try {
			datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datacenter;
	}

	//We strongly encourage users to develop their own broker policies, to submit vms and cloudlets according
	//to the specific rules of the simulated scenario
	private static DatacenterBroker createBroker(){

		DatacenterBroker broker;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}

    /**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	private static void printCloudletList(Map<Integer, Cloudlet> list) {
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("# ========== OUTPUT ==========");
		Log.printLine("# Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent +
                "Start Time" + indent + "Finish Time"
                + indent + "Submission Time" + indent + "Processing Cost");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (IMap.Entry<Integer, Cloudlet> entry: list.entrySet()) {
			cloudlet = entry.getValue();
            int cloudletId = entry.getKey();

			Log.print(indent + cloudletId + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloud2SimConstants.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
						indent + indent + dft.format(cloudlet.getExecStartTime())+ indent + indent + indent + dft.format(cloudlet.getFinishTime())
                                             + indent + indent + dft.format(cloudlet.getSubmissionTime())+ indent + indent + dft.format(cloudlet.getCostPerSec()));
            }
		}

	}
}
