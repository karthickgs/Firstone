
@echo off
:: Framework Name: Novac Automation Framework
:: Author: SriramR-NOVAC  
:: File Name: init.bat
:: Description: Windows batch script to launch the NAF framework

@echo off
echo ====================================================
echo Novac Automation Framework (NAF) - Test Execution
echo ====================================================

:: Check if TestData directory exists, create it if it doesn't
if not exist ".\TestData" (
    echo Creating TestData directory...
    mkdir ".\TestData"
)

:: Check if RunManager.xlsx exists, if not inform the user
if not exist ".\TestData\RunManager.xlsx" (
    echo WARNING: RunManager.xlsx not found in TestData directory!
    echo Please create this file with the required configuration.
    echo A template should be provided in the project documentation.
    pause
    exit /b 1
)

:: Check if required directories exist
if not exist ".\src\main\resources\TestScripts" (
    echo Creating TestScripts directory...
    mkdir ".\src\main\resources\TestScripts"
)

if not exist ".\src\main\resources\ObjectRepo" (
    echo Creating ObjectRepo directory...
    mkdir ".\src\main\resources\ObjectRepo"
)

:: Check if we need to build the project first
if not exist ".\target\NAF-1.0-SNAPSHOT-jar-with-dependencies.jar" (
    echo JAR file not found. Building project with Maven...
    call mvn clean package -DskipTests
    if %errorlevel% neq 0 (
        echo Maven build failed with error code %errorlevel%
        pause
        exit /b %errorlevel%
    )
    echo Maven build completed successfully.
)

:: Set the Java classpath including all required JAR files
set CLASSPATH=./target/NAF-1.0-SNAPSHOT-jar-with-dependencies.jar;./lib/*;./target/lib/*

echo Starting NAF execution...
echo.

:: Execute the main class of the framework with additional JVM arguments to handle warnings
java --add-opens java.base/java.lang=ALL-UNNAMED --enable-native-access=ALL-UNNAMED -classpath %CLASSPATH% com.novac.naf.Main

echo.
if %errorlevel% == 0 (
    echo NAF execution completed successfully!
) else (
    echo NAF execution failed with error code %errorlevel%
)

echo.
echo HTML reports have been generated in the Reports folder.
echo.

pause
