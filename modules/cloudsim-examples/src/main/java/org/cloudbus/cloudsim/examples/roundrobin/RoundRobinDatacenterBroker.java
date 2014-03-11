/**
 *  Copyright 2013 team XPD.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.cloudbus.cloudsim.examples.roundrobin;

import java.util.List;

import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import org.cloudbus.cloudsim.core.hazelcast.HzObjectCollection;

/**
 * This broker allocates VMs to data centers following the <a href="http://en.wikipedia.org/wiki/Round-robin_scheduling">Round-robin</a>
 * algorithm.
 *
 * @author alessandro
 */
public class RoundRobinDatacenterBroker extends DatacenterBroker {

    /**
     * Creates an instance of this class associating to it a given name.
     *
     * @param name The name to be associated to this broker. It might not be <code>null</code> or
     *             empty.
     * @throws Exception If the name contains spaces.
     */
    public RoundRobinDatacenterBroker(String name) throws Exception {
        super(name);
    }

    @Override
    protected void processResourceCharacteristics(SimEvent ev) {
        DatacenterCharacteristics characteristics = (DatacenterCharacteristics) ev.getData();
        getDatacenterCharacteristicsList().put(characteristics.getId(), characteristics);

        if (getDatacenterCharacteristicsList().size() == getDatacenterIdsList().size()) {
            distributeRequestsForNewVmsAcrossDatacentersUsingTheRoundRobinApproach();
        }
    }

    /**
     * Distributes the VMs across the data centers using the round-robin approach. A VM is allocated
     * to a data center only if there isn't a VM in the data center with the same id.
     */
    protected void distributeRequestsForNewVmsAcrossDatacentersUsingTheRoundRobinApproach() {
        int numberOfVmsAllocated = 0;
        int i = 0;
        final List<Integer> availableDatacenters = getDatacenterIdsList();
        for (Vm vm : HzObjectCollection.getVmList().values()) {
            int datacenterId = availableDatacenters.get(i++ % availableDatacenters.size());
            String datacenterName = CloudSim.getEntityName(datacenterId);
            if (!getVmsToDatacentersMap().containsKey(vm.getId())) {
                Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + vm
                    .getId() + " in " + datacenterName);
                sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, vm);
                numberOfVmsAllocated++;
            }
        }
        setVmsRequested(numberOfVmsAllocated);
        setVmsAcks(0);
    }
}