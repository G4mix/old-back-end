#!/bin/sh

cd /app

# Verifica se o arquivo .env já existe no diretório /app
if [ -e /app/.env ]; then
    echo "O arquivo .env já existe em /app. Nada será feito."
    exit 0
fi

# Cria o arquivo .env e adiciona as variáveis de ambiente
cat <<EOL > /app/.env
SPRING_DATASOURCE_DRIVER_CLASS_NAME=$SPRING_DATASOURCE_DRIVER_CLASS_NAME
SPRING_DATASOURCE_URL=$SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME=$SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD=$SPRING_DATASOURCE_PASSWORD
JWT_SIGNING_KEY_SECRET=$JWT_SIGNING_KEY_SECRET
FRONT_END_BASE_URL=$FRONT_END_BASE_URL
EOL

echo "Arquivo .env criado em /app com sucesso."
