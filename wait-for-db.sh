#!/bin/bash

set -e

echo "Esperando pelo banco de dados..."

dockerize -wait tcp://database:5432 -timeout 60 mvn package

echo "Banco de dados está pronto!"
