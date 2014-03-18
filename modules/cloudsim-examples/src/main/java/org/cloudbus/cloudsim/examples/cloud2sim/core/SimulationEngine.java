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

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerSpaceShared;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.UtilizationModel;
import org.cloudbus.cloudsim.UtilizationModelFull;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.app.AppBuilder;
import org.cloudbus.cloudsim.app.AppUtil;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;
import org.cloudbus.cloudsim.examples.cloud2sim.constants.SimulationConstants;
import org.cloudbus.cloudsim.examples.cloud2sim.roundrobin.RoundRobinDatacenterBroker;

public class SimulationEngine {
    public static int offset;

    public static void createVM(int userId) {

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

    public static void createCloudlet(int userId) {
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

    public static DatacenterBroker createBroker(String name) throws Exception {
        if (SimulationConstants.isRR) {
            return new RoundRobinDatacenterBroker(name);
        } else {
            return new DatacenterBroker(name);
        }
    }
}
