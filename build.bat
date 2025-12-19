@echo off
echo ========================================
echo  Creating Deployable JAR
echo ========================================

REM Set paths
set "APP_DIR=%~dp0"
cd /d "%APP_DIR%"

REM Compile first
echo Step 1: Compiling...
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin -sourcepath src src\attendance\App.java src\attendance\models\*.java src\attendance\database\*.java src\attendance\services\*.java src\attendance\ui\components\*.java src\attendance\ui\frames\*.java src\attendance\ui\panels\*.java src\attendance\ui\theme\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Create JAR
echo Step 2: Creating JAR file...
if not exist dist mkdir dist
jar cfm dist\AttendanceSystem.jar MANIFEST.MF -C bin .

if %ERRORLEVEL% neq 0 (
    echo JAR creation failed! Make sure JAVA_HOME/bin is in your PATH.
    pause
    exit /b 1
)

echo ========================================
echo  SUCCESS! JAR created at:
echo  dist\AttendanceSystem.jar
echo ========================================
echo.
echo To run anywhere:
echo   java -jar AttendanceSystem.jar
echo.
pause
