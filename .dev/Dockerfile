# Use the official OpenJDK base image
FROM openjdk:22

# Set the working directory inside the container
WORKDIR /app

# Set a non-root user for better security and development
RUN useradd -ms /bin/bash devuser
USER devuser

# Copy the compiled Java application into the container
COPY ../out/artifacts/Javka.jar /app/Javka.jar

# Expose the application port
EXPOSE 8080

# Run the Java application when the container starts
CMD ["java", "-jar", "/app/Javka.jar"]