# Use a base image for Java
FROM openjdk:21-jdk-slim

# Set the working directory
ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5006"

WORKDIR /app

# Copy the built JAR file into the container
COPY target/NotificationService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port for the service
EXPOSE 8082 5006

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]