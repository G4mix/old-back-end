FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean compile package -DskipTests

FROM openjdk:17.0.2-jdk
WORKDIR /app

ENV DOCKERIZE_VERSION v0.7.0
RUN microdnf install -y openssl tar wget \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && microdnf remove -y wget

ENV SPRING_DATASOURCE_URL=prod_spring_datasource_url
ENV SPRING_DATASOURCE_USERNAME=prod_spring_datasource_username
ENV SPRING_DATASOURCE_PASSWORD=prod_spring_datasource_password
ENV JWT_SIGNING_KEY_SECRET=prod_jwt_signing_key_secret
ENV FRONT_END_BASE_URL=prod_front_end_base_url

COPY --from=build /app/target/gamix-0.0.1-SNAPSHOT.jar /app/gamix.jar
ENTRYPOINT ["/bin/bash", "-c", "dockerize -wait tcp://database:5432 -timeout 120s && java -jar /app/gamix.jar"]
