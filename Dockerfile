FROM eclipse-temurin:21

# Set working directory
WORKDIR /app

# Copy Application JAR
COPY build/libs/techtide-1.0.jar techtide.jar

# Expose port
EXPOSE 8080

# Start Application (this will be the default entry point)
ENTRYPOINT ["java", "-jar", "techtide.jar"]
