/**
 * https://gist.github.com/alessandroleite
 * Copyright 2013 team XPD.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cloudbus.cloudsim.examples.roundrobin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
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
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.constants.Cloud2SimConstants;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;


public class CloudSimExample9 {
    private static int noOfVms = 5;
    private static int noOfCloudlets = 20000;


    private static void createVM(int userId, int vms) {
        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 512; //vm memory (MB)
        int mips = 250;
        long bw = 1000;
        int pesNumber = 1; //number of cpus       - 1
        String vmm = "Xen"; //VMM name

        int vmsInit = (HazelSimConstants.NO_OF_PARALLEL_EXECUTIONS - 1) *
                vms / HazelSimConstants.NO_OF_PARALLEL_EXECUTIONS;
        AppUtil.setVmsInit(vmsInit);
        AppUtil.setVmsFinal(vms - 1);
        //create VMs
        Vm[] vm = new Vm[vms - vmsInit];

        for(int i = 0; i < (vms - vmsInit); i++){
            vm[i] = new Vm(vmsInit + i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            HzObjectCollection.getUserVmList().put(vmsInit + i, vm[i]);
        }
    }


    private static void createCloudlet(int userId, int cloudlets) {
// Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

//cloudlet parameters
        long length = 40000;
        long fileSize = 300;
        long outputSize = 300;
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        int cloudletsInit = (HazelSimConstants.NO_OF_PARALLEL_EXECUTIONS - 1) *
                (cloudlets / HazelSimConstants.NO_OF_PARALLEL_EXECUTIONS);

        AppUtil.setCloudletsInit(cloudletsInit);
        AppUtil.setCloudletsFinal(cloudlets - 1);

        Cloudlet[] cloudlet = new Cloudlet[cloudlets - cloudletsInit];

        for(int i=0;i<(cloudlets - cloudletsInit);i++){
            int f = (int) ((Math.random() * 40) + 1);
            cloudlet[i] = new Cloudlet(cloudletsInit + i, length*f, pesNumber, fileSize, outputSize, utilizationModel,
                    utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet[i].setUserId(userId);
            HzObjectCollection.getUserCloudletList().put(cloudletsInit + i, cloudlet[i]);
        }
    }


    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {
        AppUtil.start();
        AppUtil.setIsMaster(true);
        if (HazelSimConstants.NO_OF_PARALLEL_EXECUTIONS > 1) {
            AppUtil.setIsPrimaryWorker(false);
        }

        Log.printLine("Starting CloudSimExample9...");

        try {
            int num_user = 2; // number of grid users                 2
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false; // mean trace events

            CloudSim.init(num_user, calendar, trace_flag);

            @SuppressWarnings("unused")
            Datacenter datacenter0 = createDatacenter("Datacenter_0");
            @SuppressWarnings("unused")
            Datacenter datacenter1 = createDatacenter("Datacenter_1");

            DatacenterBroker broker = createBroker("Broker_0");
            int brokerId = broker.getId();

            createVM(brokerId, noOfVms); //creating 5 vms
            createCloudlet(brokerId, noOfCloudlets); // creating 20000 cloudlets

            AppUtil.setNoOfCloudlets(noOfCloudlets);
            AppUtil.setNoOfVms(noOfVms);

            broker.submitCloudletsAndVms();

            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            Map<Integer, Cloudlet> newList = HzObjectCollection.getCloudletReceivedList();

            CloudSim.stopSimulation();
            printCloudletList(newList);

            Log.printLine(CloudSimExample9.class.getName() + " finished!");
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("The simulation has been terminated due to an unexpected error");
        }
        AppUtil.shutdown();
    }

    private static Datacenter createDatacenter(String name) {

        List<Host> hostList = new ArrayList<Host>();

// 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
// create a list to store these PEs before creating
// a Machine.
        List<Pe> peList1 = new ArrayList<Pe>();

        int mips = 1000;

// 3. Create PEs and add these into the list.
//for a quad-core machine, a list of 4 PEs is required:
        peList1
            .add(new Pe(0, new PeProvisionerSimple(mips))); // need to store Pe id and MIPS Rating
        peList1.add(new Pe(1, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(2, new PeProvisionerSimple(mips)));
        peList1.add(new Pe(3, new PeProvisionerSimple(mips)));

//Another list, for a dual-core machine
        List<Pe> peList2 = new ArrayList<Pe>();

        peList2.add(new Pe(0, new PeProvisionerSimple(mips)));
        peList2.add(new Pe(1, new PeProvisionerSimple(mips)));

//4. Create Hosts with its id and list of PEs and add them to the list of machines
        int hostId = 0;
        int ram = 16384; //host memory (MB)
        long storage = 1000000; //host storage
        int bw = 10000;

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

// 5. Create a DatacenterCharacteristics object that stores the
// properties of a data center: architecture, OS, list of
// Machines, allocation policy: time- or space-shared, time zone
// and its price (G$/Pe time unit).
        String arch = "x86"; // system architecture
        String os = "Linux"; // operating system
        String vmm = "Xen";
        double time_zone = 10.0; // time zone this resource located
        double cost = 3.0; // the cost of using processing in this resource
        double costPerMem = 0.05;    // the cost of using memory in this resource
        double costPerStorage = 0.1;    // the cost of using storage in this resource
        double costPerBw = 0.1;    // the cost of using bw in this resource
        LinkedList<Storage> storageList = new LinkedList<Storage>();    //we are not adding SAN devices by now

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
            arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);


// 6. Finally, we need to create a PowerDatacenter object.
        Datacenter datacenter = null;
        try {
            RoundRobinVmAllocationPolicy vm_policy = new RoundRobinVmAllocationPolicy(hostList);
            datacenter = new Datacenter(name, characteristics, vm_policy, storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datacenter;
    }

    private static DatacenterBroker createBroker(String name) throws Exception {
        return new RoundRobinDatacenterBroker(name);
    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     */
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
