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

public class HealthConstants {
    private static AtomicBoolean toScale = new AtomicBoolean();

    public static AtomicBoolean getToScale() {
        return toScale;
    }

    public static void setToScale(AtomicBoolean toScale) {
        HealthConstants.toScale = toScale;
    }
}
