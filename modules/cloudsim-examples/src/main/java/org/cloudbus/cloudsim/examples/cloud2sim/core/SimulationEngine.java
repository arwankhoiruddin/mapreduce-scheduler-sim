/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.examples.cloud2sim.core;

import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.app.AppBuilder;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.app.ConfigReader;
import org.cloudbus.cloudsim.hazelcast.core.HazelSim;
import org.cloudbus.cloudsim.hazelcast.HzCloudSim;
import org.cloudbus.cloudsim.hazelcast.HzCloudlet;
import org.cloudbus.cloudsim.hazelcast.HzDatacenterBroker;
import org.cloudbus.cloudsim.examples.cloud2sim.roundrobin.RoundRobinDatacenterBroker;
import org.cloudbus.cloudsim.hazelcast.HzVm;

public class SimulationEngine {
    private static boolean isRR = ConfigReader.getIsRR();
    private static HazelSim objectCollection = HazelSim.getHazelSim();

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
