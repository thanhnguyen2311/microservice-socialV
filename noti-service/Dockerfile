FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
COPY target/noti-service-0.0.1-SNAPSHOT.jar noti-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","noti-service-0.0.1-SNAPSHOT.jar"]