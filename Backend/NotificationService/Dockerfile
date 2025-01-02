# Use a base image for Java
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/NotificationService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port for the service
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
