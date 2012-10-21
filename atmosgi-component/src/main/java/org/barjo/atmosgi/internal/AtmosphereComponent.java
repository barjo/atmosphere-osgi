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

import org.apache.felix.ipojo.annotations.*;
import org.atmosphere.cpr.*;
import org.atmosphere.di.ServletContextProvider;
import org.barjo.atmosgi.AtmosphereService;
import org.osgi.framework.BundleContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;

@Component(name = "AtmOSGi::AtmosphereService")
@Instantiate(name = "AtmOSGi::AtmosphereService-1")
@Provides(specifications = AtmosphereService.class)
public class AtmosphereComponent extends HttpServlet implements ServletContextProvider, AtmosphereService {

    @ServiceProperty(name = "org.atmosphere.root", value = "/atmosphere")
    private String mapping;

    private final AtmosphereFramework framework;
    private final ServiceTracker tracker;

    public AtmosphereComponent(BundleContext context) {
        framework = new AtmosphereFramework(false, false);
        tracker = new ServiceTracker(context, AtmosphereHandler.class.getName(), new AtmosphereHandlerTracker(this, context));
    }

    @Requires
    private HttpService http;

    @Requires(optional = true)
    private LogService logger;

    @Validate
    private void start() {
        Hashtable<String, Object> properties = new Hashtable<String, Object>();
        properties.put(ApplicationConfig.BROADCASTER_CACHE, "org.atmosphere.cache.HeaderBroadcasterCache");
        properties.put("org.atmosphere.cpr.AtmosphereInterceptor", "org.atmosphere.client.TrackMessageSizeInterceptor");


        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(AtmosphereFramework.class.getClassLoader());
        try {
            logger.log(LogService.LOG_INFO, "Atmosphere starting...");
            http.registerServlet(mapping, this, properties, null);
            tracker.open();
        } catch (Exception e) {
            logger.log(LogService.LOG_ERROR, "Cannot create the Atmosphere Framework.", e);
            throw new RuntimeException(e);
        } finally {
            Thread.currentThread().setContextClassLoader(loader);
        }
    }


    @Invalidate
    private void stop() {
        logger.log(LogService.LOG_INFO, "Atmosphere stopping...");
        http.unregister(mapping);
    }


    // ----------------------------
    //   AtmosphereService impl.
    // ----------------------------

    public void removeAtmosphereHandler(String mapping) {
        framework.removeAtmosphereHandler(mapping);
    }

    public void removeAllInitParams() {
        framework.removeAllInitParams();
    }

    public void addAtmosphereHandler(String hMapping, AtmosphereHandler handler) {
		hMapping = hMapping.startsWith("/") ? hMapping : "/" + hMapping; 
        framework.addAtmosphereHandler(mapping + hMapping, handler);
    }

    public void addAtmosphereHandler(String hMapping, AtmosphereHandler h, Broadcaster broadcaster, List<AtmosphereInterceptor> l) {
        framework.addAtmosphereHandler(mapping + hMapping, h, broadcaster, l);
    }

    public void addAtmosphereHandler(String hMapping, AtmosphereHandler h, List<AtmosphereInterceptor> l) {
        framework.addAtmosphereHandler(mapping + hMapping, h, l);
    }

    public BroadcasterFactory getBroadcasterFactory() {
        return framework.getBroadcasterFactory();
    }

    // -------------------
    //  HttpServlet
    // -------------------

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        framework.init(config);
    }

    @Override
    public void destroy() {
        tracker.close();
        framework.destroy();
        super.destroy();
    }

    @Override
    public void doHead(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        doPost(req, res);
    }


    @Override
    public void doOptions(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        doPost(req, res);
    }


    @Override
    public void doTrace(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        doPost(req, res);
    }


    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        doPost(req, res);
    }


    @Override
    public void doPut(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        doPost(req, res);
    }


    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        doPost(req, res);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException, ServletException {
        framework.doCometSupport(AtmosphereRequest.wrap(req), AtmosphereResponse.wrap(res));
    }
}
