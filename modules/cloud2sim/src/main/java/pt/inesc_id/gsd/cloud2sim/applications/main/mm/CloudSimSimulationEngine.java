/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *                Toolkit for Modeling and Simulation
 *                of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.applications.main.mm;

import org.cloudbus.cloudsim.*;
import org.cloudbus.cloudsim.compatibility.common.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.applications.roundrobin.RoundRobinDatacenterBroker;
import pt.inesc_id.gsd.cloud2sim.hazelcast.*;

import java.util.LinkedList;
import java.util.List;

/**
 * The class that creates VMs and Cloudlets, with hard-coded values. Replace as appropriate.
 */
public class CloudSimSimulationEngine {
    private static boolean isRR = ConfigReader.getIsRR();
    private static HzObjectCollection objectCollection = HzObjectCollection.getHzObjectCollection();

    /**
     * Create a VM with the parameters
     * @param userId, the user.
     */
    public static List<Vm> createVM(int userId) {

        //Creates a container to store VMs. This list is passed to the broker later
        LinkedList<Vm> list = new LinkedList<Vm>();
        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 128; //vm memory (MB)
        int mips = 200;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        Vm vm;

        int noOfVms = ConfigReader.getNoOfVms();


        for (int i = 0; i < noOfVms; i++) {
            if (isRR) {
                vm = new Vm(i, userId, mips, pesNumber, ram, bw, (int) ((Math.random() * size) + 1), vmm, new CloudletSchedulerTimeShared());
            } else {
                vm = new Vm(i, userId, mips, pesNumber, ram, bw, (int) ((Math.random() * size) + 1), vmm, new CloudletSchedulerSpaceShared());
            }
            list.add(vm);
        }
        return list;
    }

    /**
     * Create a cloudlet with the parameters
     * @param userId, the user.
     */
    public static List<Cloudlet> createCloudlet(int userId) {
        // Creates a container to store Cloudlets
        LinkedList<Cloudlet> list = new LinkedList<Cloudlet>();

        //cloudlet parameters
        long length = 10; //100
        long fileSize = 30; // 300
        long outputSize = 30; // 300
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        HzCloudlet cloudlet;

        int noOfCloudlets = ConfigReader.getNoOfCloudlets();

        for (int i = 0; i < noOfCloudlets; i++) {
            int f = (int) ((Math.random() * 40) + 1);
            cloudlet = new HzCloudlet(i, length * f, pesNumber, fileSize, outputSize, utilizationModel,
                    utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet.setUserId(userId);
            list.add(cloudlet);
        }
        return list;
    }
}
