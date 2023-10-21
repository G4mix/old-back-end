FROM maven:3.9-eclipse-temurin-17-alpine

WORKDIR /app

RUN apk update --no-cache && apk add --no-cache findutils wget openssl inotify-tools dos2unix

ENV DOCKERIZE_VERSION v0.7.0
RUN wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin && \
apk del wget

COPY ./docker/development/*.sh ./scripts/

RUN find ./scripts -type f -name "*.sh" -exec dos2unix {} \;
RUN chmod +x ./scripts/*.sh