atmosphere-osgi
===============

A simple OSGi service which encapsulate an [atmosphere](https://github.com/Atmosphere/atmosphere "atmosphere") framework.

Usage
-----

Install the amtmosgi-component bundle which contains the implementation.
Then you can use the [AtmosphereService]("https://github.com/barjo/atmosphere-osgi/blob/master/atmosgi-service/src/main/java/org/barjo/atmosgi/AtmosphereService.java") or directly publish an [AtmosphereHandler]("https://github.com/Atmosphere/atmosphere/wiki/Understanding-AtmosphereHandler") in the OSGi service broker (following the white-board pattern).
