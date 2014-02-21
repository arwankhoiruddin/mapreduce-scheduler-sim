/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.examples.hazelcast.instances;

import java.util.Map;

public final class ObjectCollectionTest {
    public static void main(String[] args) {
        ObjectCollection.init();

        Map<Integer, String> userList;
        Map<Integer, String> tempList = ObjectCollection.getTempList();

        for (int i = 0; i < 10; i++) {
            tempList.put(i, "value" + i);
        }

        int size = ObjectCollection.getList().size();
        System.out.println("zero " + size);

        size = ObjectCollection.getTempList().size();
        System.out.println("...............ten " + size);

        ObjectCollection.getList().putAll(tempList);
        size = ObjectCollection.getList().size();
        System.out.println("ten " + size);
        ObjectCollection.getList().putAll(ObjectCollection.getTempList());
        size = ObjectCollection.getList().size();
        System.out.println("Ten. NOT twenty " + size);

        for (int i = 0; i < 10; i++) {
            ObjectCollection.getTempList().put(i + 10, "value" + i);
        }
        ObjectCollection.getList().putAll(ObjectCollection.getTempList());
        size = ObjectCollection.getList().size();
        System.out.println("Twenty " + size);

        ObjectCollection.getList().remove(1);
        size = ObjectCollection.getList().size();
        System.out.println("nineteen..............." + size);

        ObjectCollection.getList().remove(1);
        size = ObjectCollection.getList().size();
        System.out.println("nineteen..............." + size);

        ObjectCollection.getList().put(12, "kitten");
        size = ObjectCollection.getList().size();
        System.out.println("nineteen..............." + size);

        userList = ObjectCollection.getList();
        for (int i : ObjectCollection.getList().keySet()) {
            System.out.println(i + ": " + userList.get(i));
        }
    }
}

