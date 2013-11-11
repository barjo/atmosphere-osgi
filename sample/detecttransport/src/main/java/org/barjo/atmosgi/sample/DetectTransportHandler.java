package org.barjo.atmosgi.sample;

import org.apache.felix.ipojo.annotations.*;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.handler.OnMessage;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.BroadcastOnPostAtmosphereInterceptor;
import org.barjo.atmosgi.AtmosphereService;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Sample transport detect handler from the Atmosphere Wiki
 * https://github.com/Atmosphere/atmosphere/wiki/How-to-discover-supported-transport-between-browsers-and-server
 */
@Component(name = "AtmOSGi::Sample::DetectTransport")
@Instantiate(name = "AtmOSGi::Sample::DetectTransport-1")
public class DetectTransportHandler extends OnMessage<String> {

    @Property(name = "mapping", value = "/detect")
    private String mapping;

    private final List<AtmosphereInterceptor> interceptors = new ArrayList<AtmosphereInterceptor>();

    @Requires
    private AtmosphereService atmoservice;

    @Requires
    private HttpService http;

    @Validate
    private void start() {
        //Register the server (itself)
        interceptors.add(new BroadcastOnPostAtmosphereInterceptor());
        interceptors.add(new AtmosphereResourceLifecycleInterceptor());
        atmoservice.addAtmosphereHandler(mapping, this, interceptors);

        //Register the web client
        try {
            http.registerResources("/detect","/web",null);
        } catch (NamespaceException e) {
            e.printStackTrace();
        }
    }

    @Invalidate
    private void stop() {
        atmoservice.removeAtmosphereHandler(mapping);
        interceptors.clear();

        http.unregister("/detect");
    }

    @Override
    public void onMessage(AtmosphereResponse atmosphereResponse, String s) throws IOException {
        atmosphereResponse.getWriter().write("OK");
    }

}
