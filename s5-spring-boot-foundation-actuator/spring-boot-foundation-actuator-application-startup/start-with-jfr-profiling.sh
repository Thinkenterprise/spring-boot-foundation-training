#!/bin/bash

echo "> Activate JFR for 30s when starting the Spring Boot application..."
echo "> JFR works only with a HotSpot JVM, a J9 JVM won't work!"

java -XX:+FlightRecorder -XX:StartFlightRecording=duration=30s,filename=spring-boot.jfr -jar target/s5-spring-boot-foundation-actuator-application-startup

if [ $? -ne 0 ]; then
    echo "> Make sure you use a HotSpot JVM and not a J9 JVM (see also 'java --version')!"
fi