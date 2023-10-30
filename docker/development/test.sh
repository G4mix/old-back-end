#!/bin/sh

cd /app

dockerize -wait tcp://database:5432 -timeout 60s && mvn test && mvn test -P unit_tests