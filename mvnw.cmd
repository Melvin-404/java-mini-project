@echo off
setlocal
set MAVEN_PROJECTBASEDIR=%~dp0
set MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR:~0,-1%
if defined JAVA_HOME goto findJavaFromJavaHome
set JAVA_EXE=java
%JAVA_EXE% -version >NUL 2>&1
if %ERRORLEVEL% NEQ 0 goto missingJava
goto init
:findJavaFromJavaHome
set JAVA_EXE=%JAVA_HOME%\bin\java.exe
if exist "%JAVA_EXE%" goto init
echo The JAVA_HOME environment variable is not defined correctly >&2
echo JAVA_HOME=%JAVA_HOME% >&2
goto end
:init
set MAVEN_WRAPPER_DIR=%MAVEN_PROJECTBASEDIR%\.mvn\wrapper
set MAVEN_WRAPPER_JAR=%MAVEN_WRAPPER_DIR%\maven-wrapper.jar
if not exist "%MAVEN_WRAPPER_JAR%" goto downloadWrapper
"%JAVA_EXE%" -cp "%MAVEN_WRAPPER_JAR%" -Dmaven.home="%MAVEN_PROJECTBASEDIR%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
goto end
:downloadWrapper
powershell -NoProfile -Command "try { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; $u='https://repo1.maven.org/maven2/io/takari/maven-wrapper/0.5.6/maven-wrapper-0.5.6.jar'; $o='%MAVEN_WRAPPER_JAR%'; New-Item -ItemType Directory -Force -Path '%MAVEN_WRAPPER_DIR%' | Out-Null; Invoke-WebRequest -UseBasicParsing -Uri $u -OutFile $o } catch { exit 1 }"
if %ERRORLEVEL% NEQ 0 goto failedDownload
"%JAVA_EXE%" -cp "%MAVEN_WRAPPER_JAR%" -Dmaven.home="%MAVEN_PROJECTBASEDIR%" -Dmaven.multiModuleProjectDirectory="%MAVEN_PROJECTBASEDIR%" org.apache.maven.wrapper.MavenWrapperMain %*
goto end
:failedDownload
echo Failed to download Maven wrapper jar. >&2
exit /b 1
:missingJava
echo JAVA_HOME is not set and java could not be found in PATH. Please set JAVA_HOME or add java to PATH. >&2
exit /b 1
:end
endlocal
