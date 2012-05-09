@ECHO OFF

SET AGENT_DIR=dummyteamA
SET CONF=conf/%AGENT_DIR%
SET PACKAGE=../target/javaagents-1.0.3.jar

CD massim/javaagents/scripts
CD %CONF%

java -ea -jar ../../%PACKAGE%

PAUSE