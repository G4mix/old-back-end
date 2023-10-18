FROM openjdk:17.0.2-jdk
WORKDIR /app
COPY target/gamix-0.0.1-SNAPSHOT.jar /app/gamix.jar
ENTRYPOINT ["java", "-jar", "gamix.jar"]