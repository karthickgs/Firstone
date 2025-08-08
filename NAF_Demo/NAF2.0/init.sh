
#!/bin/bash
# Framework Name: Novac Automation Framework
# Author: SriramR-NOVAC
# File Name: init.sh
# Description: Unix/Linux shell script to launch the NAF framework

echo "===================================================="
echo "Novac Automation Framework (NAF) - Test Execution"
echo "===================================================="

# Check if TestData directory exists, create it if it doesn't
if [ ! -d "./TestData" ]; then
    echo "Creating TestData directory..."
    mkdir -p "./TestData"
fi

# Check if RunManager.xlsx exists, if not inform the user
if [ ! -f "./TestData/RunManager.xlsx" ]; then
    echo "WARNING: RunManager.xlsx not found in TestData directory!"
    echo "Please create this file with the required configuration."
    echo "A template should be provided in the project documentation."
    read -p "Press Enter to continue..."
    exit 1
fi

# Check if required directories exist
if [ ! -d "./src/main/resources/TestScripts" ]; then
    echo "Creating TestScripts directory..."
    mkdir -p "./src/main/resources/TestScripts"
fi

if [ ! -d "./src/main/resources/ObjectRepo" ]; then
    echo "Creating ObjectRepo directory..."
    mkdir -p "./src/main/resources/ObjectRepo"
fi

# Check if we need to build the project first
if [ ! -f "./target/NAF-1.0-SNAPSHOT-jar-with-dependencies.jar" ]; then
    echo "JAR file not found. Building project with Maven..."
    mvn clean package -DskipTests
    if [ $? -ne 0 ]; then
        echo "Maven build failed with error code $?"
        read -p "Press Enter to continue..."
        exit $?
    fi
    echo "Maven build completed successfully."
fi

# Set the Java classpath including all required JAR files
CLASSPATH="./target/NAF-1.0-SNAPSHOT-jar-with-dependencies.jar:./lib/*:./target/lib/*"

echo "Starting NAF execution..."
echo ""

# Execute the main class of the framework with additional JVM arguments to handle warnings
java --add-opens java.base/java.lang=ALL-UNNAMED --enable-native-access=ALL-UNNAMED -classpath $CLASSPATH com.novac.naf.Main

echo ""
if [ $? -eq 0 ]; then
    echo "NAF execution completed successfully!"
else
    echo "NAF execution failed with error code $?"
fi

echo ""
echo "HTML reports have been generated in the Reports folder."
echo ""

read -p "Press Enter to continue..."
