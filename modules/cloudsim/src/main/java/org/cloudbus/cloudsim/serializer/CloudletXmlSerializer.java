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
import org.cloudbus.cloudsim.hazelcast.HzCloudlet;

import java.beans.DefaultPersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CloudletXmlSerializer implements StreamSerializer<HzCloudlet> {

    @Override
    public int getTypeId() {
        return 11;
    }

    @Override
    public void write(ObjectDataOutput out, HzCloudlet object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(bos);
        String[] propertyNames = new String[] { "cloudletId", "cloudletLength", "numberOfPes", "cloudletFileSize",
                "cloudletOutputSize", "utilizationModelCpu", "utilizationModelRam", "utilizationModelBw", "record"};
        encoder.setPersistenceDelegate(HzCloudlet.class, new DefaultPersistenceDelegate(propertyNames));

        encoder.writeObject(object);
        encoder.close();
        out.write(bos.toByteArray());
    }

    @Override
    public HzCloudlet read(ObjectDataInput in) throws IOException {

        final InputStream inputStream = (InputStream) in;
        XMLDecoder decoder = new XMLDecoder(inputStream);
        HzCloudlet cloudlet = (HzCloudlet) decoder.readObject();
        decoder.close();
        return cloudlet;
    }

    @Override
    public void destroy() {
    }
}
