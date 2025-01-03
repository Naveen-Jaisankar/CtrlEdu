# Use a base image for Java
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/EurekaService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port for the service
EXPOSE 8761

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]