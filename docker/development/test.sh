#!/bin/sh

cd /app

mvn install -DskipTests

dockerize -wait tcp://database:5432 -timeout 60s && mvn test
