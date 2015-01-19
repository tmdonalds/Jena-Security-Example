An attempt to integrate a custom realm with fuseki2.

1. Need to make sure that custom packages are in some derivative of org.apache.jena.fuseki.<whatever>. You need this
because of the log4j.properties file located inside of fuseki-server.jar