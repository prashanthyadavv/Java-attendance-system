@echo off
echo ========================================
echo  Attendance Management System
echo  Building and Running...
echo ========================================

REM Set paths
set "APP_DIR=%~dp0"
cd /d "%APP_DIR%"

REM Compile
echo Compiling...
if not exist bin mkdir bin
javac -encoding UTF-8 -d bin -sourcepath src src\attendance\App.java src\attendance\models\*.java src\attendance\database\*.java src\attendance\services\*.java src\attendance\ui\components\*.java src\attendance\ui\frames\*.java src\attendance\ui\panels\*.java src\attendance\ui\theme\*.java

if %ERRORLEVEL% neq 0 (
    echo Compilation failed!
    pause
    exit /b 1
)

REM Run
echo Starting application...
java -cp bin attendance.App

pause
