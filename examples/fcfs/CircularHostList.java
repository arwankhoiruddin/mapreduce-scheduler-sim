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
package org.cloudbus.cloudsim.examples.fcfs;

import org.cloudbus.cloudsim.Host;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public final class CircularHostList implements Iterable<Host> {

    private final List<Host> host_list = new LinkedList<Host>();

    private int ini;

    public CircularHostList(List<? extends Host> hosts) {
        this.host_list.addAll(hosts);
    }

    public boolean add(Host host) {
        return this.host_list.add(host);
    }

    public boolean remove(Host host2Remove) {
        return this.host_list.remove(host2Remove);
    }

    public Host next() {
        Host host = null;

        if (!host_list.isEmpty()) {
            int index = (this.ini++ % this.host_list.size());
            host = this.host_list.get(index);
        }

        return host;
    }

    @Override
    public Iterator<Host> iterator() {
        return get().iterator();
    }

    public List<Host> get() {
        return Collections.unmodifiableList(this.host_list);
    }

    public int size() {
        return this.host_list.size();
    }
}