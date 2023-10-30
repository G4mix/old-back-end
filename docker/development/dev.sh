#!/bin/sh

cd /app

mvn wrapper:wrapper
dos2unix mvnw

dockerize -wait tcp://database:5432 -timeout 60s && ./mvnw spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005" &
while true; do
    inotifywait -e modify,create,delete,move -r ./src/ && ./mvnw compile
done
