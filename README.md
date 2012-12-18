atmosphere-osgi
===============

A simple OSGi service which encapsulate an [atmosphere](https://github.com/Atmosphere/atmosphere "atmosphere") framework.

Info
-------
The current version supports properly the sse and long-polling transport with any HttpService. I plan to add the websocket support working with the grizzly HttpService (version > 2.9) and the apache Felix jetty HttpService (version > 2.2).

Usage
-----

Install the amtmosgi-component bundle which contains the implementation.
Then you can use the [AtmosphereService](https://github.com/barjo/atmosphere-osgi/blob/master/atmosgi-service/src/main/java/org/barjo/atmosgi/AtmosphereService.java) or directly publish an [AtmosphereHandler](https://github.com/Atmosphere/atmosphere/wiki/Understanding-AtmosphereHandler) in the OSGi service broker (following the white-board pattern).

Infrastructure
--------------

### Repository 
  	
```xml
<repository>
	<id>maven-barjo-repository-snapshot</id>
	<name>Barjo - Snapshot</name>
	<url>https://repository-barjo.forge.cloudbees.com/snapshot/</url>
	<layout>default</layout>
</repository>
```
