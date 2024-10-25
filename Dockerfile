# Use a lightweight JDK 22 image
FROM eclipse-temurin:22-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/BookStoreTest-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Render will use
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
