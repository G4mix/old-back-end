#!/bin/bash

host=$1
port=$2
timeout=${3:-15}

while ! nc -v $host $port </dev/null; do
    echo "Esperando por $host:$port ..."
    sleep 1
done

echo "$host:$port está disponível!"
