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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component(name = "AtmOSGi::Sample::Chat")
@Instantiate(name = "AtmOSGi::Sample::Chat-1")
public class ChatHandler extends OnMessage<String> {

    @ServiceProperty(name = "mapping", value = "/chat")
    private String mapping;

    private final List<AtmosphereInterceptor> interceptors = new ArrayList<AtmosphereInterceptor>();

    @Requires
    private AtmosphereService atmoservice;

    @Validate
    private void start() {
        interceptors.add(new BroadcastOnPostAtmosphereInterceptor());
        interceptors.add(new AtmosphereResourceLifecycleInterceptor());
        atmoservice.addAtmosphereHandler(mapping, this, interceptors);
    }

    @Invalidate
    private void stop() {
        atmoservice.removeAtmosphereHandler(mapping);
        interceptors.clear();
    }

    @Override
    public void onMessage(AtmosphereResponse atmosphereResponse, String s) throws IOException {
        atmosphereResponse.getWriter().write(s);
    }
}
