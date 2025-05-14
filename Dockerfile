# Step 1: Use Maven and Java 17 for building the project
FROM maven:3.8.4-openjdk-17 AS builder

# Set working directory
WORKDIR /app

# Copy all files
COPY . .

# Build the project and skip tests
RUN mvn clean package -DskipTests

# Step 2: Use Java 17 to run the app
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the JAR from the builder stage
COPY --from=builder /app/target/spring_boot_backend_template-0.0.1.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
