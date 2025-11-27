
# Use a lightweight JDK image
FROM eclipse-temurin:17-jdk



# Copy the JAR file into the container
COPY ./target/API-Gateway-0.0.1-SNAPSHOT.jar app.jar


# Run the jar file
CMD ["java", "-jar", "app.jar"]