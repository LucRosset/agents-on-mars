@ECHO OFF

SET PACKAGE=../target/agentcontest-2012-1.0.3.jar
SET DIRECTORY=backup

CD massim/massim/scripts

java -Xss20000k -cp %PACKAGE% massim.competition2012.monitor.GraphFileViewer -dir %DIRECTORY%

PAUSE