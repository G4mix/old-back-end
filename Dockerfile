FROM maven:3.9-eclipse-temurin-17-alpine AS dependencies
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:copy-dependencies -DoutputDirectory=/app/dependencies

FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY --from=dependencies /app/dependencies /app/.m2/repository
COPY src /app/src
RUN microdnf install -y nc
CMD ["bash"]