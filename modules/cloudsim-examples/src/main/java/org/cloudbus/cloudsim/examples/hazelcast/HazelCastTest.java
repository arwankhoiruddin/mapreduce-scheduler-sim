/*
 * Title:        CloudSim Toolkit
 * Description:  CloudSim (Cloud Simulation) Toolkit for Modeling and Simulation
 *               of Clouds
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */
package org.cloudbus.cloudsim.examples.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.Cloud2SimConstants;
import org.cloudbus.cloudsim.core.HazelSim;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Queue;

public class HazelCastTest {
    public static void main(String[] args) {

        HazelSim hazelSim = HazelSim.getHazelSim(Cloud2SimConstants.NO_OF_HAZELCAST_INSTANCES);
        HazelcastInstance[] instances = hazelSim.getHazelcastInstances();

        Map<Integer, String> mapCustomers = instances[0].getMap("customers");
        mapCustomers.put(1, "Joe");
        mapCustomers.put(2, "Ali");
        mapCustomers.put(3, "Avi");
        mapCustomers.put(4, "vince");
        Map<Integer, String> mapCustomers1 = instances[1].getMap("customers");
        mapCustomers1.put(11, "Joe");
        mapCustomers1.put(21, "Ali");
        mapCustomers1.put(31, "Avi");
        mapCustomers1.put(41, "vince");
        Map<Integer, String> mapCustomers2 = instances[2].getMap("customers");
        mapCustomers2.put(112, "Joe");
        mapCustomers2.put(212, "Ali");
        mapCustomers2.put(312, "Avi");
        mapCustomers2.put(412, "vince");

//        Log.printConcatLine("Customer with key 1: "+ mapCustomers.get(1));
//        System.out.println("Map Size:" + mapCustomers.size());
//        Queue<String> queueCustomers = instances[0].getQueue("customers");
//        queueCustomers.offer("Tom");
//        queueCustomers.offer("Mary");
//        queueCustomers.offer("Jane");
//        System.out.println("First customer: " + queueCustomers.poll());
//        System.out.println("Second customer: "+ queueCustomers.peek());
//        System.out.println("Queue size: " + queueCustomers.size());
    }
}
