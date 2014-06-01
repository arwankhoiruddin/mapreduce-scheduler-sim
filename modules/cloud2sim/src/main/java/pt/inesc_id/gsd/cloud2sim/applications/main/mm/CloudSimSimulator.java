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
import org.cloudbus.cloudsim.core.CloudSim;
import pt.inesc_id.gsd.cloud2sim.applications.callables.DatacenterCreatorCallable;
import pt.inesc_id.gsd.cloud2sim.applications.roundrobin.RoundRobinDatacenterBroker;
import pt.inesc_id.gsd.cloud2sim.core.Cloud2SimEngine;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * An example showing how to create
 * scalable static simulations.
 */
public class CloudSimSimulator {

    /**
     * The cloudlet list.
     */
    private static List<Cloudlet> cloudletList;

    /**
     * The vmlist.
     */
    private static List<Vm> vmlist;

    private static boolean isPU = true;


    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        Cloud2SimEngine.start();
        Log.printLine("# Starting the ScalableSimulator...");

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            // Initialize the CloudSim library
            CloudSim.init(ConfigReader.getNoOfUsers(), calendar, trace_flag);

            // Second step: Create Datacenters
            //Datacenters are the resource providers in CloudSim. We need at least one of them to run a CloudSim simulation

            List<Datacenter> datacenters = new ArrayList<Datacenter>();
            int noOfDatacenters = ConfigReader.getNoOfDatacenters();
            ExecutorService pool = Executors.newFixedThreadPool(noOfDatacenters);
            for (int i = 0; i < noOfDatacenters; i++) {
                Callable<Datacenter> callable = new DatacenterCreatorCallable("Datacenter_" + i);
                Future<Datacenter> future = pool.submit(callable);
                try {
                    datacenters.add(future.get());
                    //waits for the thread to complete, even if it hasn't started
                } catch (ExecutionException e) {
                    Log.printConcatLine("Execution Exception, waiting for the data center creation: ", e);
                    throw e;
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

            //Third step: Create Broker
            DatacenterBroker broker = new DatacenterBroker("Broker");
            int brokerId = broker.getId();

            System.out.println("here");
            //Fourth step: Create VMs and Cloudlets and send them to broker
            vmlist = CloudSimSimulationEngine.createVM(brokerId); //creating 20 vms //2000
            System.out.println("hereeeeee");
            /* The cloudlet list. */
            cloudletList = CloudSimSimulationEngine.createCloudlet(brokerId); //2000
            System.out.println("hereeeeee too");

            if (isPU) {

                for (int i = 0; i < cloudletList.size(); i++) {
                    long size = 0;
                    long tempSize;
                    int returnKey = -1;
                    for (int j = 0; j < vmlist.size(); j++) {

                        tempSize = vmlist.get(j).getSize();
                        if (size == 0) {
                            returnKey = j;
                            size = tempSize;
                        } else if (tempSize < size) {
                            returnKey = j;
                            size = tempSize;
                        }
                    }
                    if (returnKey != -1) {
                        cloudletList.get(i).setVmId(returnKey);
                    }
                }
            }

            broker.submitVmList(vmlist);
            broker.submitCloudletList(cloudletList);

            // Fifth step: Starts the simulation
            CloudSim.startSimulation();

            // Final step: Print results when simulation is over
            List<Cloudlet> newList = broker.getCloudletReceivedList();

            CloudSim.stopSimulation();
            printCloudletList(newList);

            Log.printLine("# CloudSimExample6 finished!");
            long endTime = System.currentTimeMillis();
            double totalTimeTaken = (endTime - startTime) / 1000.0;
            Log.printLine("The total time taken for the execution: " + totalTimeTaken + " s.");

        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("# The simulation has been terminated due to an unexpected error");
        }
    }

    /**
     * Prints the Cloudlet objects
     *
     * @param list list of Cloudlets
     */
    private static void printCloudletList(List<Cloudlet> list) {
        int size = list.size();
        Cloudlet cloudlet;

        String indent = "    ";
        Log.printLine();
        Log.printLine("# ========== OUTPUT ==========");
        Log.printLine("# Cloudlet ID" + indent + "STATUS" + indent +
                "Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent +
                "Start Time" + indent + "Finish Time"
                + indent + "Submission Time" + indent + "Processing Cost");

        DecimalFormat dft = new DecimalFormat("###.##");
        System.out.println(size);
        for (int i = 0; i < size; i++) {
            cloudlet = list.get(i);
            Log.print(indent + cloudlet.getCloudletId() + indent + indent);

            if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS) {
                Log.print("SUCCESS");

                Log.printLine(indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
                        indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
                        indent + indent + dft.format(cloudlet.getExecStartTime()) + indent + indent + indent + dft.format(cloudlet.getFinishTime())
                        + indent + indent + dft.format(cloudlet.getSubmissionTime()) + indent + indent + dft.format(cloudlet.getCostPerSec()));
            }
            Log.printLine();
        }

    }
}
