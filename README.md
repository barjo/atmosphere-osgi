atmosphere-osgi
===============

A simple OSGi service which encapsulate an [atmosphere](https://github.com/Atmosphere/atmosphere "atmosphere") framework.
The current release version is 0.1.0.

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
  	
#### Release

```xml
<repository>
	<id>maven-barjo-repository-release</id>
	<name>Barjo - Release</name>
	<url>https://repository-barjo.forge.cloudbees.com/release/</url>
	<layout>default</layout>
</repository>
```

  	
#### Snapshot

```xml
<repository>
	<id>maven-barjo-repository-snapshot</id>
	<name>Barjo - Snapshot</name>
	<url>https://repository-barjo.forge.cloudbees.com/snapshot/</url>
	<layout>default</layout>
</repository>
```

ChangeLog
---------

### Version 0.1.0

```log
* a1a2206 - barjo : [maven-release-plugin] prepare release atmosgi-parent-0.1.0
*   77f9d80 - Jonathan Bardin : Merge pull request #4 from sergehuber/PUSH_TO_MASTER
|\  
| * 7543391 - Serge Huber : Revert custom changes to be able to generate pull request from new branch
| * 515e132 - Serge Huber : Attach javadoc and source systematically so we're sure we will deploy and install them.
| * a88f8ad - Serge Huber : Modify version number to distinguish forked version
| * adc3d14 - Serge Huber : Update project metadata to prepare for a release
| * 9470564 - Serge Huber : Upgrade iPOJO and bundle plugin
| * c393951 - Serge Huber : Upgrade to Atmosphere 2.0.3 Not tested yet, just made sure it compiles
|/  
*   bcd21a9 - Jonathan Bardin : Merge pull request #3 from torito/master
|\  
| * 9cfb397 - torito : avoid conflict with iPOJO 1.10.x and a bug with names in the @instantiate annotation
|/  
* 98025c7 - barjo : Upgrade to atmosphere 1.1.0-RC1. fix #1 fix by the last atmophere version
* 23e32df - Jonathan Bardin : fix typo.
* d6f2fc2 - barjo : no cache by default
* ea98a97 - barjo : update to last one.
*   1eae70b - barjo : Merge branch 'master' of github.com:barjo/atmosphere-osgi
|\  
| * 3216e4c - Jonathan Bardin : Update README.md
* | 70639b1 - barjo : Update to atmosphere 1.1.0.beta1
|/  
* 35de045 - barjo : remove version tag. use parents one.
*   290cad7 - barjo : Merge branch 'master' of github.com:barjo/atmosphere-osgi
|\  
* | 2c6b5ab - barjo : Embeded atmosphere runtime in atmosgi.
* | cdf0455 - barjo : Minor fix.
|/  
* 15e420f - barjo : Add some minor javadoc .. and little fix :D
* 438d7c9 - barjo : Add bundlelistener sample!
* f040814 - barjo : Change ServiceProperty to component property
* cdca018 - barjo : Add client code, from atmosphere.
* 7d4f92a - barjo : Add simple chat sample.
* 78c8e0a - barjo : Add chat sample.
* 38a977f - barjo : Change component name.
* e60b60b - barjo : Pom refactoring
* 2950141 - barjo : Initial project!
* 7cae8b5 - barjo : Add License apache 2.
* 5e4ea62 - barjo : Ignore maven,intelliJ,eclipse files
* af331a8 - Jonathan Bardin : Update README.md
* c20f698 - Jonathan Bardin : Initial commit
```

