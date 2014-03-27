/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.hazelcast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IMap;
import com.hazelcast.core.Member;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.core.CloudSimTags;
import org.cloudbus.cloudsim.core.SimEvent;
import pt.inesc_id.gsd.cloud2sim.concurrent.runnables.SubmittedCloudletsRemover;
import pt.inesc_id.gsd.cloud2sim.concurrent.callables.CloudletListSubmitter;
import pt.inesc_id.gsd.cloud2sim.concurrent.callables.VmListSubmitter;

/**
 * DatacenterBroker represents a broker acting on behalf of a user. It hides VM management, as vm
 * creation, submission of cloudlets to this VMs and destruction of VMs.
 *
 */
public class HzDatacenterBroker extends DatacenterBroker {

    protected HzObjectCollection hzObjectCollection = HzObjectCollection.getHzObjectCollection();

    /**
     * The cloudlets submitted.
     */
    protected int cloudletsSubmitted;

    private IExecutorService vmExecutor;
    private IExecutorService cloudletExecutor;
    private IExecutorService cloudletRemoverExecutor;

    protected static List<Integer> submittedCloudletIds = new ArrayList<>();

    /**
     * Created a new DatacenterBroker object.
     *
     * @param name name to be associated with this entity (as required by Sim_entity class from
     *             simjava package)
     * @throws Exception the exception
     * @pre name != null
     * @post $none
     */
    public HzDatacenterBroker(String name) throws Exception {
        super(name);

        vmExecutor = hzObjectCollection.getFirstInstance().getExecutorService("vmExecutor");
        cloudletExecutor = hzObjectCollection.getFirstInstance().getExecutorService("cloudletExecutor");
        cloudletRemoverExecutor = hzObjectCollection.getFirstInstance().getExecutorService("cloudletRemoverExecutor");

        cloudletsSubmitted = 0;
        setVmsRequested(0);
        setVmsAcks(0);
        setVmsDestroyed(0);

        setDatacenterIdsList(new LinkedList<Integer>());
        setDatacenterRequestedIdsList(new ArrayList<Integer>());
        setVmsToDatacentersMap(new HashMap<Integer, Integer>());
        setDatacenterCharacteristicsList(new HashMap<Integer, DatacenterCharacteristics>());
    }

    public static List<Integer> getSubmittedCloudletIds() {
        return submittedCloudletIds;
    }

    /**
     * This method is used to send to the broker the list with virtual machines that must be
     * created.
     *
     * @param list the list
     * @pre list !=null
     * @post $none
     */
    public void submitVmList(Map<Integer, HzVm> list) {
        hzObjectCollection.getVmList().putAll(list);
    }

    public void submitCloudletsAndVms() throws InterruptedException, ExecutionException {
        Map <Member, Future< Integer >> vmResult =
                vmExecutor.submitToAllMembers(new VmListSubmitter());
        Map <Member, Future< Integer >> cloudletResult =
                cloudletExecutor.submitToAllMembers(new CloudletListSubmitter());
        int vmSize = 0;
        for (Future < Integer > future : vmResult.values ()) {
            vmSize += future.get();
        }
        int cloudletSize = 0;
        for (Future < Integer > future : cloudletResult.values ()) {
            cloudletSize += future.get();
        }
    }

    /**
     * This method is used to send to the broker the list of cloudlets.
     *
     * @param list the list
     * @pre list !=null
     * @post $none
     */
    public void submitCloudletList(Map<Integer, HzCloudlet> list) {
        hzObjectCollection.getCloudletList().putAll(list);
    }

    /**
     * Specifies that a given cloudlet must run in a specific virtual machine.
     *
     * @param cloudletId ID of the cloudlet being bount to a vm
     * @param vmId       the vm id
     * @pre cloudletId > 0
     * @pre id > 0
     * @post $none
     */
    public void bindCloudletToVm(int cloudletId, int vmId) {
        hzObjectCollection.getCloudletList().get(cloudletId).setVmId(vmId);
    }

