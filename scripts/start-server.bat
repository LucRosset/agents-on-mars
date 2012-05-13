@ECHO OFF

SET SIM_FILE=2012-3sims.xml
SET CONF=conf/%SIM_FILE%
SET PACKAGE=../target/agentcontest-2012-1.0.3.jar
SET HOSTNAME=localhost

CD ../massim/massim/scripts

MKDIR backup

java -ea -Dcom.sun.management.jmxremote -Xss2000k -Xmx600M  -DentityExpansionLimit=1000000 -DelementAttributeLimit=1000000 -Djava.rmi.server.hostname=%HOSTNAME% -jar %PACKAGE% --conf %CONF%

PAUSE