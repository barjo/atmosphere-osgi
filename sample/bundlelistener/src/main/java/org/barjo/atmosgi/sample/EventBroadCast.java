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
package org.barjo.atmosgi.sample;

import org.apache.felix.ipojo.annotations.*;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.cpr.Broadcaster;
import org.atmosphere.handler.OnMessage;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.BroadcastOnPostAtmosphereInterceptor;
import org.barjo.atmosgi.AtmosphereService;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.*;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component(name = "AtmOSGi::Sample::EventBroadcast")
@Instantiate(name = "AtmOSGi::Sample::EventBroadcast-1")
public class EventBroadCast extends OnMessage<String> {

    @Property(name = "mapping", value = "/event")
    private String mapping;

    private final List<AtmosphereInterceptor> interceptors = new ArrayList<AtmosphereInterceptor>();

    @Requires
    private AtmosphereService atmoservice;

    @Requires
    private HttpService http;

    private Broadcaster broadcaster;

    private MyEventListener listener;

    private final BundleContext context;

    public EventBroadCast(BundleContext pContext) {
        context = pContext;
    }

    @Validate
    private void start() {
        listener = new MyEventListener();
        context.addBundleListener(listener);

        broadcaster = atmoservice.getBroadcasterFactory().get();

        //Register the server (itself)
        interceptors.add(new AtmosphereResourceLifecycleInterceptor());
        atmoservice.addAtmosphereHandler(mapping, this,broadcaster, interceptors);

        //Register the web client
        try {
            http.registerResources("/event","/web",null);
        } catch (NamespaceException e) {
            e.printStackTrace();
        }
    }

    @Invalidate
    private void stop() {
        context.removeBundleListener(listener);
        atmoservice.removeAtmosphereHandler(mapping);
        interceptors.clear();

        http.unregister("/event");
    }



    @Override
    public void onMessage(AtmosphereResponse atmosphereResponse, String s) throws IOException {
        atmosphereResponse.getWriter().write(s);
    }

    private class MyEventListener implements BundleListener {

        public void bundleChanged(BundleEvent bundleEvent) {
            JSONObject json = new JSONObject();
            try {
                json.put("type",bundleEvent.getType());
                json.put("id",bundleEvent.getBundle().getBundleId());
                json.put("name",bundleEvent.getBundle().getSymbolicName());
                json.put("time", new Date().getTime());
                broadcaster.broadcast(json.toString());
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
    }
}