    /**
     * Process the ack received due to a request for VM creation.
     *
     * @param ev a SimEvent object
     * @pre ev != null
     * @post $none
     */
    protected void processVmCreate(SimEvent ev) {
        int[] data = (int[]) ev.getData();
        int datacenterId = data[0];
        int vmId = data[1];
        int result = data[2];

        if (result == CloudSimTags.TRUE) {
            getVmsToDatacentersMap().put(vmId, datacenterId);
            hzObjectCollection.getVmsCreatedList().put(vmId, hzObjectCollection.getVmList().get(vmId));

            Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": VM #", vmId,
                    " has been created in Datacenter #", datacenterId, ", Host #",
                    hzObjectCollection.getVmsCreatedList().get(vmId).getHostId());
        } else {
            Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Creation of VM #", vmId,
                    " failed in Datacenter #", datacenterId);
        }

        incrementVmsAcks();

        // all the requested VMs have been created
        if (hzObjectCollection.getVmsCreatedList().size() == hzObjectCollection.getVmList().size() - getVmsDestroyed()) {
            submitCloudlets();
        } else {
            // all the acks received, but some VMs were not created
            if (getVmsRequested() == getVmsAcks()) {
                // find id of the next datacenter that has not been tried
                for (int nextDatacenterId : getDatacenterIdsList()) {
                    if (!getDatacenterRequestedIdsList().contains(nextDatacenterId)) {
                        createVmsInDatacenter(nextDatacenterId);
                        return;
                    }
                }

                // all datacenters already queried
                if (hzObjectCollection.getVmsCreatedList().size() > 0) { // if some vm were created
                    submitCloudlets();
                } else { // no vms created. abort
                    Log.printLine(CloudSim.clock() + ": " + getName()
                            + ": none of the required VMs could be created. Aborting");
                    finishExecution();
                }
            }
        }
    }

    /**
     * Process a cloudlet return event.
     *
     * @param ev a SimEvent object
     * @pre ev != $null
     * @post $none
     */
    protected void processCloudletReturn(SimEvent ev) {
        HzCloudlet cloudlet = (HzCloudlet) ev.getData();
        hzObjectCollection.getCloudletReceivedList().put(cloudlet.getCloudletId(), cloudlet);
        Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Cloudlet ", cloudlet.getCloudletId(),
                " received");
        cloudletsSubmitted--;
        if (hzObjectCollection.getCloudletList().size() == 0 && cloudletsSubmitted == 0) { // all cloudlets executed
            Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": All Cloudlets executed. Finishing...");
            clearDatacenters();
            finishExecution();
        } else { // some cloudlets haven't finished yet
            if (hzObjectCollection.getCloudletList().size() > 0 && cloudletsSubmitted == 0) {
                // all the cloudlets sent finished. It means that some bount
                // cloudlet is waiting its VM be created
                clearDatacenters();
                createVmsInDatacenter(0);
            }

        }
    }

    /**
     * Create the virtual machines in a datacenter.
     *
     * @param datacenterId Id of the chosen PowerDatacenter
     * @pre $none
     * @post $none
     */
    protected void createVmsInDatacenter(int datacenterId) {
        // send as much vms as possible for this datacenter before trying the next one
        int requestedVms = 0;
        String datacenterName = CloudSim.getEntityName(datacenterId);
        for (IMap.Entry<Integer, HzVm> entry : hzObjectCollection.getVmList().entrySet()) {
            if (!getVmsToDatacentersMap().containsKey(entry.getKey())) {
                Log.printLine(CloudSim.clock() + ": " + getName() + ": Trying to Create VM #" + entry.getKey() +
                        " in " + datacenterName);
                sendNow(datacenterId, CloudSimTags.VM_CREATE_ACK, entry.getValue());
                requestedVms++;
            }
        }

        getDatacenterRequestedIdsList().add(datacenterId);

        setVmsRequested(requestedVms);
        setVmsAcks(0);
    }

    /**
     * Submit cloudlets to the created VMs.
     *
     * @pre $none
     * @post $none
     */
    protected void submitCloudlets() {
        int vmIndex = 0;
        for (HzCloudlet cloudlet : hzObjectCollection.getCloudletList().values()) {
            Vm vm;
            // if user didn't bind this cloudlet and it has not been executed yet
            if (cloudlet.getVmId() == -1) {
                vm = hzObjectCollection.getVmsCreatedList().get(vmIndex);
            } else { // submit to the specific vm
                Log.printConcatLine(cloudlet.getVmId());
                vm = hzObjectCollection.getVmsCreatedList().get(cloudlet.getVmId());
                if (vm == null) { // vm was not created
                    if (!Log.isDisabled()) {
                        Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Postponing execution of cloudlet ",
                                cloudlet.getCloudletId(), ": bount VM not available");
                    }
                    continue;
                }
            }

            if (!Log.isDisabled()) {
                Log.printConcatLine(CloudSim.clock(), ": ", getName(), ": Sending cloudlet ",
                        cloudlet.getCloudletId(), " to VM #", vm.getId());
            }

            cloudlet.setVmId(vm.getId());
            sendNow(getVmsToDatacentersMap().get(vm.getId()), CloudSimTags.CLOUDLET_SUBMIT, cloudlet);
            cloudletsSubmitted++;
            vmIndex = (vmIndex + 1) % hzObjectCollection.getVmsCreatedList().size();
            hzObjectCollection.getCloudletSubmittedList().put(cloudlet.getCloudletId(), cloudlet);

            submittedCloudletIds.add(cloudlet.getCloudletId());
        }

        for (int cloudletId : getSubmittedCloudletIds()) {
            cloudletRemoverExecutor.executeOnKeyOwner(new SubmittedCloudletsRemover(cloudletId), cloudletId);
        }
    }

    /**
     * Destroy the virtual machines running in datacenters.
     *
     * @pre $none
     * @post $none
     */
    protected void clearDatacenters() {
        for (IMap.Entry<Integer, HzVm> entry : hzObjectCollection.getVmsCreatedList().entrySet()) {
            Log.printConcatLine(CloudSim.clock(), ": " + getName(), ": Destroying VM #", entry.getKey());
            sendNow(getVmsToDatacentersMap().get(entry.getKey()), CloudSimTags.VM_DESTROY, entry.getValue());
        }
        hzObjectCollection.getVmsCreatedList().clear();
    }
}
