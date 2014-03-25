@echo off
set proj_home=E:\TankGameClientCode\AAA
set java_home=C:\Program Files\Java\jdk1.7.0_10\bin
set main_class=Test
set output_jar=server.zip

cd /d %proj_home%
"%java_home%\javac" -sourcepath src -d bin src/%main_class%.java
cd bin
"%java_home%\jar" cvfe ../%output_jar% %main_class% *.class
cd ..
"%java_home%\java" -jar %output_jar%
"%java_home%\java" -cp %output_jar% %main_class%
::"%java_home%\java" -server -cp bin;lib/flex-messaging-core.jar;lib/flex-messaging-common.jar snjdck.server.LogicServer
@pause