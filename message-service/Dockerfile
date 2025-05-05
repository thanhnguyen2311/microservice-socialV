FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
COPY target/message-service-0.0.1-SNAPSHOT.jar message-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","message-service-0.0.1-SNAPSHOT.jar"]