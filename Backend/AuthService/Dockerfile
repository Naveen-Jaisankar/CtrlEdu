# Use a base image for Java
FROM openjdk:21-jdk-slim

ENV JAVA_TOOL_OPTIONS="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005"

# Set the working directory
WORKDIR /app

# Copy the built JAR file into the container
COPY target/AuthService-0.0.1-SNAPSHOT.jar app.jar

# Expose the port for the service
EXPOSE 8081 5005

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]