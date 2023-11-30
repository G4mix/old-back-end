FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean compile package -DskipTests

FROM openjdk:17.0-jdk

ENV SPRING_DATASOURCE_URL=prod_spring_datasource_url
ENV SPRING_DATASOURCE_USERNAME=prod_spring_datasource_username
ENV SPRING_DATASOURCE_PASSWORD=prod_spring_datasource_password
ENV JWT_SIGNING_KEY_SECRET=prod_jwt_signing_key_secret
ENV FRONT_END_BASE_URL=prod_front_end_base_url

WORKDIR /app
COPY --from=build /app/target/gamix-0.0.1-SNAPSHOT.jar /app/gamix.jar
ENTRYPOINT ["java", "-jar", "gamix.jar"]
