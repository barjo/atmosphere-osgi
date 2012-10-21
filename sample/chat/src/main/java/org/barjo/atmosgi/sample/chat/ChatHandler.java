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
package org.barjo.atmosgi.sample.chat;

import org.apache.felix.ipojo.annotations.*;
import org.atmosphere.cpr.AtmosphereInterceptor;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.handler.OnMessage;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.BroadcastOnPostAtmosphereInterceptor;
import org.barjo.atmosgi.AtmosphereService;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component(name = "AtmOSGi::Sample::Chat")
@Instantiate(name = "AtmOSGi::Sample::Chat-1")
public class ChatHandler extends OnMessage<String> {

    @ServiceProperty(name = "mapping", value = "/chat")
    private String mapping = "/chat";

    private final List<AtmosphereInterceptor> interceptors = new ArrayList<AtmosphereInterceptor>();

    @Requires
    private AtmosphereService atmoservice;

    @Requires
    private HttpService http;

    @Validate
    private void start() {
        interceptors.add(new BroadcastOnPostAtmosphereInterceptor());
        interceptors.add(new AtmosphereResourceLifecycleInterceptor());
        atmoservice.addAtmosphereHandler(mapping, this, interceptors);

        try {
            http.registerResources("/chat","/web",null);
        } catch (NamespaceException e) {
            e.printStackTrace();
        }
    }

    @Invalidate
    private void stop() {
        atmoservice.removeAtmosphereHandler(mapping);
        interceptors.clear();

        http.unregister("/chat");
    }

    @Override
    public void onMessage(AtmosphereResponse atmosphereResponse, String s) throws IOException {
        try {
            JSONObject json = new JSONObject(s);
            json.accumulate("time", new Date().getTime());
            atmosphereResponse.getWriter().write(json.toString());
        } catch (JSONException e) {
            throw new IOException(e);
        }
    }
}
