FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
COPY target/post-service-0.0.1-SNAPSHOT.jar post-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","post-service-0.0.1-SNAPSHOT.jar"]