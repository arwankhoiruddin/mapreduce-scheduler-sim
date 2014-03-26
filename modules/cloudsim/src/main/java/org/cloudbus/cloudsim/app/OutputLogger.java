/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package org.cloudbus.cloudsim.app;

import com.hazelcast.core.IMap;
import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.core.constants.Cloud2SimConstants;

import java.text.DecimalFormat;
import java.util.Map;

public class OutputLogger {

    /**
	 * Prints the Cloudlet objects
	 * @param list  list of Cloudlets
	 */
	public static void printCloudletList(Map<Integer, ? extends Cloudlet> list) {
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("# ========== OUTPUT ==========");
		Log.printLine("# Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent +
                "Start Time" + indent + "Finish Time"
                + indent + "Submission Time" + indent + "Processing Cost");

		DecimalFormat dft = new DecimalFormat("###.##");
		for (IMap.Entry<Integer, ? extends Cloudlet> entry: list.entrySet()) {
			cloudlet = entry.getValue();
            int cloudletId = entry.getKey();

			Log.print(indent + cloudletId + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloud2SimConstants.SUCCESS){
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + dft.format(cloudlet.getActualCPUTime()) +
						indent + indent + dft.format(cloudlet.getExecStartTime())+ indent + indent + indent + dft.format(cloudlet.getFinishTime())
                                             + indent + indent + dft.format(cloudlet.getSubmissionTime())+ indent + indent + dft.format(cloudlet.getCostPerSec()));
            }
		}

	}
}
