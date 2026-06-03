How to configure LOCAL environment with XLNets
==============================================

**[0] - Configure a WEBLOGIC SERVER as XLNets CORE**
____________________________________________________

A [weblogic server] will act as "xlnets core" exposing XLNets EJB services.
In this [weblogic server] a patched version of [n38i.jar] will be deployed

BEWARE!
> Do NOT deploy both the patched n38i.jar and the "official" n38i.jar
> Since the "official" n38i.jar is located at a shared NAS path that all [weblogic server] uses
> it's mandatory to change the [weblogic] startup script to change the [n38i.jar] path

Download XLNets._rename_to_7z file and rename from XLNets._rename_to_7z to XLNets.7z.
This file contains:

>
- n38i.ear
- n38i.jar
- n38a.jar
- n38c.jar
- n38n.jar
- iws.jar
- cryptix32.jar

Modify the [weblogic server] startup script at
> `/dominio_wls11/{domain-name}/bin/startWeblogic.sh`

change the following lines:

```
	N38OPTIONS="-Dfile.encoding=iso8859-1"

	N38_LIB="{patched-n38-path}/n38i.jar:{patched-n38-path}/n38a.jar:{patched-n38-path}/n38c.jar:{patched-n38-path}/n38n.jar:{patched-n38-path}/iws.jar:{patched-n38-path}/cryptix32.jar"

	export N38_LIB
```

put the patche n38 jars at `{patched-n38-path}` for example `/aplic/xlnets/remote/`

Deploy [n38i.ear] using the [weblogic server] console


**[1] - Clone [r01fXLNetsConfigByEnv] project https://src.alm02.itbatera.euskadi.eus/fabric/r01fconfigbyenv.git**
_____________________________________________

**BEWARE!!**
The config at the DEVELOP branch is for a WINDOWS box with a `{develop-home}` at `c:/develop`
For a LINUX box:
1. Create a NEW branch of the project
2. Adapt the config files

> Every config file has TWO files:
- The `n38x.properties`
- The `configLog.xml`

> Edit the `n38x.properties` file and change the `configLog.xml` file path:

```
		<parametro id="n38FicheroLog"><valor>c:/develop/xlnets/config/n38a/configLog.xml</valor>
```
> **BEWARE** the content of the `n38x.properties` file **MUST BE IN A SINGLE LINE!!!**

> Edit the `configLog.xml` file and change the log file path:

````
		<param name="File" value="C://develop/logs/xlnets/n38a.log" />
````
> (the content of this file does NOT need to be in a single line)



**[2] - Configure the [tomcat server]**
_______________________________________

Edit the [tomcat] startup script and add the following JVM params:

```
 -DN38IPServidor=555.0.0.1
 -DN38ENTORNO={develop-home}/projects/fabric/r01fconfigbyenv/r01fXLNetsConfigByEnv/src/main/config/default
 -Djava.net.preferIPv4Stack=true
```
For a WINDOWS box with `{develop-home}` at `c:/develop` it's:

```
 -DN38IPServidor=555.0.0.1
 -DN38ENTORNO=C:/develop/projects/fabric/r01fconfigbyenv/r01fXLNetsConfigByEnv/src/main/config/default
 -Djava.net.preferIPv4Stack=true
```
NOTE: the DN38IPServidor property is only needed in LOCAL, CANARY & DEVELOP


**[3] - Configure the [apache server]**
_______________________________________

XLNets just generates cookies for certain DNS domains (ie *.ejiedes.net or *.ejiedes.eus in development environment)
... so the application MUST be accessed from one of the [xl-nets]-supported sub-domain
ie: `http://localhost.ejiedes.net/{myAppUrlPath}`

So a LOCAL apache's [virtual host] at for example `localhost.ejiedes.net` must be configured in order for XLNets cookies to be accepted
To do so:

1. Edit the `/etc/hosts` (in linux) or `c:/windows/system32/drivers/etc` (in windows) and add an entry for the local host:

```
	127.0.0.1 localhost.ejiedes.net
```

2. Add a NEW apache virtual host with this config:

````
	# LOCAL XLNETS
	# =============================================================================
	<VirtualHost *:${r01-listen-port}>
		DocumentRoot ${r01-dev-home}/temp_dev/euskadi
		ServerName localhost.euskadi.euskadi.eus
		ServerAlias localhost.ejiedes.net localhost.ejiedes.eus localhost.sandbox.euskadi.eus localhost.develop.euskadi.eus localhost.canary.euskadi.eus

		ErrorLog "logs/localhost-euskadi-error.log"
		CustomLog "logs/localhost-euskadi-access.log" common

		RewriteEngine On
		#LogLevel alert rewrite:trace8

		<Directory "${r01-dev-home}/temp_dev/euskadi">
			Options Includes FollowSymLinks
			AllowOverride None
			Require all granted
			AddType text/html .shtml
			AddOutputFilter INCLUDES .shtml
		</Directory>

		ProxyPreserveHost On
		RewriteRule ^/r01PLATEAWeb(.*)$ http://localhost:8080/r01PLATEAWeb$1 [P]

	</VirtualHost>
```
> where the variables MUST be defined beforehand like:

```
	Define r01-listen-port 81
	Define r01-dev-home c:/develop
```

> NOTE that the DOCUMENT ROOT of the virtual is at `${r01-dev-home}/temp_dev/euskadi` but it can be changed to any location

**XLNETS CONFIG
_________________________________________________

n38 use Log4J 1.2.1 but the slf4j's bridg to Log4 DOES NOT WORK since it's designed for Log4J 2.x  
(there are other sl4fj bridges to log4-12 but it's problematic to have more than one SL4j bridge to Lo4J)  
... if the dependency with Log4J 1 is not set, it raises a "NoSuchMethodException" with the Root, Category classes, etc..  
		  
The solution is:  
1. Patch n38i OVERWRITING the n38i.exe.Log so it either do nothing or use a more actual log system  
   (this is actually the solution adopted -see n38i-log4j-patch.jar dependenci above-)  
				   
			
2. If the dependency with Log4J 1.2 is set, it works fine, but:  

- Be careful do not affect other components. Keep in mind that this is an old library!  

- Configure the n38*.properties with the log configLog.xml file location (file absolute path, NOT classpath path):  
  (If the files are not configured it works but a very annoying FileNotFoundException appears)  

	<elemento subtipo="n38n"><parametro id="n38FicheroLog"><valor>{develop-home}/r01fXLNetsConfigByEnv/src/main/config/{ENV}/config/n38{x}/configLog.xml</valor>  

WTF!! This file MUST contain a SINGLE LINE: DO NOT format the XML!
		                   	
- The configLog.xml file contains the [log file location]
	  
	  ```
		  	<?xml version="1.0" encoding="UTF-8"?>
			<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
			<log4j:configuration debug="false" xmlns:log4j="http://jakarta.apache.org/log4j/">
				<appender name="n38c" class="org.apache.log4j.RollingFileAppender">
					<param name="Append" value="true" />
					<param name="File" value="{develop-home}/log/xlnets/n38{x}.log" />
					<param name="MaxFileSize" value="5000KB" />
					<param name="MaxBackupIndex" value="5"/>
					<layout class="org.apache.log4j.PatternLayout">
						<param name="ConversionPattern" value="%d{dd MMM yyyy HH:mm:ss,SSS} %-5p [%t] %c{4} - %m%n"/>
					</layout>
				</appender>
				<root>
					<priority value ="error"/>
				</root>
			</log4j:configuration>
		``` 

