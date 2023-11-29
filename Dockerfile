FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src .
RUN mvn clean package -DskipTests

FROM openjdk:17.0.9-jdk
WORKDIR /app
COPY --from=build /app/target/gamix-0.0.1-SNAPSHOT.jar /app/gamix.jar
ENTRYPOINT ["java", "-jar", "gamix.jar"]
