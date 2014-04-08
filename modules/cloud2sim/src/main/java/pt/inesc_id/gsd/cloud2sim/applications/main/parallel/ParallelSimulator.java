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

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.compatibility.ConfigReader;
import pt.inesc_id.gsd.cloud2sim.applications.callables.DatacenterCreatorCallable;
import pt.inesc_id.gsd.cloud2sim.applications.main.dynamics.SimulationEngine;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudSim;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzCloudlet;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzDatacenterBroker;
import pt.inesc_id.gsd.cloud2sim.hazelcast.HzObjectCollection;
import pt.inesc_id.gsd.cloud2sim.util.AppUtil;
import pt.inesc_id.gsd.cloud2sim.util.OutputLogger;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * An example showing how to create
 * parallel independent simulations.
 */
public class ParallelSimulator {

    private static final int id = (int) (Math.random() * 10000);

    /**
     * Creates main() to run this example
     */
    public static void main(String[] args) {
        AppUtil.start();
        Log.printLine("# Starting the ParallelSimulator...");

        try {
            // First step: Initialize the CloudSim package. It should be called
            // before creating any entities.
            Calendar calendar = Calendar.getInstance();
            boolean trace_flag = false;  // mean trace events

            String clusterGroup = "main" + id;

            // Initialize the CloudSim library
            HzCloudSim.init(ConfigReader.getNoOfUsers(), calendar, trace_flag, clusterGroup);

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
                }
            }

            //Third step: Create Broker
            HzDatacenterBroker broker = SimulationEngine.createBroker("Broker_" + HzCloudSim.getOffset());
            int brokerId = broker.getId();

            //Fourth step: Create VMs and Cloudlets and send them to broker
            SimulationEngine.createVM(brokerId); //creating 20 vms //2000
            /* The cloudlet list. */
            SimulationEngine.createCloudlet(brokerId); //2000

            AppUtil.setNoOfCloudlets(ConfigReader.getNoOfCloudlets());
            AppUtil.setNoOfVms(ConfigReader.getNoOfVms());

            broker.submitCloudletsAndVms();


            if (AppUtil.getIsMaster()) {
                HzObjectCollection objectCollection = HzObjectCollection.getHzObjectCollection();
                while (objectCollection.getCloudletList().size() < AppUtil.getNoOfCloudlets() ||
                        objectCollection.getVmList().size() < AppUtil.getNoOfVms()) {
                    Thread.sleep(1000);
                }
                // Fifth step: Starts the simulation
                HzCloudSim.startSimulation();
                // Final step: Print results when simulation is over
                Map<Integer, HzCloudlet> newList = objectCollection.getCloudletReceivedList();
                HzCloudSim.stopSimulation();
                OutputLogger.printCloudletList(newList);
                Log.printLine("# ScalableSimulator execution finished!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.printLine("# The simulation has been terminated due to an unexpected error");
        }
        AppUtil.shutdown();
    }
}
