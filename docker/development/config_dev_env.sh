#!/bin/sh

# Verifica se o arquivo .env já existe no diretório /app
if [ -e /app/.env ]; then
    echo "O arquivo .env já existe em /app. Nada será feito."
    exit 0
fi

# Verifica se o arquivo /tmp/.env existe
if [ ! -e /tmp/.env ]; then
    echo "O arquivo /tmp/.env não existe. Nenhuma variável será definida."
    exit 1
fi

# Lê o valor do clientID do arquivo /tmp/.env e define como variável de ambiente
export IMGUR_CLIENT_ID=$(grep -E '^IMGUR_CLIENT_ID=' /tmp/.env | cut -d '=' -f2-)

# Cria o arquivo .env e adiciona as variáveis de ambiente

/tmp/.env

cat <<EOL > /app/.env
SPRING_DATASOURCE_DRIVER_CLASS_NAME=$SPRING_DATASOURCE_DRIVER_CLASS_NAME
SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
JWT_SIGNING_KEY_SECRET=$JWT_SIGNING_KEY_SECRET
FRONT_END_BASE_URL=$FRONT_END_BASE_URL
IMGUR_CLIENT_ID=$IMGUR_CLIENT_ID
EOL

echo "Arquivo .env criado em /app com sucesso."