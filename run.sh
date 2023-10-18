#!/bin/bash

# Remover diretório de saída antigo, se existir
rm -rf /app/output

# Criar novo diretório de saída
mkdir /app/output

# Compilar os arquivos Java
javac -d /app/output -cp /app/.m2/repository/*:/app/src/main/java/ /app/src/main/java/com/gamix/*.java

# Executar a aplicação Java
java -cp /app/.m2/repository/*:/app/output/ com.gamix.GamixApplication