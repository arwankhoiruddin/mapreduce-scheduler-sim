/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */
package org.cloudbus.cloudsim.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.FileSystemXmlConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletScheduler;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.constants.HazelSimConstants;
import org.cloudbus.cloudsim.serializer.CloudletSchedulerXmlSerializer;
import org.cloudbus.cloudsim.serializer.CloudletXmlSerializer;
import org.cloudbus.cloudsim.serializer.DatacenterXmlSerializer;
import org.cloudbus.cloudsim.serializer.HostXmlSerializer;
import org.cloudbus.cloudsim.serializer.VmXmlSerializer;

import java.io.FileNotFoundException;

/**
 * A singleton that integrates Hazelcast into Cloud2Sim and initiates Hazelcast.
 */
public class HazelSimCore {
    private static HazelSimCore hazelSimCore;
    private static Config cfg;
    protected static HazelcastInstance[] instances;

    /**
     * Protected constructor to avoid instantiation of the singleton class
     */
    protected HazelSimCore() {
        SerializerConfig sc = new SerializerConfig().setImplementation(new VmXmlSerializer()).
            setTypeClass(Vm.class);
        SerializerConfig sc0 = new SerializerConfig().setImplementation(new CloudletXmlSerializer()).
            setTypeClass(Cloudlet.class);
        SerializerConfig sc1 = new SerializerConfig().setImplementation(
            new CloudletSchedulerXmlSerializer()).setTypeClass(CloudletScheduler.class);
        SerializerConfig sc2 = new SerializerConfig().setImplementation(
            new HostXmlSerializer()).setTypeClass(Host.class);
        SerializerConfig sc3 = new SerializerConfig().setImplementation(
            new DatacenterXmlSerializer()).setTypeClass(Datacenter.class);

        try {
            cfg = new FileSystemXmlConfig(HazelSimConstants.HAZELCAST_CONFIG_FILE);
        } catch (FileNotFoundException e) {
            Log.printConcatLine(HazelSimConstants.HAZELCAST_CONFIG_FILE_NOT_FOUND_ERROR);
            cfg = new Config();
        }
        cfg.getSerializationConfig().addSerializerConfig(sc);
        cfg.getSerializationConfig().addSerializerConfig(sc0);
        cfg.getSerializationConfig().addSerializerConfig(sc1);
        cfg.getSerializationConfig().addSerializerConfig(sc2);
        cfg.getSerializationConfig().addSerializerConfig(sc3);
        // wait for all executions to come alive
        cfg.setProperty("hazelcast.initial.min.cluster.size", String.valueOf(HazelSimConstants.NO_OF_PARALLEL_EXECUTIONS));
        cfg.setProperty("hazelcast.operation.call.timeout.millis", "50000000");
    }

    /**
     * Creates a HazelSimCore object and initializes an array of Hazelcast instances.
     * @param instanceCount No. of Hazelcast instances.
     * @return the hazelsim object.
     */
    public static HazelSimCore getHazelSim(int instanceCount) {
        if (hazelSimCore == null) {
            hazelSimCore = new HazelSimCore();
        }
        instances = new HazelcastInstance[instanceCount];
        for (int i = 0; i < instanceCount; i++) {
            instances[i] = Hazelcast.newHazelcastInstance(cfg);
        }
        return hazelSimCore;
    }

    /**
     * Gets the hazelcast instances.
     * @return the hazelcast instances.
     */
    public HazelcastInstance[] getHazelcastInstances() {
        return instances;
    }
}

