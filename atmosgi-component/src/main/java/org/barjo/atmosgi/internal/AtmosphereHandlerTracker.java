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
package org.barjo.atmosgi.internal;

import org.atmosphere.cpr.AtmosphereHandler;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.Broadcaster;
import org.barjo.atmosgi.AtmosphereService;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

import java.util.List;

/**
 * User: barjo
 * Date: 18/10/12
 * Time: 14:50
 */
public class AtmosphereHandlerTracker implements ServiceTrackerCustomizer {

    private final AtmosphereService atmo;
    private final BundleContext context;

    public AtmosphereHandlerTracker(AtmosphereService pAtmo, BundleContext pContext){
        atmo = pAtmo;
        context = pContext;
    }

    public Object addingService(ServiceReference serviceReference) {
        String mapping = (String) serviceReference.getProperty(AtmosphereService.MAPPING);
        List<AtmosphereInterceptor> interceptors = (List<AtmosphereInterceptor>) serviceReference.getProperty(AtmosphereService.INTERCEPTORS);
        Broadcaster broadcaster = (Broadcaster) serviceReference.getProperty(AtmosphereService.BROADCASTER);
        AtmosphereHandler handler =  (AtmosphereHandler) context.getService(serviceReference);


        if (interceptors != null && broadcaster != null){
            atmo.addAtmosphereHandler(mapping,handler,broadcaster,interceptors);
        } else if (broadcaster == null){
            atmo.addAtmosphereHandler(mapping,handler,interceptors);
        } else {
            atmo.addAtmosphereHandler(mapping,handler);
        }

        return mapping;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void modifiedService(ServiceReference serviceReference, Object o) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void removedService(ServiceReference serviceReference, Object o) {
        atmo.removeAtmosphereHandler((String) o);
        context.ungetService(serviceReference);
    }
}
