/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.applications.main.parallel;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.compatibility.hazelcast.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.applications.roundrobin.RoundRobinDatacenterBroker;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudlet;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzDatacenterBroker;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzVm;

/**
 * The class that creates VMs and Cloudlets, with hard-coded values. Replace as appropriate.
 */
public class SimulationEngine {
    private static boolean isRR = ConfigReader.getIsRR();
    private static HzObjectCollection objectCollection = HzObjectCollection.getHzObjectCollection();

    /**
     * Create a vm with the values
     * @param userId, the user.
     */
    public static void createVM(int userId) {

        //VM Parameters
        long size = 10000; //image size (MB)
        int ram = 128; //vm memory (MB)
        int mips = 200;
        long bw = 1000;
        int pesNumber = 1; //number of cpus
        String vmm = "Xen"; //VMM name

        //create VMs
        HzVm vm;

        int noOfVms = ConfigReader.getNoOfVms();

        for (int i = 0; i < noOfVms; i++) {
            if (isRR) {
                vm = new HzVm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            } else {
                vm = new HzVm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            }
            objectCollection.getUserVmList().put(i, vm);
        }
    }

    /**
     * Create a cloudlet with the parameters
     * @param userId, the user.
     */
    public static void createCloudlet(int userId) {
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
            objectCollection.getUserCloudletList().put(i, cloudlet);
        }
    }

    /**
     * Create a datacenter broker
     * @param name, the broker name
     * @return the broker
     * @throws Exception, if broker creation failed.
     */
    public static HzDatacenterBroker createBroker(String name) throws Exception {
        if (isRR) {
            return new RoundRobinDatacenterBroker(name);
        } else {
            return new HzDatacenterBroker(name);
        }
    }
}
