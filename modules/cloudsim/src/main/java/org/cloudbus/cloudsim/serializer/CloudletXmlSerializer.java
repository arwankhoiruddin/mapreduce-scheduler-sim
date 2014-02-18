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
import org.cloudbus.cloudsim.Cloudlet;

import java.beans.DefaultPersistenceDelegate;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

public class CloudletXmlSerializer implements StreamSerializer<Cloudlet> {

    @Override
    public int getTypeId() {
        return 11;
    }

    @Override
    public void write(ObjectDataOutput out, Cloudlet object) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        XMLEncoder encoder = new XMLEncoder(bos);
        String[] propertyNames = new String[] { "cloudletId", "cloudletLength", "numberOfPes", "cloudletFileSize",
                "cloudletOutputSize", "utilizationModelCpu", "utilizationModelRam", "utilizationModelBw", "record"};
        encoder.setPersistenceDelegate(Cloudlet.class, new DefaultPersistenceDelegate(propertyNames));

        encoder.writeObject(object);
        encoder.close();
        out.write(bos.toByteArray());
    }

    @Override
    public Cloudlet read(ObjectDataInput in) throws IOException {

        final InputStream inputStream = (InputStream) in;
        XMLDecoder decoder = new XMLDecoder(inputStream);
        Cloudlet cloudlet = (Cloudlet) decoder.readObject();
        decoder.close();
        return cloudlet;
    }

    @Override
    public void destroy() {
    }
}
