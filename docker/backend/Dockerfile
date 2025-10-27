# Use Eclipse Temurin JDK 21 as base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy pom.xml first for better layer caching
COPY backend/pom.xml .

# Download dependencies
RUN mvn dependency:go-offline -B

# Copy source code
COPY backend/src src

# Build the application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/wish-manager-backend-1.0.0.jar"]
