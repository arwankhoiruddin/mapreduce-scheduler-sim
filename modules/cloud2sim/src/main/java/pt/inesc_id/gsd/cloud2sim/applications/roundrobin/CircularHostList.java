/**
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
package pt.inesc_id.gsd.cloud2sim.applications.roundrobin;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Host;

public final class CircularHostList implements Iterable<Host> {

    private final List<Host> hostList = new LinkedList<Host>();

    private int ini;

    public CircularHostList(List<? extends Host> hosts) {
        this.hostList.addAll(hosts);
    }

    public boolean add(Host host) {
        return this.hostList.add(host);
    }

    public boolean remove(Host host2Remove) {
        return this.hostList.remove(host2Remove);
    }

    public Host next() {
        Host host = null;

        if (!hostList.isEmpty()) {
            int index = (this.ini++ % this.hostList.size());
            host = this.hostList.get(index);
        }

        return host;
    }

    @Override
    public Iterator<Host> iterator() {
        return get().iterator();
    }

    public List<Host> get() {
        return Collections.unmodifiableList(this.hostList);
    }

    public int size() {
        return this.hostList.size();
    }
}