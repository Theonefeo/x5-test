FROM openjdk:17-jdk-slim
WORKDIR x5-proj
COPY /target/proj-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "proj-0.0.1-SNAPSHOT.jar"]