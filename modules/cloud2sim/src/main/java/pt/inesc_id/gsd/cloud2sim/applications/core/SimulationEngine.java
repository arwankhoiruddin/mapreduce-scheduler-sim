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

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;
import pt.inesc_id.gsd.cloud2sim.util.AppBuilder;
import pt.inesc_id.gsd.cloud2sim.util.AppUtil;
import org.cloudbus.cloudsim.compatibility.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudSim;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudlet;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzDatacenterBroker;
import pt.inesc_id.gsd.cloud2sim.applications.roundrobin.RoundRobinDatacenterBroker;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzVm;

public class SimulationEngine {
    private static boolean isRR = ConfigReader.getIsRR();
    private static HzObjectCollection objectCollection = HzObjectCollection.getHzObjectCollection();

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
        int init = AppBuilder.getPartitionInit(noOfVms, HzCloudSim.getOffset());
        int end = AppBuilder.getPartitionFinal(noOfVms, HzCloudSim.getOffset());

        AppUtil.setVmsInit(init);
        AppUtil.setVmsFinal(end);

        for (int i = init; i < end; i++) {
            if (isRR) {
                vm = new HzVm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared());
            } else {
                vm = new HzVm(i, userId, mips, pesNumber, ram, bw, size, vmm, new CloudletSchedulerSpaceShared());
            }
            objectCollection.getUserVmList().put(i, vm);
        }
    }

    public static void createCloudlet(int userId) {
        //cloudlet parameters
        long length = 10; //100
        long fileSize = 30; // 300
        long outputSize = 30; // 300
        int pesNumber = 1;
        UtilizationModel utilizationModel = new UtilizationModelFull();

        HzCloudlet cloudlet;

        int noOfCloudlets = ConfigReader.getNoOfCloudlets();
        int init = AppBuilder.getPartitionInit(noOfCloudlets, HzCloudSim.getOffset());
        int end = AppBuilder.getPartitionFinal(noOfCloudlets, HzCloudSim.getOffset());

        AppUtil.setCloudletsInit(init);
        AppUtil.setCloudletsFinal(end);

        for (int i = init; i < end; i++) {
            int f = (int) ((Math.random() * 40) + 1);
            cloudlet = new HzCloudlet(i, length * f, pesNumber, fileSize, outputSize, utilizationModel,
                    utilizationModel, utilizationModel);
            // setting the owner of these Cloudlets
            cloudlet.setUserId(userId);
            objectCollection.getUserCloudletList().put(i, cloudlet);
        }
    }

    public static HzDatacenterBroker createBroker(String name) throws Exception {
        if (isRR) {
            return new RoundRobinDatacenterBroker(name);
        } else {
            return new HzDatacenterBroker(name);
        }
    }
}
