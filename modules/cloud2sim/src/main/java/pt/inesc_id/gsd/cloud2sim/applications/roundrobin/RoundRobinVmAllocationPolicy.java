package pt.inesc_id.gsd.cloud2sim.applications.roundrobin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;

public class RoundRobinVmAllocationPolicy extends org.cloudbus.cloudsim.VmAllocationPolicy {
    private final Map<String, Host> vmTable = new HashMap<String, Host>();
    private final CircularHostList hosts;

    public RoundRobinVmAllocationPolicy(List<? extends Host> list) {
        super(list);
        this.hosts = new CircularHostList(list);
    }

    @Override
    public boolean allocateHostForVm(Vm vm) {
        if (this.vmTable.containsKey(vm.getUid())) {
            return true;
        }

        boolean vmAllocated = false;

        Host host = this.hosts.next();
        if (host != null) {
            vmAllocated = this.allocateHostForVm(vm, host);
        }

        return vmAllocated;
    }

    @Override
    public boolean allocateHostForVm(Vm vm, Host host) {
        if (host != null && host.vmCreate(vm)) {
            vmTable.put(vm.getUid(), host);
            Log.formatLine("%.4f: VM #" + vm.getId() + " has been allocated to the host#" + host.getId() +
                           " datacenter #" + host.getDatacenter().getId() + "(" + host.getDatacenter().getName() + ") #",
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
        Host host = this.vmTable.remove(vm.getUid());

        if (host != null) {
            host.vmDestroy(vm);
        }
    }

    @Override
    public Host getHost(Vm vm) {
        return this.vmTable.get(vm.getUid());
    }

    @Override
    public Host getHost(int vmId, int userId) {
        return this.vmTable.get(Vm.getUid(userId, vmId));
    }
}