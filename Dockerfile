FROM openjdk:17.0.2-jdk

WORKDIR /app

RUN microdnf install -y tar
RUN microdnf install -y maven

# Install dockerize
ENV DOCKERIZE_VERSION v0.7.0
RUN microdnf install -y wget openssl \
    && wget -O - https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz | tar xzf - -C /usr/local/bin \
    && microdnf remove -y wget openssl

CMD dockerize -wait tcp://database:5432 -timeout 60s && mvn spring-boot:run