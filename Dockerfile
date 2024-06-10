# Use a base image with JDK
FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar

# Copy the JAR file into the container
COPY ./target/Soliq-0.0.1-SNAPSHOT.jar app.jar

# Expose the port on which your application will run
EXPOSE 8000

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]