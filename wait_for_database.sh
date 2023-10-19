#!/bin/bash

# Script para aguardar a disponibilidade de um serviço TCP
# Exemplo de uso:
#   ./wait_database.sh host porta -t tempo_de_espera

host=$1
port=$2
timeout=${3:-15}

# Utilizando um loop while para esperar até que a conexão seja estabelecida
while ! nc -v $host $port </dev/null; do
    echo "Esperando por $host:$port ..."
    sleep 1
done

echo "$host:$port está disponível!"
