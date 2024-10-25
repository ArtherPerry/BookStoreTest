# Use a lightweight JDK 22 image
FROM eclipse-temurin:22-jdk-alpine AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven wrapper script and configuration
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Give execution permissions to the Maven wrapper
RUN chmod +x mvnw

# Build the Spring Boot app
RUN ./mvnw clean package

# Start a new stage from the base image
FROM eclipse-temurin:22-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY --from=build /app/target/BookStoreTest-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Render will use
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
