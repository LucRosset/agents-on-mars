@ECHO OFF

SET PACKAGE=../target/agentcontest-2012-1.0.3.jar
SET HOSTNAME=localhost
SET PORT=1099

CD ../massim/massim/scripts

java -Xss20000k -cp %PACKAGE% massim.competition2011.monitor.GraphMonitor -rmihost %HOSTNAME% -rmiport %PORT% -savexmls 

PAUSE