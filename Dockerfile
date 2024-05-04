FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file from the target directory into the container
COPY target/*.jar app.jar

# Define the command to run your application
CMD ["java", "-jar", "app.jar"]
