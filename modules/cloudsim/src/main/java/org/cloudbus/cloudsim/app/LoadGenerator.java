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

public class LoadGenerator {

    public static boolean isPrime(Double n) {
        if (n % 2 == 0) return false;
        for (int i = 3; i * i <= n; i += 2) {
            if (n % i == 0)
                return false;
        }
        return true;
    }

    public static Double iffPrime(Double n) {
        int val = 0;
        for (int i = 0; i < n; i++) {
            val += Math.atan(i);
            for (int j = 0; j < n; j++) {
                val += (int) (Math.sqrt(i) * Math.exp(j) * Math.atan2(Math.random() * Math.log(Math.log(val)),
                        Math.acos(Math.sqrt(Math.atan(Math.log(n))))));
                for (int k = 0; k < n; k++) {
                    val += Math.sin(i * j * k) + Math.sqrt(k * j);
                }
            }
        }
        if (isPrime(n)) {
            return Math.sqrt(val) * Math.exp(n) * Math.atan2(Math.random() * Math.log(Math.log(n)),
                    Math.acos(Math.sqrt(Math.atan(Math.log(n)))));
        } else {
            return Math.exp(val) * Math.atan2(Math.random() * Math.log(Math.log(n)),
                    Math.acos(Math.sqrt(Math.atan(Math.log(n)))));
        }
    }

    public static int ifPrime(Double n) {
        if (iffPrime(n).isNaN()) {
            n = Math.random() * n;
        }
        return (int) (Math.log(n.hashCode()) * Math.log(n.hashCode() * Math.log(n.hashCode())));
    }
}
