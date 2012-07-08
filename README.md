Floodlight
==========
Place the following folders inside 
Floodlight/src/main/java
  flowhashtable
	flowtracker
***********************************************
Inside 
Floodlight/src/main/resources/META-INF/
   services/net.floodlightcontroller.core.module.IFloodlightModule 
  
Place the following line (i.e. the newly added module name) at the end
net.floodlightcontroller.flowtracker.FlowTracker

***********************************************
Inside 
Floodlight/src/main/resources/floodlightdefault.properties

Place the line net.floodlightcontroller.flowtracker.FlowTracker
 after PktInProcessingTime 
Ex:

net.floodlightcontroller.perform.PktInProcessingTime,\
net.floodlightcontroller.flowtracker.FlowTracker
net.floodlightcontroller.restserver.RestApiServer.port = 8080
net.floodlightcontroller.core.FloodlightProvider.openflowport = 6633
***********************************************
