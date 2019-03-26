@echo off

rem ##############################################################
set current_path=%cd%
cd ..
set parent_path=%cd%
cd %current_path%

set JARS=
set CLASSPATH=
for %%i in (%parent_path%\dist\*.jar) do call jar_append.bat %%i

set CLASSPATH=
for %%i in (%parent_path%\libs\*.jar) do call jar_append.bat %%i
set CLASSPATH=%JARS%;

set LOG_PATH=%current_path%\log
set INSTALL_PATH=%current_path%\ws-scim-gw.exe
set JAVA_HOME=C:\Program Files\ojdkbuild\java-1.8.0-openjdk-1.8.0.201-1\jre
set JAVA_VM=%JAVA_HOME%\bin\server\jvm.dll
set LOG_CONF=%parent_path%\config\logback.xml
set LOG_PATH=%parent_path%\logs

@echo.
@echo PARENT_PATH  : %parent_path%
@echo CURRENT_PATH : %current_path%
@echo LOG_PATH     : %LOG_PATH%
@echo INSTALL_PATH : %INSTALL_PATH%
@echo CLASSPATH    : %CLASSPATH%
@echo JAVA_HOME    : %JAVA_HOME%
@echo JAVA_VM      : "%JAVA_VM%"
@echo LOG_CONF     : "%LOG_CONF%"
@echo LOG_PATH     : "%LOG_PATH%"

rem ##############################################################

@echo =====[ws-scim-windows install start]=====
@echo. 

ws-scim-gw.exe //IS//ws-scim-gw ^
--DisplayName="ws-scim-gw" ^
--Description="SCIM AGENT Java Daemon" ^
--Install=%INSTALL_PATH% ^
--StartClass=com.wowsanta.daemon.WowsataDaemon ^
--StartMethod=start ^
--StopClass=com.wowsanta.daemon.WowsataDaemon ^
--StopMethod=stop ^
--LogPath=%LOG_PATH% ^
--LogLevel=debug ^
--Startup=auto ^
--Classpath=%CLASSPATH% ^
--Jvm="%JAVA_VM%" ^
--StartMode=jvm ^
--StopMode=jvm ^
++JvmOptions=-Dapp.name=ws-scim-gw ^
++JvmOptions=-Dconfig.file=../config/daemon_config.json ^
++JvmOptions=-Dlogback.configurationFile=%LOG_CONF% ^
++JvmOptions=-Dlogback.path=%LOG_PATH% ^
++JvmOptions=-Dlogback.mode=debug ^

@echo.
@echo.
@echo =====[ws-scim-windows install end]=====
