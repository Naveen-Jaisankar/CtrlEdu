FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/ChatService-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "app.jar"]
