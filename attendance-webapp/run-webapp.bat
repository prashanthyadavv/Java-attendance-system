@echo off
setlocal enabledelayedexpansion

echo ========================================
echo   Attendance Management System
echo   Spring Boot Web Application
echo ========================================
echo.

cd /d "%~dp0"

REM Auto-detect JAVA_HOME
if not defined JAVA_HOME (
    echo Detecting Java installation...
    for /f "tokens=2 delims==" %%a in ('java -XshowSettings:properties -version 2^>^&1 ^| findstr "java.home"') do (
        set "JAVA_HOME=%%a"
    )
    REM Trim leading space
    set "JAVA_HOME=!JAVA_HOME:~1!"
    echo Found Java at: !JAVA_HOME!
)

REM Check if Maven is downloaded
set MAVEN_CMD=%~dp0.mvn\maven\apache-maven-3.9.6\bin\mvn.cmd

if not exist "%MAVEN_CMD%" (
    echo.
    echo ERROR: Maven not found at %MAVEN_CMD%
    echo Please run the setup first.
    pause
    exit /b 1
)

echo.
echo Starting Spring Boot application...
echo Access at: http://localhost:8080
echo Press Ctrl+C to stop.
echo.

"%MAVEN_CMD%" spring-boot:run -f "%~dp0pom.xml"

pause
