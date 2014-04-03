/*
 * Title:        Cloud2Sim
 * Description:  Distributed and Concurrent Cloud Simulation
 *               Toolkit for Modeling and Simulation
 *               of Clouds - Enhanced version of CloudSim.
 * Licence:      GPL - http://www.gnu.org/copyleft/gpl.html
 *
 * Copyright (c) 2014, Pradeeban Kathiravelu <pradeeban.kathiravelu@tecnico.ulisboa.pt>
 */

package pt.inesc_id.gsd.cloud2sim.scale.health;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Creates the atomic variables toScale{Out|In} with the initial value, false.
 * This will later be set by HealthMonitor thread and read by HealthAnnouncer of AdaptiveScalerProbe.
 */
public class HealthParams {
    private static AtomicBoolean toScaleOut = new AtomicBoolean();
    private static AtomicBoolean toScaleIn = new AtomicBoolean();

    public static boolean getToScaleOut() {
        return toScaleOut.get();
    }

    public static void setToScaleOut(boolean toScaleOut) {
        HealthParams.toScaleOut.set(toScaleOut);
    }

    public static boolean getToScaleIn() {
        return toScaleIn.get();
    }

    public static void setToScaleIn(boolean toScaleIn) {
        HealthParams.toScaleIn.set(toScaleIn);
    }
}
