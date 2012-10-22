/*
 Copyright 2012 Jonathan M. Bardin
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package org.barjo.atmosgi;

import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.cpr.BroadcasterFactory;

import java.util.List;

/**
 * OSGi Service which represents an AtmosphereFramework.
 * Thanks to this service you can add AtmosphereHandler and get the BroadcasterFactory at runtime.
 * User: barjo
 * Date: 18/10/12
 * Time: 11:08
 */
public interface AtmosphereService {
    static String MAPPING = "org.atmosphere.mapping";
    static String INTERCEPTORS = "org.atmosphere.interceptors";
    static String BROADCASTER = "org.atmosphere.broadcaster";

    /**
     * @return The AtmosphereFramework default BroadcasterFactory.
     */
    BroadcasterFactory getBroadcasterFactory();

    /**
     * Add a new AtmosphereHandler to the framework with the given mapping.
     * Be aware that the mapping is added to the atmosphere root, if the root
     * is /atmosphere, then the handler will be accessible through the
     * /atmosphere/<mapping> address.
     * @param mapping The AtmosphereHandler mapping. (e.g /toto)
     * @param handler The AtmosphereHandler to be added.
     * @throws IllegalArgumentException if an handler is already linked to this mapping.
     */
    void addAtmosphereHandler(String mapping, AtmosphereHandler handler);

    void addAtmosphereHandler(String mapping, AtmosphereHandler handler, List<AtmosphereInterceptor> interceptors);

    void addAtmosphereHandler(String mapping, AtmosphereHandler h, Broadcaster broadcaster, List<AtmosphereInterceptor> l);

    /**
     * Remove the existing AtmosphereHandler linked to the given mapping.
     * @param mapping The existing AtmosphereHandler mapping.
     */
    void removeAtmosphereHandler(String mapping);
}
