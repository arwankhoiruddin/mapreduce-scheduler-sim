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
package org.cloudbus.cloudsim.examples.other;

import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.examples.fcfs.Hosts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VmAllocationPolicyMinimum extends org.cloudbus.cloudsim.VmAllocationPolicy {

    private Map<String, Host> vm_table = new HashMap<String, Host>();
    private final Hosts hosts;
    private Datacenter datacenter;

    public VmAllocationPolicyMinimum(List<? extends Host> list) {
        super(list);
        hosts = new Hosts(list);
    }

    public void setDatacenter(Datacenter datacenter) {
        this.datacenter = datacenter;
    }

    public Datacenter getDatacenter() {
        return datacenter;
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {

        if (this.vm_table.containsKey(vm.getUid()))
            return true;

        boolean vm_allocated = false;
        int tries = 0;
        do {
            Host host = this.hosts.getWithMinimumNumberOfPesEquals(vm.getNumberOfPes());
            vm_allocated = this.allocateHostForVm(vm, host);
        } while (!vm_allocated && tries++ < hosts.size());

        return vm_allocated;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        if (host != null && host.vmCreate(vm)) {
            vm_table.put(vm.getUid(), host);
            Log.formatLine("%.4f: VM #" + vm.getId() + " has been allocated to the host#" + host
                .getId() +
                           " datacenter #" + host.getDatacenter().getId() + "(" + host
                .getDatacenter().getName() + ") #",
                           CloudSim.clock());
            return true;
        }
        return false;
    }

    @Override
    public List<Map<String, Object>> optimizeAllocation(List<? extends Vm> vmList) {
        return null;
    }

    @Override
    public void deallocateHostForVm(Vm vm) {
        Host host = this.vm_table.remove(vm.getUid());
        if (host != null) {
            host.vmDestroy(vm);
        }
    }

    @Override
    public Host getHost(Vm vm) {
        return this.vm_table.get(vm.getUid());
    }

    @Override
    public Host getHost(int vmId, int userId) {
        return this.vm_table.get(Vm.getUid(userId, vmId));
    }
}