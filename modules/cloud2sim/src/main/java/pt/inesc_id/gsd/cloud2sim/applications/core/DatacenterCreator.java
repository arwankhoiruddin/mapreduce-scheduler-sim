/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.applications.core;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;
import org.cloudbus.cloudsim.compatibility.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.applications.roundrobin.RoundRobinVmAllocationPolicy;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The datacenter is defined here, with hard-coded values. Replace as appropriate.
 */
public class DatacenterCreator {

    public static Datacenter createDatacenter(String name) {

        // Here are the steps needed to create a PowerDatacenter:
        // 1. We need to create a list to store one or more
        //    Machines
        List<Host> hostList = new ArrayList<Host>();

        // 2. A Machine contains one or more PEs or CPUs/Cores. Therefore, should
        //    create a list to store these PEs before creating
        //    a Machine.

        int mips = 400000;
        List<Pe> peList1 = Cloud2SimEngine.createMachines(mips, 4);

        //Another list, for a dual-hazelcast machine
        List<Pe> peList2 = Cloud2SimEngine.createMachines(mips, 2);

        //4. Create Hosts with its id and list of PEs and add them to the list of machines
        int ram = 2048000; //host memory (MB)
        long storage = 400000000; //host storage
        int bw = 4000000;

        for (int hostId = 0; hostId < ConfigReader.getNoOfHosts(); hostId++) {
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
            if (ConfigReader.getIsRR()) {
                datacenter = new Datacenter(name, characteristics, new RoundRobinVmAllocationPolicy(hostList), storageList, 0);
            } else {
                datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return datacenter;
    }

}
