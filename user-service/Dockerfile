FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
COPY target/user-service-0.0.1-SNAPSHOT.jar user-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","user-service-0.0.1-SNAPSHOT.jar"]