/**
 *  Simulations that run in multiple nodes of the same hazelcast cluster, where the nodes join at later points of the
 *  simulation based on the load. To be able to keep the nodes connected, a secondary hazelcast cluster is introduced,
 *  where all the nodes have a hazelcast instance running on. Based on the load they learn from the HealthMonitor's
 *  pings they receive from AdaptiveScalerProbe, they will spawn instances in the main cluster to join the simulation.
 *  This follows a cycle-sharing model.
 */

package pt.inesc_id.gsd.cloud2sim.applications.main.dynamics;