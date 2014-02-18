/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */
package org.cloudbus.cloudsim.serializer;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import org.cloudbus.cloudsim.Vm;

import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class VmXmlSerializer implements StreamSerializer<Vm> {

    @Override
    public int getTypeId() {
        return 14;
    }

    @Override
    public void write(ObjectDataOutput out, Vm object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(bos);
        String[] propertyNames = new String[] { "id", "userId", "mips", "numberOfPes", "ram", "bw", "size", "vmm",
                "cloudletScheduler" };
        encoder.setPersistenceDelegate(Vm.class, new DefaultPersistenceDelegate(propertyNames));

        encoder.writeObject(object);
        encoder.close();
        out.write(bos.toByteArray());
    }

    @Override
    public Vm read(ObjectDataInput in) throws IOException {
        final InputStream inputStream = (InputStream) in;
        XMLDecoder decoder = new XMLDecoder(inputStream);
        return (Vm) decoder.readObject();
    }

    @Override
    public void destroy() {
    }
}
